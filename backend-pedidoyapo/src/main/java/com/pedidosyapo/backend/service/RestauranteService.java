package com.pedidosyapo.backend.service;

import com.pedidosyapo.backend.entity.Restaurante;
import com.pedidosyapo.backend.repository.RestauranteRepository;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

@Service
public class RestauranteService {

    private final RestauranteRepository repository;

    public RestauranteService(RestauranteRepository repository) {
        this.repository = repository;
    }

    public List<Restaurante> findAll() {
        return repository.findAll();
    }

    @Nullable
    public Restaurante findById(@NonNull Long id) {
        return repository.findById(id).orElse(null);
    }

    @NonNull
    public Restaurante save(@NonNull Restaurante restaurante) {
        return repository.save(restaurante);
    }

    public Restaurante update(@NonNull Long id, @NonNull Restaurante data) {
        Restaurante existente = repository.findById(id).orElse(null);
        if (existente == null) return null;

        existente.setNombre(data.getNombre());
        existente.setDireccion(data.getDireccion());
        existente.setImagenUrl(data.getImagenUrl());

        return repository.save(existente);
    }

    public void delete(@NonNull Long id) {
        repository.deleteById(id);
    }
}
