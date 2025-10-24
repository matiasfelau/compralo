package ar.edu.uade.compralo.compralo.utils;

import ar.edu.uade.compralo.compralo.model.entity.Producto;
import ar.edu.uade.compralo.compralo.model.entity.Recomendacion;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.min;

@Getter
@Setter
public class DPUtils {

    public static List<Recomendacion> facade(Map<Producto, Double> distancias, int maximo) {
        int cantidad = distancias.size();
        Set<Producto> camino;

        if (cantidad <= maximo) camino = distancias.keySet();
        else {
            List<Producto> productos = new ArrayList<>(distancias.keySet());
            Double[][] solucion = construirSolucion(cantidad, maximo, distancias, productos);
            camino = reconstruirCamino(maximo, solucion, productos);
        }

        return transform(camino, distancias);
    }

    private static Double[][] construirSolucion(
            int n,
            int m,
            Map<Producto, Double> distancias,
            List<Producto> productos
    ) {
        Double[][] tabla = new Double[n][m+1];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= m; j++) {
                if (j == 0) tabla[i][j] = 0.0;
//                else if (i == 0) tabla[i][j] = distancias.get(productos.get(i));
                else if (i == 0) tabla[i][j] = 0.0;
                else if (i <= j) tabla[i][j] = tabla[i-1][j] + distancias.get(productos.get(i));
                else tabla[i][j] = min(tabla[i-1][j], tabla[i-1][j-1] + distancias.get(productos.get(i)));
            }
        }

        return tabla;
    }

    private static Set<Producto> reconstruirCamino(int j, Double[][] tabla, List<Producto> productos) {
        Set<Producto> combinacion = new HashSet<>();

        for (int i = tabla.length - 1; i >= 0; i--) {
            //if (Double.compare(tabla[i][j], 0) == 0) break;
            if (i == 0) break;
            else if (Double.compare(tabla[i][j], tabla[i - 1][j]) != 0) {
                combinacion.add(productos.get(i));
                j--;
            }
        }

        return combinacion;
    }

    private static List<Recomendacion> transform(Set<Producto> combinacion, Map<Producto, Double> distancias) {
        return combinacion.stream()
                .map(p -> Recomendacion.builder()
                        .producto(p)
                        .interes(distancias.get(p))
                        .build())
                .collect(Collectors.toList());
    }
}