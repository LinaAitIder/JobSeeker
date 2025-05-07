package com.jobapp.controller;

import com.jobapp.config.FileStorageProperties;
import com.jobapp.dto.request.CreateOffreRequest;
import com.jobapp.dto.request.UpdateRecruteurProfileRequest;
import com.jobapp.dto.request.UpdatePasswordRequest;
import com.jobapp.dto.response.*;
import com.jobapp.service.FileStorageService;
import com.jobapp.service.RecruteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

@RestController
@RequestMapping("/api/recruteur")
public class RecruteurController {

    private final RecruteurService recruteurService;
    private final FileStorageService fileStorageService;
    private final FileStorageProperties fileStorageProperties;

    @Autowired
    public RecruteurController(RecruteurService recruteurService, FileStorageService fileStorageService,
                               FileStorageProperties fileStorageProperties) {
        this.recruteurService = recruteurService;
        this.fileStorageService = fileStorageService;
        this.fileStorageProperties = fileStorageProperties;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecruteurProfileResponse> getRecruteurProfile(@PathVariable Long id) {
        return recruteurService.getRecruteurProfile(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecruteurProfile(
            @PathVariable Long id,
            @RequestBody UpdateRecruteurProfileRequest request) {
        return recruteurService.updateRecruteurProfile(id, request);
    }
    @PatchMapping("/{id}/password")
    @PreAuthorize("#id == authentication.principal.id && hasRole('RECRUTEUR')")
    public ResponseEntity<?> updateMotDePasse(
            @PathVariable Long id,
            @RequestBody UpdatePasswordRequest request) {
        return recruteurService.updateMotDePasse(
                id,
                request.getAncienMotDePasse(),
                request.getNouveauMotDePasse()
        );
    }
    @GetMapping("/offres/{offreId}/candidatures")
    @PreAuthorize("hasRole('RECRUTEUR')")
    public ResponseEntity<List<CandidatureResponse>> getCandidaturesPourOffre(
            @PathVariable Long offreId,
            @RequestParam(required = false) String statut) {

        return recruteurService.getCandidaturesPourOffre(offreId, statut);
    }

    @GetMapping("/{id}/offre")
    public ResponseEntity<List<OffreResponse>> getOffresByRecruteur(@PathVariable Long id) {
        return recruteurService.getOffresByRecruteur(id);
    }

    @PostMapping("/{id}/offre")
    public ResponseEntity<OffreResponse> createOffre(
            @PathVariable Long id,
            @RequestBody CreateOffreRequest request) {
        return recruteurService.createOffre(id, request);
    }

    @PostMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPhotoProfil(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        try {
            String filename = fileStorageService.storeProfilePhoto(file, "recruteur", id);
            recruteurService.updatePhotoProfil(id, filename);
            return ResponseEntity.ok(new MessageResponse("Photo uploadée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("FILE_ERROR", e.getMessage()));
        }
    }
    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> getPhotoProfil(@PathVariable Long id) {
        try {
            RecruteurProfileResponse response = recruteurService.getRecruteurProfile(id).getBody();
            if (response == null || response.photoProfilPath() == null) {
                return ResponseEntity.notFound().build();
            }

            Path path = Paths.get(fileStorageProperties.getUploadDir() + response.photoProfilPath());
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id && hasRole('RECRUTEUR')")
    public ResponseEntity<Void> deleteRecruteur(@PathVariable Long id) {

        // Double vérification de sécurité
        // UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        // if (!principal.getId().equals(id)) {
        //     return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        // }
        //ajouter Authentication authentication dans arguments

        return recruteurService.deleteRecruteur(id);
    }
}
