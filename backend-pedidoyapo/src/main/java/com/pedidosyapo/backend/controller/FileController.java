package com.pedidosyapo.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
@CrossOrigin(origins = "*")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${uploads.path}")
    private String uploadPath;
    @Value("${uploads.max-size-bytes:10485760}")
    private long maxFileSizeBytes;

    @PostMapping(value = "/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> subirImagen(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file sent");
            }
            if (file.getSize() > maxFileSizeBytes) {
                return ResponseEntity.status(413).body("Archivo demasiado grande");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(415).body("Tipo de archivo no soportado");
            }
            logger.info("subirImagen called, uploadPath={} file={} size={}", uploadPath, file.getOriginalFilename(), file.getSize());
            // Crear nombre único
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path dir = Paths.get(uploadPath);
            if (!Files.exists(dir)) Files.createDirectories(dir);
            Path path = dir.resolve(fileName);
            // Guardar archivo
            Files.write(path, file.getBytes());

            // Devolver URL accesible
            String url = "http://localhost:8085/uploads/" + fileName;

            return ResponseEntity.ok(url);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error subiendo archivo");
        }
    }
}
