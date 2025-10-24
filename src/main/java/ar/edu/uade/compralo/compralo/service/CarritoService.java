package ar.edu.uade.compralo.compralo.service;

import ar.edu.uade.compralo.compralo.model.entity.Producto;
import ar.edu.uade.compralo.compralo.utils.DYCUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


@Service
@AllArgsConstructor
public class CarritoService {
    private final RecomendacionService recomendacionService;
    private final ProductoService productoService;

    private static final int MONTO_ENVIO_GRATIS = 400;

    public Set<Producto> obtenerRecomendaciones(Set<Producto> carrito) {
        return recomendacionService.obtenerRecomendacionesCarrito(carrito);
    }

    /**
     * GREEDY
     * @param productos
     * @return
     */
    public Set<Producto> obtenerEnvioGratis(Set<Producto> productos) {
        int suma = 0;
        Set<Producto> carrito = new HashSet<>(productos);
        for (Producto p: productos) {
            suma += p.getPrecio();
        }

        List<Producto> todosLosProductos = productoService.encontrarTodosProductos();
        todosLosProductos = DYCUtils.ordenarLista(todosLosProductos);

        Iterator<Producto> it = todosLosProductos.iterator();
        Producto p = it.next();
        while (suma < MONTO_ENVIO_GRATIS) {
          if (!carrito.contains(p)){
                carrito.add(p);
                suma += p.getPrecio();
            }
            p = it.next();
        }
        return carrito;
    }
}
