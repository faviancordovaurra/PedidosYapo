package com.pedidosyapo.backend.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;

    // Imagen principal (opcional – para compatibilidad con tu app actual)
    private String imagenUrl;

    // ============================================================
    // 🟦 RELACIÓN: UN PRODUCTO TIENE MUCHAS IMÁGENES
    // ============================================================
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenProducto> imagenes = new ArrayList<>();

    // ============================================================
    // 🔹 CONSTRUCTORES
    // ============================================================

    public Producto() {}

    public Producto(Long id, String nombre, String descripcion, Double precio, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenUrl = imagenUrl;
    }

    // ============================================================
    // 🔹 GETTERS & SETTERS
    // ============================================================

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getNombre() { 
        return nombre; 
    }
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public String getDescripcion() { 
        return descripcion; 
    }
    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; 
    }

    public Double getPrecio() { 
        return precio; 
    }
    public void setPrecio(Double precio) { 
        this.precio = precio; 
    }

    // Imagen principal (opcional, no afecta imágenes múltiples)
    public String getImagenUrl() { 
        return imagenUrl; 
    }
    public void setImagenUrl(String imagenUrl) { 
        this.imagenUrl = imagenUrl; 
    }

    // lista de imágenes (OneToMany)
    public List<ImagenProducto> getImagenes() { 
        return imagenes; 
    }
    public void setImagenes(List<ImagenProducto> imagenes) { 
        this.imagenes = imagenes; 
    }
}
