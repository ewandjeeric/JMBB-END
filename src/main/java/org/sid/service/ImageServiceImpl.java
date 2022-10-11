package org.sid.service;


import org.sid.entities.Image;
import org.sid.exception.ImageException;
import org.sid.exception.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageServiceImpl implements ImageService{
    private final Path fileStorageLocation;

    // Constructeur utilisé pour créer une instance de la classe FileStorageServiceImpl.
    @Autowired
    public ImageServiceImpl(Image fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getLocation()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new ImageException(
                    "Impossible de créer le répertoire dans lequel les fichiers téléchargés seront stockés", ex);
        }
    }


    /**
     * Il prend un MultipartFile comme paramètre, copie le fichier dans un emplacement prédéfini et renvoie le nom du fichier
     *
     * @param file L'objet MultipartFile qui contient le fichier chargé par l'utilisateur.
     * @return Le nom du fichier.
     */
    @Override
    public String storeFile(MultipartFile file) {

        // Normaliser le nom du fichier
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Vérifiez si le nom du fichier contient des caractères invalides
            if (fileName.contains("..")) {
                throw new ImageException(
                        "Pardon! Le nom de fichier contient une séquence de chemin non valide" + fileName);
            }

            // Copier le fichier à l'emplacement cible (remplacement du fichier existant
            // avec le même nom)
            Path targetLocation = this.fileStorageLocation.resolve("erico_" + fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new ImageException("Impossible de stocker le fichier " + fileName + ". Please try again!", ex);
        }

    }

    /**
     * Il prend un nom de fichier en entrée, le convertit en un chemin de fichier, charge le fichier en tant que ressource et
     * renvoie la ressource
     *
     * @param fileName Le nom du fichier à télécharger.
     * @return Un objet Ressource.
     */
    @Override
    public Resource loadFileAsResource(String fileName) {

        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("Fichier introuvable " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("Fichier introuvable " + fileName, ex);
        }
    }

}
