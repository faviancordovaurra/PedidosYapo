package com.pedidosyapo.backend.controller;

import com.pedidosyapo.backend.entity.Restaurante;
import com.pedidosyapo.backend.service.RestauranteService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/restaurantes")
public class RestauranteController {

    private final RestauranteService service;

    public RestauranteController(RestauranteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Restaurante> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> getById(@PathVariable @NonNull Long id) {
        Restaurante r = service.findById(id);
        return r != null ? ResponseEntity.ok(r) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Restaurante create(@RequestBody @NonNull Restaurante r) {
        return service.save(r);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurante> update(
            @PathVariable @NonNull Long id,
            @RequestBody @NonNull Restaurante r
    ) {
        Restaurante actualizado = service.update(id, r);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @NonNull Long id) {
        service.delete(id);
    }
}
