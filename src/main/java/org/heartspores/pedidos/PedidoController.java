package org.heartspores.pedidos;

import lombok.RequiredArgsConstructor;
import org.heartspores.dto.CrearPedidoDTO;
import org.heartspores.dto.LineaPedidoDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PedidoController {

    private final PedidoService service;

    @PostMapping
    public Pedido crear(@RequestBody CrearPedidoDTO dto) {
        return service.crearPedido(dto);
    }

    @GetMapping
    public List<Pedido> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Pedido obtener(@PathVariable UUID id) {
        return service.obtener(id);
    }

    @PostMapping("/{id}/agregar")
    public Pedido agregarProducto(
            @PathVariable UUID id,
            @RequestBody LineaPedidoDTO dto
    ) {
        return service.agregarProducto(id, dto);
    }

    @PutMapping("/{id}/linea/{productoId}")
    public Pedido actualizarLinea(
            @PathVariable UUID id,
            @PathVariable UUID productoId,
            @RequestBody LineaPedidoDTO dto
    ) {
        return service.actualizarLinea(id, productoId, dto.cantidad);
    }

    @DeleteMapping("/{id}/linea/{productoId}")
    public Pedido eliminarLinea(
            @PathVariable UUID id,
            @PathVariable UUID productoId
    ) {
        return service.eliminarLinea(id, productoId);
    }

    @PutMapping("/{id}/estado/{nuevo}")
    public Pedido cambiarEstado(
            @PathVariable UUID id,
            @PathVariable String nuevo
    ) {
        return service.cambiarEstado(id, nuevo);
    }

    @DeleteMapping("/{id}/cancelar")
    public void cancelar(@PathVariable UUID id) {
        service.cancelar(id);
    }
}