package ar.edu.uade.compralo.compralo.config;

import ar.edu.uade.compralo.compralo.model.entity.Producto;
import ar.edu.uade.compralo.compralo.service.ProductoService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Component
@Profile({"dev", "test", "preprod"})
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final ProductoService productoService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!productoService.encontrarTodosProductos().isEmpty()) {
            return;
        }

        Object[][] productosData = new Object[][]{
                {"Mouse", 300.0},
                {"Teclado", 200.0},
                {"Auriculares", 500.0},
                {"Monitor", 400.0},
                {"Mouse Pad", 150.0},
                {"Webcam", 450.0},
                {"Gabinete", 1200.0},
                {"Fuente de poder", 800.0},
                {"Placa madre", 5000.0},
                {"Disco SSD", 3500.0},
                {"Memoria RAM", 2500.0},
                {"Parlantes", 220.0}
        };

        for (Object[] p : productosData) {
            String nombre = (String) p[0];
            Double precio = (Double) p[1];
            productoService.agregarProducto(nombre, precio);
        }

        List<Producto> creados = productoService.encontrarTodosProductos();
        Map<String, Producto> productos = new HashMap<>();
        for (Producto p : creados) {
            productos.put(p.getNombre(), p);
        }

        productoService.relacionarProductos(productos.get("Mouse"), productos.get("Teclado"));
        productoService.relacionarProductos(productos.get("Mouse"), productos.get("Teclado"));

        productoService.relacionarProductos(productos.get("Mouse"), productos.get("Mouse Pad"));

        productoService.relacionarProductos(productos.get("Mouse"), productos.get("Monitor"));
        productoService.relacionarProductos(productos.get("Mouse"), productos.get("Monitor"));
        productoService.relacionarProductos(productos.get("Mouse"), productos.get("Monitor"));

        productoService.relacionarProductos(productos.get("Teclado"), productos.get("Monitor"));

        productoService.relacionarProductos(productos.get("Auriculares"), productos.get("Parlantes"));
        productoService.relacionarProductos(productos.get("Auriculares"), productos.get("Parlantes"));

        productoService.relacionarProductos(productos.get("Webcam"), productos.get("Auriculares"));

        productoService.relacionarProductos(productos.get("Webcam"), productos.get("Monitor"));
        productoService.relacionarProductos(productos.get("Webcam"), productos.get("Monitor"));

        productoService.relacionarProductos(productos.get("Parlantes"), productos.get("Monitor"));

        productoService.relacionarProductos(productos.get("Mouse"), productos.get("Webcam"));
        productoService.relacionarProductos(productos.get("Mouse"), productos.get("Webcam"));

        productoService.relacionarProductos(productos.get("Placa madre"), productos.get("Memoria RAM"));

        productoService.relacionarProductos(productos.get("Placa madre"), productos.get("Disco SSD"));
        productoService.relacionarProductos(productos.get("Placa madre"), productos.get("Disco SSD"));

        productoService.relacionarProductos(productos.get("Placa madre"), productos.get("Fuente de poder"));

        productoService.relacionarProductos(productos.get("Gabinete"), productos.get("Fuente de poder"));
        productoService.relacionarProductos(productos.get("Gabinete"), productos.get("Fuente de poder"));

        productoService.relacionarProductos(productos.get("Gabinete"), productos.get("Placa madre"));

        productoService.relacionarProductos(productos.get("Gabinete"), productos.get("Disco SSD"));
        productoService.relacionarProductos(productos.get("Gabinete"), productos.get("Disco SSD"));

        productoService.relacionarProductos(productos.get("Disco SSD"), productos.get("Memoria RAM"));
    }
}
