package com.pedidosyapo.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long restauranteId;
    private Long productoId;
    private int cantidad;

    private String estado = "pendiente"; // ✔ Valor por defecto

    public Pedido() {}

    public Pedido(Long restauranteId, Long productoId, int cantidad, String estado) {
        this.restauranteId = restauranteId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRestauranteId() { return restauranteId; }
    public void setRestauranteId(Long restauranteId) { this.restauranteId = restauranteId; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
