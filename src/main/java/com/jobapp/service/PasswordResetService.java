package com.jobapp.service;

import com.jobapp.dto.exception.NotFoundException;
import com.jobapp.model.Recruteur;
import com.jobapp.model.Candidat;
import com.jobapp.repository.CandidatRepository;
import com.jobapp.repository.RecruteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordResetService {
    private final CandidatRepository candidatRepository;
    private final RecruteurRepository recruteurRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetService(CandidatRepository candidatRepository,
                                RecruteurRepository recruteurRepository,
                                PasswordEncoder passwordEncoder){
        this.candidatRepository = candidatRepository;
        this.recruteurRepository = recruteurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void resetPassword(String email, String newPassword, String userType) {
        String encodedPassword = passwordEncoder.encode(newPassword);

        if ("CANDIDAT".equals(userType)) {
            Candidat candidat = candidatRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("Candidat non trouvé"));

            candidat.setMotDePasse(encodedPassword);
            candidatRepository.save(candidat);
        } else {
            Recruteur recruteur = recruteurRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("Recruteur non trouvé"));

            recruteur.setMotDePasse(encodedPassword);
            recruteurRepository.save(recruteur);
        }
    }
}
