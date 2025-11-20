package org.heartspores.productos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String nombre;

    private String descripcion;

    @Min(1)
    private double precio;

    @Min(0)
    private int stock;

    private String categoria;

    private String imagenUrl;
}