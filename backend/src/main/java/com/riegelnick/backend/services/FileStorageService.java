package com.riegelnick.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    // Constructor creates the upload directory if it doesn't exist
    //"${file.upload-dir}" looks up the value in application.properties, and @Value assigns it to the uploadDir parameter?
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        // resolves to /app(backend)/uploads
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        
        try {
            // creates the directory if it doesn't exist
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create upload directory", ex);
        }
    }

    // MultipartFile is a Spring class that represents an uploaded file received with the methods .getOriginalFilename() .getInputStream() .getSize() .getContentType() .isEmpty() etc.
    public String storeFile(MultipartFile file) {
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + fileExtension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return newFilename;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + newFilename, ex);
        }
    }
}