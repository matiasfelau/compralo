package ar.edu.uade.compralo.compralo.service;

import ar.edu.uade.compralo.compralo.model.entity.Producto;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Service
public class CarritoService {

    private final Set<Producto> descartados = new HashSet<>();

    /**
     * Agrega un producto a la lista de descarte.
     */
    public void descarte(Producto producto) {
        if (producto != null) {
            descartados.add(producto);
        }
    }


    /**
     * Devuelve una vista de solo lectura de los descartados actuales.
     */
    public Set<Producto> getDescartados() {
        return Collections.unmodifiableSet(descartados);
    }
}
