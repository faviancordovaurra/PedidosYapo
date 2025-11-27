package com.pedidosyapo.backend.repository;

import com.pedidosyapo.backend.entity.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
}
