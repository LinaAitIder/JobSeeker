package com.jobapp.service;

import com.jobapp.config.FileStorageProperties;
import com.jobapp.dto.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        // Normalise le chemin et crée le dossier s'il n'existe pas
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException(
                    "Impossible de créer le dossier de stockage : " + this.fileStorageLocation, ex);
        }
    }

    /**
     * Stocke un fichier dans un sous-dossier specifique
     * @param file Fichier a enregistrer
     * @param subfolder Sous-dossier (ex: "cvs", "certifications")
     * @param userId ID de l'utilisateur (pour creer un dossier unique)
     * @return Chemin relatif du fichier (ex: "/cvs/123/cv.pdf")
     */
    public String storeFile(MultipartFile file, String subfolder, Long userId) {
        try {
            // 1. Cree le dossier utilisateur (ex: "uploads/cvs/123/")
            Path userFolder = this.fileStorageLocation
                    .resolve(subfolder)
                    .resolve(userId.toString());
            Files.createDirectories(userFolder);

            // 2. Genere un nom de fichier sécurise
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            fileName = System.currentTimeMillis() + "_" + fileName; // Préfixe temporel pour éviter les conflits

            // 3. Verifie les injections de chemin
            if (fileName.contains("..")) {
                throw new FileStorageException("Nom de fichier invalide : " + fileName);
            }

            // 4. Copie le fichier
            Path targetLocation = userFolder.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "/" + subfolder + "/" + userId + "/" + fileName;

        } catch (IOException ex) {
            throw new FileStorageException(
                    "Échec de l'enregistrement du fichier " + file.getOriginalFilename(), ex);
        }
    }

    /**
     * Supprime un fichier existant
     */
    public void deleteFile(String filePath) throws IOException {
        Path fullPath = this.fileStorageLocation.resolve(filePath.substring(1)); // Retire le "/" initial
        Files.deleteIfExists(fullPath);
    }

    public String storeProfilePhoto(MultipartFile file, String userType, Long userId) {
        return storeFile(file, "profile-photos/" + userType, userId);
    }

    public String storeCv(MultipartFile file, Long candidatId) {
        return storeFile(file, "cvs", candidatId);
    }

    public String storeCertification(MultipartFile file, Long candidatId) {
        return storeFile(file, "certifications", candidatId);
    }

    public String storeCompanyLogo(MultipartFile file, Long companyId) {
        try {
            // 1. Creer le dossier spécifique pour les logos d'entreprise
            Path companyLogoFolder = this.fileStorageLocation
                    .resolve("company-logos")
                    .resolve(companyId.toString());
            Files.createDirectories(companyLogoFolder);

            // 2. Génere un nom de fichier sécurisé
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            fileName = "logo_" + System.currentTimeMillis() + "_" + fileName;

            // 3. Vérifie les injections de chemin
            if (fileName.contains("..")) {
                throw new FileStorageException("Nom de fichier invalide : " + fileName);
            }

            // 4. Copie le fichier
            Path targetLocation = companyLogoFolder.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 5. Retourne le chemin relatif complet
            return "/company-logos/" + companyId + "/" + fileName;
        } catch (IOException ex) {
            throw new FileStorageException(
                    "Échec de l'enregistrement du logo d'entreprise " + file.getOriginalFilename(), ex);
        }
    }

    public String storeLettreMotivation(MultipartFile file, Long candidatId, Long offreId) {
        try {
            // Créer le dossier spécifique pour cette candidature
            Path userFolder = this.fileStorageLocation
                    .resolve("lettres-motivation")
                    .resolve(candidatId.toString())
                    .resolve(offreId.toString());
            Files.createDirectories(userFolder);

            // Génere un nom de fichier sécurisé
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            fileName = "lettre_" + System.currentTimeMillis() + "_" + fileName;

            // Vérifie les injections de chemin
            if (fileName.contains("..")) {
                throw new FileStorageException("Nom de fichier invalide : " + fileName);
            }

            // Copie le fichier
            Path targetLocation = userFolder.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Retourne le chemin relatif complet
            return "/lettres-motivation/" + candidatId + "/" + offreId + "/" + fileName;
        } catch (IOException ex) {
            throw new FileStorageException(
                    "Échec de l'enregistrement de la lettre de motivation " + file.getOriginalFilename(), ex);
        }
    }

    public Path getFilePath(String relativePath) {
        return this.fileStorageLocation.resolve(relativePath.substring(1)).normalize();
    }

    public Resource loadFileAsResource(String filePath) throws FileStorageException {
        try {
            String normalizedPath = filePath.startsWith("/") ? filePath.substring(1) : filePath;
            Path path = this.fileStorageLocation.resolve(normalizedPath).normalize();

            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileStorageException("Fichier introuvable ou non lisible: " + filePath);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("Chemin de fichier invalide: " + filePath, ex);
        }
    }

}