package com.jobapp.controller;

import com.jobapp.dto.request.EntrepriseRequest;
import com.jobapp.dto.response.EntrepriseResponse;
import com.jobapp.service.EntrepriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/entreprises")
public class EntrepriseController {

    @Autowired
    private EntrepriseService entrepriseService;

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
}