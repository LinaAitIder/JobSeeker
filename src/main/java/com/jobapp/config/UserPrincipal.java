package com.jobapp.config;

import com.jobapp.repository.CandidatRepository;
import com.jobapp.repository.RecruteurRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private final Long id;
    private final String email;
    private final String role;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String email, String role,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.authorities = authorities;
    }

    public static UserPrincipal createWithIdLookup(String email, String role,
                                                   CandidatRepository candidatRepo, RecruteurRepository recruteurRepo) {
        Long id;
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + role)
        );

        if ("CANDIDAT".equalsIgnoreCase(role)) {
            id = candidatRepo.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Candidat non trouvé"))
                    .getId();
        } else {
            id = recruteurRepo.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Recruteur non trouvé"))
                    .getId();
        }

        return new UserPrincipal(id, email, role, authorities);
    }

    public Long getId() { return id; }

    public String getEmail() { return email; }

    public String getRole() { return role; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; }

    @Override
    public String getPassword() { return null; } //pas utilise avec jwt

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
