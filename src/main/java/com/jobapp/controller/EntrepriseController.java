package com.jobapp.controller;

import com.jobapp.dto.request.EntrepriseRequest;
import com.jobapp.dto.response.EntrepriseResponse;
import com.jobapp.service.EntrepriseService;
import com.jobapp.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/entreprises")
public class EntrepriseController {

    @Autowired
    private final EntrepriseService entrepriseService;
    private final FileStorageService fileStorageService;

    @Autowired
    public EntrepriseController(EntrepriseService entrepriseService,
                                FileStorageService fileStorageService){
        this.fileStorageService = fileStorageService;
        this.entrepriseService = entrepriseService;
    }

    @PostMapping
    public ResponseEntity<EntrepriseResponse> create(@RequestBody EntrepriseRequest request) {
        return ResponseEntity.ok(entrepriseService.createEntreprise(request));
    }

    @PostMapping(value = "/{id}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EntrepriseResponse> uploadLogo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(entrepriseService.updateLogo(id, file));
    }

    @GetMapping
    public ResponseEntity<List<EntrepriseResponse>> getAll() {
        return ResponseEntity.ok(entrepriseService.getAllEntreprises());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrepriseResponse> getInfosEntreprise(@PathVariable Long id) {
        EntrepriseResponse entreprise = entrepriseService.getEntrepriseById(id);
        return ResponseEntity.ok(entreprise);
    }

    @GetMapping("/{id}/logo")
    public ResponseEntity<Resource> getLogo(@PathVariable Long id) {
        try {
            EntrepriseResponse entreprise = entrepriseService.getEntrepriseById(id);
            if (entreprise.logoPath() == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = fileStorageService.getFilePath(entreprise.logoPath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // ou MediaType.IMAGE_PNG
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}