package ar.edu.uade.compralo.compralo.service;

import ar.edu.uade.compralo.compralo.model.entity.Producto;
import ar.edu.uade.compralo.compralo.utils.ListaUtils;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CarritoService {
    private final RecomendacionService recomendacionService;
    private final ProductoService productoService;
    private final int MONTOENVIOGRATIS = 33000;
    /**
     * Agrega un producto a la lista de descarte.
     */
    public void descartar(Producto producto) {
        if (producto != null) {
            recomendacionService.descartarRecomendacion(producto);
        }
    }

    public Set<Producto> obtenerRecomendaciones(Set<Producto> productos, int nivel){
        nivel = nivel/productos.size();
        for (Producto p: productos){
            recomendacionService.obtenerRecomendaciones(p,nivel);
        }
        return productos;

    }

    public Set<Producto> llenarCarrito (Set<Producto> productos){
        int suma = 0;
        Set<Producto> carrito = new HashSet<>();
        carrito.addAll(productos);
        for (Producto p: productos){
            suma += p.getPrecio();
            
        }
        int restante = MONTOENVIOGRATIS - suma;
        List<Producto> todosLosProductos = productoService.traerTodos();
        todosLosProductos = ListaUtils.ordenarLista(todosLosProductos);
        Iterator<Producto> it = todosLosProductos.iterator();
        Producto p = it.next();
        while (restante < MONTOENVIOGRATIS){
          if (!carrito.contains(p)){
                carrito.add(p);
                restante += p.getPrecio();
            }
            p = it.next();
        }
        return carrito;
    }

}
