package org.heartspores.pedidos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue
    private UUID id;

    private LocalDateTime fecha;

    private String estado;

    private double total;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineaPedido> items;
}