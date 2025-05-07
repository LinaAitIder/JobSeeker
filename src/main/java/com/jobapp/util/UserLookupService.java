package com.jobapp.util;

import com.jobapp.config.UserPrincipal;
import com.jobapp.repository.CandidatRepository;
import com.jobapp.repository.RecruteurRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserLookupService {

    private final CandidatRepository candidatRepository;
    private final RecruteurRepository recruteurRepository;

    public UserLookupService(CandidatRepository candidatRepository, RecruteurRepository recruteurRepository) {
        this.candidatRepository = candidatRepository;
        this.recruteurRepository = recruteurRepository;
    }

    public UserPrincipal loadUserByEmailAndRole(String email, String role) {
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + role)
        );

        if ("CANDIDAT".equalsIgnoreCase(role)) {
            return candidatRepository.findByEmail(email)
                    .map(c -> new UserPrincipal(
                            c.getId(),
                            c.getEmail(),
                            role,
                            authorities))
                    .orElseThrow(() -> new UsernameNotFoundException("Candidat non trouvé"));
        } else if ("RECRUTEUR".equalsIgnoreCase(role)) {
            return recruteurRepository.findByEmail(email)
                    .map(r -> new UserPrincipal(
                            r.getId(),
                            r.getEmail(),
                            role,
                            authorities))
                    .orElseThrow(() -> new UsernameNotFoundException("Recruteur non trouvé"));
        } else {
            throw new IllegalArgumentException("Rôle inconnu: " + role);
        }
    }
}
