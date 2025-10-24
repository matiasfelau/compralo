package ar.edu.uade.compralo.compralo.service;

import ar.edu.uade.compralo.compralo.model.entity.Producto;
import ar.edu.uade.compralo.compralo.model.entity.Recomendacion;
import ar.edu.uade.compralo.compralo.utils.BTUtils;
import ar.edu.uade.compralo.compralo.utils.DPUtils;
import ar.edu.uade.compralo.compralo.utils.DijkstraUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class RecomendacionService {
    private final ProductoService productoService;

    private static final int MAXIMA_PROFUNDIDAD = 2;
    private static final int MAXIMO_RECOMENDACIONES = 2;

    public Set<Producto> obtenerRecomendacionesCarrito(Set<Producto> carrito) {
        List<Recomendacion> recomendaciones = new ArrayList<>();

        for (Producto producto : carrito) {
            Set<Producto> relacionados = productoService.encontrarProductosRelacionados(producto, MAXIMA_PROFUNDIDAD);
            Map<Producto, Double> distancias = DijkstraUtils.calcularDistancias(relacionados, producto);
            recomendaciones.addAll(DPUtils.facade(distancias, MAXIMO_RECOMENDACIONES));
        }

        return BTUtils.facade(recomendaciones, MAXIMO_RECOMENDACIONES);
    }
}