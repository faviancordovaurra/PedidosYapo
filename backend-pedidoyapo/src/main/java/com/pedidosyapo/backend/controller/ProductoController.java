package com.pedidosyapo.backend.controller;

import com.pedidosyapo.backend.entity.ImagenProducto;
import com.pedidosyapo.backend.entity.Producto;
import com.pedidosyapo.backend.service.ProductoService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/productos")
public class ProductoController {

    private static final Logger logger = LoggerFactory.getLogger(ProductoController.class);

    private final ProductoService service;

    @Value("${uploads.path}")
    private String uploadFolder;

    @Value("${uploads.max-size-bytes:10485760}") // 10MB por defecto
    private long maxFileSizeBytes;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // ==========================================================
    // 1. CREAR PRODUCTO
    // ==========================================================
    @PostMapping
    public Producto create(@RequestBody Producto p) {
        p.setImagenUrl(null); // imagen principal opcional
        return service.save(p);
    }

    // ==========================================================
    // 2. SUBIR MÚLTIPLES IMÁGENES
    // ==========================================================
    @PostMapping(value = "/{id}/imagenes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadMultipleImages(
            @PathVariable @NonNull Long id,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "files[]", required = false) List<MultipartFile> filesArray
    ) throws IOException {

        if (files == null) files = filesArray;
        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body("No se enviaron imágenes");
        }

        Producto producto = service.findById(id);
        if (producto == null) return ResponseEntity.notFound().build();

        // Normalizar ruta (Windows compatible)
        String path = uploadFolder.replace("\\", "/");

        File carpeta = new File(path);
        if (!carpeta.exists()) carpeta.mkdirs();

        logger.info("Subiendo {} imágenes para producto {}", files.size(), id);

        for (MultipartFile file : files) {

            if (file.getSize() > maxFileSizeBytes)
                return ResponseEntity.status(413).body("Alguna imagen supera el tamaño máximo permitido.");

            if (file.getContentType() == null || !file.getContentType().startsWith("image/"))
                return ResponseEntity.status(415).body("Archivo inválido: solo se permiten imágenes.");

            String nombreArchivo = id + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destino = new File(carpeta, nombreArchivo);

            file.transferTo(destino);
            service.saveImage(nombreArchivo, producto);
        }

        return ResponseEntity.ok("Imágenes subidas: " + files.size());
    }

    // ==========================================================
    // 3. LISTAR OBJETOS ImagenProducto
    // ==========================================================
    @GetMapping("/{id}/imagenes")
    public ResponseEntity<List<ImagenProducto>> getImagenes(@PathVariable @NonNull Long id) {
        Producto p = service.findById(id);
        if (p == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(p.getImagenes());
    }

    // ==========================================================
    // 3B. LISTAR URLs COMPLETAS DE IMÁGENES
    // ==========================================================
    @GetMapping("/{id}/imagenes/url")
    public ResponseEntity<List<String>> getImagenesUrl(@PathVariable @NonNull Long id) {

        Producto p = service.findById(id);
        if (p == null) return ResponseEntity.notFound().build();

        List<String> urls = p.getImagenes().stream()
                .map(img -> "http://localhost:8085/uploads/" + img.getUrl()) // <- CORREGIDO (usa getUrl())
                .toList();

        return ResponseEntity.ok(urls);
    }

    // ==========================================================
    // 4. ELIMINAR UNA IMAGEN ESPECÍFICA
    // ==========================================================
    @DeleteMapping("/imagenes/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable @NonNull Long imageId) {
        service.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }

    // ==========================================================
    // 5. CRUD BÁSICO
    // ==========================================================
    @GetMapping
    public List<Producto> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable @NonNull Long id) {
        Producto p = service.findById(id);
        return (p != null) ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable @NonNull Long id, @RequestBody @NonNull Producto p) {
        Producto actualizado = service.update(id, p);
        return (actualizado != null) ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @NonNull Long id) {
        service.delete(id);
    }
}
