package com.jobapp.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMs) {

        // Validation des paramètres
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalArgumentException("La clé secrète JWT ne peut pas être vide");
        }

        try {
            byte[] keyBytes = Base64.getDecoder().decode(secret);
            this.secretKey = Keys.hmacShaKeyFor(keyBytes);
            this.expirationMs = expirationMs;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("La clé JWT n'est pas un Base64 valide", e);
        }
    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("Token expiré: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("Token non supporté: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("Token malformé: " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("Signature invalide: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Claim vide: " + e.getMessage());
        }
        return false;
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Méthode utilitaire pour générer une nouvelle clé (à utiliser une seule fois)
    public static String generateNewSecretKey() {
        return Base64.getEncoder().encodeToString(
                Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded()
        );
    }

    public String refreshToken(String oldToken) {
        // 1. Vérifier que l'ancien token est valide (même expiré)
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(oldToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // On accepte quand meme les tokens expires pour le refresh
            claims = e.getClaims();
        } catch (Exception e) {
            throw new MalformedJwtException("Token invalide: " + e.getMessage());
        }

        // 2. Vérifier que le token n'est pas trop vieux pour être rafraîchi
        Date issuedAt = claims.getIssuedAt();
        if (issuedAt.before(new Date(System.currentTimeMillis() - expirationMs * 2))) {
            throw new ExpiredJwtException(null, claims, "Token trop ancien pour être rafraîchi");
        }

        // 3. Creer un nouveau token avec les memes claims
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateTokenSignature(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}