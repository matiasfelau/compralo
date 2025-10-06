package ar.edu.uade.compralo.compralo.utils;

import ar.edu.uade.compralo.compralo.model.entity.Producto;
import ar.edu.uade.compralo.compralo.model.entity.Relacion;

import java.util.*;

public class DijkstraUtils {

    /**
     * Usa Dijkstra para calcular el potencial interés hacia cada producto, respecto de un producto raíz.
     * Mantiene compatibilidad con la firma original.
     */
    public static Map<Producto, Double> calcularCaminos(Set<Producto> productos, Producto raiz) {
        return calcularCaminos(productos, raiz, Collections.emptySet());
    }

    /**
     * Variante con "poda": ignora nodos descartados y no relaja aristas hacia ellos.
     *
     * @param productos conjunto de productos alcanzables
     * @param raiz nodo de inicio
     * @param descartados productos a ignorar (no se expanden ni se relajan aristas hacia ellos)
     */
    public static Map<Producto, Double> calcularCaminos(Set<Producto> productos, Producto raiz, Set<Producto> descartados) {
        PriorityQueue<Producto> pendiente = new PriorityQueue<>();
        Map<Producto, Double> distancias = new HashMap<>();
        Set<Producto> visitados = new HashSet<>();

        if (descartados != null && descartados.contains(raiz)) {
            return Collections.emptyMap();
        }

        pendiente.add(raiz);

        for (Producto p : productos) {
            distancias.put(p, Double.POSITIVE_INFINITY);
        }
        distancias.put(raiz, 0.0);

        while (!pendiente.isEmpty()) {
            Producto producto = pendiente.poll();

            if (producto != null) {
                if (!visitados.contains(producto)) {

                    // Poda: si el nodo actual está descartado, no lo expando
                    if (descartados != null && descartados.contains(producto)) {
                        visitados.add(producto);
                        continue;
                    }

                    visitados.add(producto);

                    for (Relacion relacion : producto.getRelacionados()) {
                        Producto relacionado = relacion.getProducto();

                        // Poda: no relajar aristas hacia nodos descartados
                        if (descartados != null && descartados.contains(relacionado)) {
                            continue;
                        }

                        Double nuevaDistancia = distancias.get(producto) + relacion.getPeso();
                        if (nuevaDistancia < distancias.get(relacionado)) {
                            distancias.put(relacionado, nuevaDistancia);
                            pendiente.add(relacionado);
                        }
                    }
                }
            }
        }

        // Limpieza final: no devolver descartados en el resultado
        if (descartados != null && !descartados.isEmpty()) {
            for (Producto d : descartados) {
                distancias.remove(d);
            }
        }

        return distancias;
    }
}
