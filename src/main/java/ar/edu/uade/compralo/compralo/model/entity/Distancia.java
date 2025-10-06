package ar.edu.uade.compralo.compralo.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Distancia {
    private final Producto producto;
    private final Double distancia;
}
