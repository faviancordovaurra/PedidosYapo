package com.pedidosyapo.backend.service;

import com.pedidosyapo.backend.entity.ImagenProducto;
import com.pedidosyapo.backend.entity.Producto;
import com.pedidosyapo.backend.repository.ImagenProductoRepository;
import com.pedidosyapo.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repo;
    private final ImagenProductoRepository imagenRepo;

    public ProductoService(ProductoRepository repo, ImagenProductoRepository imagenRepo) {
        this.repo = repo;
        this.imagenRepo = imagenRepo;
    }

    // ============================================================
    // 🔹 CRUD PRINCIPAL DE PRODUCTOS
    // ============================================================

    public List<Producto> findAll() {
        return repo.findAll();
    }

    @Nullable
    public Producto findById(@NonNull Long id) {
        return repo.findById(id).orElse(null);
    }

    @NonNull
    public Producto save(@NonNull Producto p) {
        return repo.save(p);
    }

    @Nullable
    public Producto update(@NonNull Long id, @NonNull Producto nuevo) {
        Producto p = findById(id);
        if (p == null) return null;

        p.setNombre(nuevo.getNombre());
        p.setDescripcion(nuevo.getDescripcion());
        p.setPrecio(nuevo.getPrecio());
        p.setImagenUrl(nuevo.getImagenUrl()); // imagen principal opcional

        return repo.save(p);
    }

    public void delete(@NonNull Long id) {
        repo.deleteById(id);
    }

    // ============================================================
    // 🔹 GESTIÓN DE IMÁGENES (MÚLTIPLES POR PRODUCTO)
    // ============================================================

    // Guardar una imagen
    public ImagenProducto saveImage(String fileName, Producto producto) {
        ImagenProducto img = new ImagenProducto(fileName, producto);
        return imagenRepo.save(img);
    }

    // Eliminar una imagen
    public void deleteImage(@NonNull Long imageId) {
        imagenRepo.deleteById(imageId);
    }
}
