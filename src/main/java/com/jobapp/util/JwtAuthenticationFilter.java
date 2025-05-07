package com.jobapp.util;

import com.jobapp.config.UserPrincipal;
import com.jobapp.repository.CandidatRepository;
import com.jobapp.repository.RecruteurRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/refresh-token",
            "/api/auth/exists/email",
            "/api/auth/password"
    );

    private final JwtUtil jwtUtil;
    private final CandidatRepository candidatRepository;
    private final RecruteurRepository recruteurRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   CandidatRepository candidatRepository,
                                   RecruteurRepository recruteurRepository) {
        this.jwtUtil = jwtUtil;
        this.candidatRepository = candidatRepository;
        this.recruteurRepository = recruteurRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        // 1. Passer les requêtes OPTIONS et les routes exclues
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) ||
                shouldNotFilter(request)) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Essayer d'authentifier si token présent
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (jwtUtil.validateToken(token)) {
                    Claims claims = jwtUtil.extractAllClaims(token);
                    String email = claims.getSubject();
                    String role = claims.get("role", String.class);

                    // Création de l'authentication
                    UserPrincipal principal = UserPrincipal.createWithIdLookup(
                            email,
                            role,
                            candidatRepository,
                            recruteurRepository
                    );

                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            principal.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Authentifié : " + email);
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("Erreur JWT (non bloquante)", e);
        }

        // 3. Toujours continuer la chaîne
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return EXCLUDED_PATHS.stream()
                .anyMatch(path -> request.getRequestURI().startsWith(path));
    }
}
