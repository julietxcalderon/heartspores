package org.heartspores.productos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository repo;

    public Producto crear(Producto p) {
        return repo.save(p);
    }

    public List<Producto> listar() {
        return repo.findAll();
    }

    public Producto obtener(UUID id) {
        return repo.findById(id).orElse(null);
    }

    public Producto actualizar(UUID id, Double precio, Integer stock) {
        Producto p = obtener(id);
        if (p == null) return null;

        if (precio != null && precio > 0) p.setPrecio(precio);
        if (stock != null && stock >= 0) p.setStock(stock);

        return repo.save(p);
    }

    public boolean eliminar(UUID id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}