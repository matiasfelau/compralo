package ar.edu.uade.compralo.compralo.model.entity;

import lombok.*;

@Builder
@Getter
public class Recomendacion {
    private Producto producto;
    private Double interes;
}
