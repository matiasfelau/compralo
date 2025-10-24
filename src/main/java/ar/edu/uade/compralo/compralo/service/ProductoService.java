package ar.edu.uade.compralo.compralo.service;

import ar.edu.uade.compralo.compralo.model.entity.Producto;
import ar.edu.uade.compralo.compralo.model.entity.Relacion;
import ar.edu.uade.compralo.compralo.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;

    private static final Double PESO_INICIAL = 1.0;

    public void agregarProducto(String nombre, Double precio) {
        productoRepository.save(Producto.builder().nombre(nombre).precio(precio).build());
    }

    public Producto encontrarProducto(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public List<Producto> encontrarTodosProductos() {
        return productoRepository.findAll();
    }

    public void relacionarProductos(Producto productoA, Producto productoB) {
        Relacion relacionExistente = null;

        for (Relacion relacion : productoA.getRelacionados()) {
            if (relacion.getProducto().equals(productoB)) {
                relacionExistente = relacion;
                break;
            }
        }

        if (relacionExistente != null) {
            relacionExistente.setPeso(calcularPeso(relacionExistente.getPeso()));
            productoRepository.save(productoA);

            productoB = encontrarProducto(productoB.getId());
            for (Relacion relacion : productoB.getRelacionados()) {
                if (relacion.getProducto().equals(productoA)) {
                    relacion.setPeso(calcularPeso(relacion.getPeso()));
                    break;
                }
            }
            productoRepository.save(productoB);
        } else {
            productoA.getRelacionados().add(Relacion.builder().producto(productoB).peso(PESO_INICIAL).build());
            productoRepository.save(productoA);

            productoB = encontrarProducto(productoB.getId());
            productoB.getRelacionados().add(Relacion.builder().producto(productoA).peso(PESO_INICIAL).build());
            productoRepository.save(productoB);
        }
    }

    /**
     * BFS
     * @param raiz el nodo desde el cual se buscarán productos relacionados.
     * @param profundidad la distancia máxima respecto al nodo ingresado como raiz.
     * @return el conjunto de productos relacionados.
     */
    public Set<Producto> encontrarProductosRelacionados(Producto raiz, int profundidad) {
        Queue<Producto> pendiente = new ArrayDeque<>();
        Set<Producto> visitados = new HashSet<>();
        int nivel = 0;

        pendiente.add(raiz);

        while (!pendiente.isEmpty() && nivel <= profundidad) {
            int n = pendiente.size();
            for (int i=0; i < n; i++) {
                Producto producto = pendiente.poll();
                if (producto != null) {
                    if (!visitados.contains(producto)) {
                        visitados.add(producto);
                        
                        for (Relacion relacion : producto.getRelacionados()) {
                            productoRepository.findById(relacion.getProducto().getId()).ifPresent(pendiente::add);
                        }
                    }
                }
            }
            nivel++;
        }

        return visitados;
    }

    private Double calcularPeso(Double peso) {
        return 1 / (1 / peso + 1);
    }
}
