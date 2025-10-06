package ar.edu.uade.compralo.compralo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ar.edu.uade.compralo.compralo.model.entity.Distancia;
import ar.edu.uade.compralo.compralo.model.entity.Producto;

public class MapaUtils {

    /**
     *
     * @param mapa
     * @return
     */
    public static List<Distancia> ordenarMapa(Map<Producto, Double> mapa) {
        List<Distancia> distancias = new ArrayList<>();

        for (Producto p : mapa.keySet()) {
            distancias.add(Distancia.builder().producto(p).distancia(mapa.get(p)).build());
        }

        return ordenarMapaImpl(distancias, 0, mapa.size());
    }

    private static List<Distancia> ordenarMapaImpl(List<Distancia> vector, int inicio, int fin) {
        int longitud = fin - inicio + 1;

        if (longitud == 1) {
            return vector;
        }

        int mitad = (inicio + fin) / 2;

        List<Distancia> izquierda = ordenarMapaImpl(vector, inicio, mitad);
        List<Distancia> derecha = ordenarMapaImpl(vector, mitad + 1, fin);

        return fusionarMitades(izquierda, derecha);
    }

    private static List<Distancia> fusionarMitades(List<Distancia> izq, List<Distancia> der) {
        int i = 0;
        int j = 0;
        List<Distancia> resultado = new ArrayList<>();

        while (i < izq.size() && j < der.size()) {
            if (Double.compare(izq.get(i).getDistancia(), der.get(j).getDistancia()) <= 0) {
                resultado.add(izq.get(i++));
            } else {
                resultado.add(der.get(j++));
            }
        }

        if (i != izq.size()) {
            resultado.addAll(izq.subList(i, izq.size()));
        } else {
            resultado.addAll(der.subList(j, der.size()));
        }

        return resultado;
    }
    
}
