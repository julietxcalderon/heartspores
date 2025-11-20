package org.heartspores.productos;

import lombok.RequiredArgsConstructor;
import org.heartspores.dto.ActualizarProductoDTO;
import org.heartspores.dto.CrearProductoDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductoController {

    private final ProductoService service;

    @PostMapping
    public Producto crear(@RequestBody CrearProductoDTO dto) {
        Producto p = Producto.builder()
                .nombre(dto.nombre)
                .descripcion(dto.descripcion)
                .precio(dto.precio)
                .stock(dto.stock)
                .categoria(dto.categoria)
                .imagenUrl(dto.imagenUrl)
                .build();

        return service.crear(p);
    }

    @GetMapping
    public List<Producto> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Producto obtener(@PathVariable UUID id) {
        return service.obtener(id);
    }

    @PutMapping("/{id}")
    public Producto actualizar(
            @PathVariable UUID id,
            @RequestBody ActualizarProductoDTO dto) {
        return service.actualizar(id, dto.precio, dto.stock);
    }

    @DeleteMapping("/{id}")
    public boolean eliminar(@PathVariable UUID id) {
        return service.eliminar(id);
    }
}