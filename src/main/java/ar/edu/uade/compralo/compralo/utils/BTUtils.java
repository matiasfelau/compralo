package ar.edu.uade.compralo.compralo.utils;

import ar.edu.uade.compralo.compralo.model.entity.Producto;
import ar.edu.uade.compralo.compralo.model.entity.Recomendacion;

import java.util.*;
import java.util.stream.Collectors;

public class BTUtils {
    private static Double menor;
    private static List<Recomendacion> mejor;

    public static Set<Producto> facade(List<Recomendacion> recomendaciones, int maximo) {
        init();

        if (recomendaciones.size() <= maximo) mejor = recomendaciones;
        else backtrack(recomendaciones, 0, 0.0, new ArrayList<>(), maximo);

        return transform(mejor);
    }

    private static void init() {
        menor = Double.POSITIVE_INFINITY;
        mejor = new ArrayList<>();
    }

    private static void backtrack(
            List<Recomendacion> recomendaciones,
            int posicion,
            Double suma,
            List<Recomendacion> actual,
            int maximo
    ) {
        if (posicion == recomendaciones.size()) {
            if (suma < menor && actual.size() == maximo) {
                menor = suma;
                mejor = actual;
            }
            return;
        }

        int longitud = actual.size();

        //PODA: La suma de intereses restantes no satisface al problema.
        if (suma + sumarRestantes(recomendaciones, posicion, maximo - longitud) >= menor) return;

        Recomendacion recomendacion = recomendaciones.get(posicion++);
        Double interes = recomendacion.getInteres();

        boolean agregar = false;
        if (longitud < maximo) {
            agregar = true;
        } else {
            Recomendacion mayor = Collections.max(actual, Comparator.comparing(Recomendacion::getInteres));
            if (mayor.getInteres() > interes) {
                actual.remove(mayor);
                agregar = true;
            }
        }

        if (agregar) {
            suma += interes;
            actual.add(recomendacion);
            backtrack(recomendaciones, posicion, suma, actual, maximo);

            suma -= interes;
            actual.remove(actual.size() - 1);
            backtrack(recomendaciones, posicion, suma, actual, maximo);
        } else backtrack(recomendaciones, posicion, suma, actual, maximo);
    }

    private static Double sumarRestantes(List<Recomendacion> recomendaciones, int inicio, int faltan) {
        return recomendaciones.subList(inicio, recomendaciones.size()).stream()
                .map(Recomendacion::getInteres)
                .sorted()
                .limit(faltan)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private static Set<Producto> transform(List<Recomendacion> recomendaciones) {
        return recomendaciones.stream()
                .map(Recomendacion::getProducto)
                .collect(Collectors.toSet());
    }
}
