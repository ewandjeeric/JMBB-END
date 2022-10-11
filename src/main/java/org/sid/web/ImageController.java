package org.sid.web;



import org.sid.dao.ImageRepository;
import org.sid.entities.Image;
import org.sid.entities.Personnel;
import org.sid.service.ImageService;
import org.sid.service.PersonnelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Il prend un fichier comme paramètre, le stocke dans le système de fichiers et renvoie un objet FileStorageProperties
 * avec le nom du fichier, l'URI de téléchargement, le type de contenu et la taille
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService fileStorageService;

    @Autowired
    private ImageRepository FileStoragePropertiesRepo;

    @Autowired
    private PersonnelService personnelService;


    /**
     * Il prend un fichier comme paramètre, le stocke dans le système de fichiers et renvoie un objet FileStorageProperties
     * avec le nom du fichier, l'URI de téléchargement, le type de contenu et la taille.
     *
     * @param file Le MultipartFile qui a été téléchargé
     * @return Un objet FileStorageProperties.
     */
    @PostMapping("/uploadImage/{username}")
    @PostAuthorize("hasAuthority('IMAGE_UPLOADE')")
    public Image uploadFile(@RequestParam("file") MultipartFile file, @PathVariable(name = "username")String username) {

        String fileName = "jmbImage_" + fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadImage/")
                .path(fileName).toUriString();

        Personnel personnel = personnelService.findByUsername(username);

        return FileStoragePropertiesRepo
                .save(new Image(fileName, fileDownloadUri, file.getContentType(), file.getSize(), personnel));
    }


    /**
     * Il prend le nom du fichier comme paramètre, charge le fichier en tant que ressource, détermine le type de contenu du
     * fichier, définit le type de contenu de la réponse, définit la disposition du contenu de la réponse et renvoie la
     * réponse.
     *
     * @param fileName Le nom du fichier à télécharger.
     * @param request L'objet HttpServletRequest.
     * @return Un objet ResponseEntity.
     */
    @GetMapping("/downloadImage/{fileName:.+}")
    @PostAuthorize("hasAuthority('IMAGE_DOWNLOAD')")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Impossible de déterminer le type de fichier.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
