package com.jobapp.service;


import com.jobapp.dto.exception.NotFoundException;
import com.jobapp.model.Candidat;
import com.jobapp.repository.CandidatRepository;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

@Service
public class CvService {
    private static final Logger logger = LoggerFactory.getLogger(CvService.class);

    private final FileStorageService fileStorageService;
    private final CandidatRepository candidatRepository;

    public CvService(FileStorageService fileStorageService,
                     CandidatRepository candidatRepository) {
        this.fileStorageService = fileStorageService;
        this.candidatRepository = candidatRepository;
    }

    /**
     * Extrait le texte brut d'un CV
     * @param candidateId ID du candidat
     * @return Texte extrait du CV
     * @throws NotFoundException si le candidat ou CV n'existe pas
     * @throws RuntimeException si l'extraction échoue
     */
    public String extractCvText(Long candidateId) {
        Candidat candidat = candidatRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidat non trouvé"));

        if (candidat.getCvPath() == null || candidat.getCvPath().isEmpty()) {
            throw new NotFoundException("Aucun CV uploadé pour ce candidat");
        }

        try {
            Resource cvResource = fileStorageService.loadFileAsResource(candidat.getCvPath());
            return extractTextFromResource(cvResource).replaceAll("[^\\p{Print}]", "")  // Supprime les caractères non imprimables
                    .replaceAll("\\s+", " ")          // Remplace les espaces multiples
                    .trim();
        } catch (Exception e) {
            logger.error("Échec de l'extraction du texte du CV", e);
            throw new RuntimeException("Impossible d'extraire le texte du CV", e);
        }
    }

    private String extractTextFromResource(Resource resource)
            throws IOException, TikaException, SAXException {
        try (InputStream inputStream = resource.getInputStream()) {
            AutoDetectParser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler(-1); // -1 = pas de limite de taille
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();

            parser.parse(inputStream, handler, metadata, context);
            return handler.toString();
        }
    }

    // on vérifie si le condidat a un cv ou nom
    public boolean hasCv(Long candidateId) {
        return candidatRepository.findById(candidateId)
                .map(c -> c.getCvPath() != null && !c.getCvPath().isEmpty())
                .orElse(false);
    }
}