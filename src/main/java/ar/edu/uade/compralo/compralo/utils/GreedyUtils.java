package ar.edu.uade.compralo.compralo.utils;

import ar.edu.uade.compralo.compralo.model.entity.Distancia;
import ar.edu.uade.compralo.compralo.model.entity.Producto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GreedyUtils {

    /**
     *
     * @param distancias
     * @param n
     * @return
     */
    public static Set<Producto> nRecomendaciones(List<Distancia> distancias, int n) {
        Set<Producto> recomendaciones = new HashSet<>();

        for(Distancia d : distancias){
            if(recomendaciones.size() == n) break;

            recomendaciones.add(d.getProducto());
        }

        return recomendaciones;
    }
}
