package org.heartspores.pedidos;

import jakarta.persistence.*;
import lombok.*;
import org.heartspores.productos.Producto;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineaPedido {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Producto producto;

    private int cantidad;
}