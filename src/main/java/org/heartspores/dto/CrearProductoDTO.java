package org.heartspores.dto;

import lombok.*;

@Getter @Setter
public class CrearProductoDTO {
    public String nombre;
    public String descripcion;
    public double precio;
    public int stock;
    public String categoria;
    public String imagenUrl;
}