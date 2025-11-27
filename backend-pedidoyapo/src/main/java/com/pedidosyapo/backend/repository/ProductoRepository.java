package com.pedidosyapo.backend.repository;

import com.pedidosyapo.backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
