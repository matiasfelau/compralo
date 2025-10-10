package ar.edu.uade.compralo.compralo.service;

import ar.edu.uade.compralo.compralo.model.entity.Producto;
import ar.edu.uade.compralo.compralo.model.entity.Relacion;
import ar.edu.uade.compralo.compralo.repository.ProductoRepository;
import ar.edu.uade.compralo.compralo.utils.PesoUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProductoService {
    private static final Double PESO_INICIAL = 1.0;
    private final ProductoRepository repo;
    private final Set<Producto> descartados = new HashSet<>();

    public void relacionarProductos(Producto productoA, Producto productoB) {
        Relacion relacionExistente = null;

        for (Relacion relacion : productoA.getRelacionados()) {
            if (relacion.getProducto().equals(productoB)) {
                relacionExistente = relacion;
                break;
            }
        }

        if (relacionExistente != null) {
            relacionExistente.setPeso(PesoUtils.calcularPeso(relacionExistente.getPeso()));

            for (Relacion relacion : productoB.getRelacionados()) {
                if (relacion.getProducto().equals(productoA)) {
                    relacion.setPeso(PesoUtils.calcularPeso(relacion.getPeso()));
                    break;
                }
            }
        } else {
            productoA.getRelacionados().add(Relacion.builder().producto(productoB).peso(PESO_INICIAL).build());
            productoB.getRelacionados().add(Relacion.builder().producto(productoA).peso(PESO_INICIAL).build());
        }

        repo.save(productoA);
        repo.save(productoB);
    }

    /**
     * Este método utiliza BFS para encontrar, en cascada, los productos relacionados a un producto raiz.
     * <br>
     * Se aconseja usar una profundidad igual a 2.
     *
     * @param raiz el nodo desde el cual se buscarán productos relacionados.
     * @param profundidad la distancia máxima respecto al nodo ingresado como raiz.
     * @return el conjunto de productos relacionados.
     */
    public Set<Producto> encontrarProductosRelacionados(Producto raiz, int profundidad) {
        PriorityQueue<Producto> pendiente = new PriorityQueue<>();
        Set<Producto> visitados = new HashSet<>();
        int nivel = 0;

        pendiente.add(raiz);

        while (!pendiente.isEmpty() && nivel <= profundidad) {
            int n = pendiente.size();

            for (int i=0; i < n; i++) {
                Producto producto = pendiente.poll();

                if (producto != null) {
                    // Poda: si el nodo actual está descartado, no lo expando
                    if (descartados.contains(producto)) {
                        continue;
                    }


                    if (!visitados.contains(producto)) {
                        visitados.add(producto);
                        
                        for (Relacion relacion : producto.getRelacionados()) {
                            repo.findById(relacion.getProducto().getId()).ifPresent(pendiente::add);
                        }
                    }
                }
            }

            nivel++;
        }

        return visitados;
    }

    public void descartarProducto(Producto producto) {
        descartados.add(producto);
    }

    public List<Producto> traerTodos(){
        return repo.findAll();
    }
}
