package ar.edu.uade.compralo.compralo.utils;

import ar.edu.uade.compralo.compralo.model.entity.Producto;
import ar.edu.uade.compralo.compralo.model.entity.Relacion;

import java.util.*;

public class DijkstraUtils {

    /**
     * Variante con "poda": ignora nodos descartados y no relaja aristas hacia ellos.
     *
     * @param productos conjunto de productos alcanzables
     * @param raiz nodo de inicio
     */
    public static Map<Producto, Double> calcularDistancias(Set<Producto> productos, Producto raiz) {
        Map<Producto, Double> distancias = new HashMap<>();
        Set<Producto> visitados = new HashSet<>();

        for (Producto p : productos) {
            distancias.put(p, Double.POSITIVE_INFINITY);
        }
        distancias.put(raiz, 0.0);

        PriorityQueue<Producto> pendiente = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));

        pendiente.add(raiz);

        while (!pendiente.isEmpty()) {
            Producto producto = pendiente.poll();

            if (producto != null) {
                if (!visitados.contains(producto)) {
                    visitados.add(producto);

                    for (Relacion relacion : producto.getRelacionados()) {
                        Producto relacionado = relacion.getProducto();

                        if (productos.contains(relacionado) /*&& !visitados.contains(relacionado)*/) {
                            Double nuevaDistancia = distancias.get(producto) + relacion.getPeso();

                            if (nuevaDistancia < distancias.get(relacionado)) {
                                distancias.put(relacionado, nuevaDistancia);
                                pendiente.add(relacionado);
                            }
                        }
                    }
                }
            }
        }

        return distancias;
    }
}
