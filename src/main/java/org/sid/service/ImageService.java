package org.sid.service;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


// Interface utilisée pour stocker et charger des fichiers.
public interface ImageService {

    String storeFile(MultipartFile file);

    Resource loadFileAsResource(String fileName);
}
