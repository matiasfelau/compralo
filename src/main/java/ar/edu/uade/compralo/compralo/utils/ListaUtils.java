package ar.edu.uade.compralo.compralo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ar.edu.uade.compralo.compralo.model.entity.Distancia;
import ar.edu.uade.compralo.compralo.model.entity.Producto;

public class ListaUtils {

    /**
     *
     * @param mapa
     * @return
     */
    public static List<Producto> ordenarLista(List<Producto> mapa) {
        return ordenarListaImpl(mapa, 0, mapa.size());
    }

    private static List<Producto> ordenarListaImpl(List<Producto> vector, int inicio, int fin) {
        int longitud = fin - inicio + 1;

        if (longitud == 1) {
            return vector;
        }

        int mitad = (inicio + fin) / 2;

        List<Producto> izquierda = ordenarListaImpl(vector, inicio, mitad);
        List<Producto> derecha = ordenarListaImpl(vector, mitad + 1, fin);

        return fusionarMitades(izquierda, derecha);
    }

    private static List<Producto> fusionarMitades(List<Producto> izq, List<Producto> der) {
        int i = 0;
        int j = 0;
        List<Producto> resultado = new ArrayList<>();

        while (i < izq.size() && j < der.size()) {
            if (Double.compare(izq.get(i).getPrecio(), der.get(j).getPrecio()) <= 0) {
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
