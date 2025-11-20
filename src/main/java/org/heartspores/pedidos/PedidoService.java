package org.heartspores.pedidos;

import lombok.RequiredArgsConstructor;
import org.heartspores.dto.LineaPedidoDTO;
import org.heartspores.exceptions.StockInsuficienteException;
import org.heartspores.productos.Producto;
import org.heartspores.productos.ProductoRepository;
import org.heartspores.dto.CrearPedidoDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository repoPedidos;
    private final ProductoRepository repoProductos;

    public Pedido crearPedido(CrearPedidoDTO dto) {

        List<LineaPedido> lineas = new ArrayList<>();

        for (LineaPedidoDTO l : dto.items) {
            Producto p = repoProductos.findById(UUID.fromString(l.productoId)).orElseThrow();

            if (p.getStock() < l.cantidad)
                throw new StockInsuficienteException("Stock insuficiente para " + p.getNombre());
        }

        for (LineaPedidoDTO l : dto.items) {
            Producto p = repoProductos.findById(UUID.fromString(l.productoId)).orElseThrow();

            p.setStock(p.getStock() - l.cantidad);
            repoProductos.save(p);

            lineas.add(LineaPedido.builder()
                    .producto(p)
                    .cantidad(l.cantidad)
                    .build());
        }

        double total = lineas.stream()
                .mapToDouble(lp -> lp.getProducto().getPrecio() * lp.getCantidad())
                .sum();

        Pedido pedido = Pedido.builder()
                .fecha(LocalDateTime.now())
                .estado("PENDIENTE")
                .items(lineas)
                .total(total)
                .build();

        return repoPedidos.save(pedido);
    }

    public List<Pedido> listar() {
        return repoPedidos.findAll();
    }

    public Pedido obtener(UUID id) {
        return repoPedidos.findById(id).orElseThrow();
    }

    public Pedido agregarProducto(UUID pedidoId, LineaPedidoDTO dto) {

        Pedido pedido = obtener(pedidoId);

        Producto p = repoProductos.findById(UUID.fromString(dto.productoId)).orElseThrow();

        if (p.getStock() < dto.cantidad)
            throw new StockInsuficienteException("Stock insuficiente para " + p.getNombre());

        p.setStock(p.getStock() - dto.cantidad);
        repoProductos.save(p);

        Optional<LineaPedido> existente = pedido.getItems().stream()
                .filter(lp -> lp.getProducto().getId().equals(p.getId()))
                .findFirst();

        if (existente.isPresent()) {
            LineaPedido lp = existente.get();
            lp.setCantidad(lp.getCantidad() + dto.cantidad);
        } else {
            pedido.getItems().add(
                    LineaPedido.builder()
                            .producto(p)
                            .cantidad(dto.cantidad)
                            .build()
            );
        }

        recalcularTotal(pedido);
        return repoPedidos.save(pedido);
    }

    public Pedido actualizarLinea(UUID pedidoId, UUID productoId, int nuevaCantidad) {

        Pedido pedido = obtener(pedidoId);

        LineaPedido lp = pedido.getItems().stream()
                .filter(l -> l.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow();

        Producto p = lp.getProducto();

        int diferencia = nuevaCantidad - lp.getCantidad();

        if (diferencia > 0) {
            if (p.getStock() < diferencia)
                throw new StockInsuficienteException("Stock insuficiente para " + p.getNombre());

            p.setStock(p.getStock() - diferencia);
        } else {
            p.setStock(p.getStock() + Math.abs(diferencia));
        }

        repoProductos.save(p);
        lp.setCantidad(nuevaCantidad);

        if (nuevaCantidad == 0) {
            pedido.getItems().remove(lp);
        }

        recalcularTotal(pedido);
        return repoPedidos.save(pedido);
    }

    public Pedido eliminarLinea(UUID pedidoId, UUID productoId) {

        Pedido pedido = obtener(pedidoId);

        LineaPedido lp = pedido.getItems().stream()
                .filter(l -> l.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow();

        Producto p = lp.getProducto();

        p.setStock(p.getStock() + lp.getCantidad());
        repoProductos.save(p);

        pedido.getItems().remove(lp);

        recalcularTotal(pedido);
        return repoPedidos.save(pedido);
    }

    public Pedido cambiarEstado(UUID id, String nuevoEstado) {
        Pedido p = obtener(id);
        p.setEstado(nuevoEstado.toUpperCase());
        return repoPedidos.save(p);
    }

    public void cancelar(UUID id) {

        Pedido pedido = obtener(id);

        for (LineaPedido lp : pedido.getItems()) {
            Producto prod = lp.getProducto();
            prod.setStock(prod.getStock() + lp.getCantidad());
            repoProductos.save(prod);
        }

        repoPedidos.deleteById(id);
    }

    private void recalcularTotal(Pedido p) {
        double total = p.getItems().stream()
                .mapToDouble(lp -> lp.getProducto().getPrecio() * lp.getCantidad())
                .sum();
        p.setTotal(total);
    }
}