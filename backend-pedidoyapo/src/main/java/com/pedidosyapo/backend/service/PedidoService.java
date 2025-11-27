package com.pedidosyapo.backend.service;

import com.pedidosyapo.backend.entity.Pedido;
import com.pedidosyapo.backend.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Service
public class PedidoService {

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public List<Pedido> findAll() {
        return repository.findAll();
    }

    @Nullable
    public Pedido findById(@NonNull Long id) {
        return repository.findById(id).orElse(null);
    }

    @NonNull
    public Pedido save(@NonNull Pedido pedido) {
        return repository.save(pedido);
    }

    @Nullable
    public Pedido update(@NonNull Long id, @NonNull Pedido data) {
        Pedido existente = repository.findById(id).orElse(null);
        if (existente == null) return null;

        existente.setRestauranteId(data.getRestauranteId());
        existente.setProductoId(data.getProductoId());
        existente.setCantidad(data.getCantidad());
        existente.setEstado(data.getEstado());

        return repository.save(existente);
    }

    public void delete(@NonNull Long id) {
        repository.deleteById(id);
    }
}
