package ar.edu.uade.compralo.compralo.utils;

import java.util.ArrayList;
import java.util.List;

import ar.edu.uade.compralo.compralo.model.entity.Producto;

public class DYCUtils {

    /**
     *
     * @param lista
     * @return
     */
    public static List<Producto> ordenarLista(List<Producto> lista) {
        return ordenarListaImpl(lista, 0, lista.size());
    }

    private static List<Producto> ordenarListaImpl(List<Producto> vector, int inicio, int fin) {
        // 1,2,3 - i:0 f:3 m:1

        // 1 - i:0 f:1 ---
        // 2,3 - i:1 f:3 m:2

        // 2 - i:1 f:2 ---
        // 3 - i:2 f:3 ---
        if (inicio == fin - 1) {
            return new ArrayList<>(vector.subList(inicio, fin));
        }

        int mitad = (inicio + fin) / 2;

        List<Producto> izquierda = ordenarListaImpl(vector, inicio, mitad);
        List<Producto> derecha = ordenarListaImpl(vector, mitad, fin);

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
