package com.pedidosyapo.backend.controller;

import com.pedidosyapo.backend.entity.Pedido;
import com.pedidosyapo.backend.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pedido> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getById(@PathVariable @NonNull Long id) {
        Pedido p = service.findById(id);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Pedido create(@RequestBody @NonNull Pedido p) {
        return service.save(p);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> update(@PathVariable @NonNull Long id, @RequestBody @NonNull Pedido p) {
        Pedido actualizado = service.update(id, p);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @NonNull Long id) {
        service.delete(id);
    }
}
