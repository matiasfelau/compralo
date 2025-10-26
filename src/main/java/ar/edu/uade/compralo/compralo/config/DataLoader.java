package ar.edu.uade.compralo.compralo.config;

import ar.edu.uade.compralo.compralo.model.entity.Producto;
import ar.edu.uade.compralo.compralo.service.ProductoService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("!test") // no ejecutar durante tests
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final ProductoService productoService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // No cargar si ya hay productos
        if (!productoService.encontrarTodosProductos().isEmpty()) {
            return;
        }

        // Lista de productos hardcodeada (al menos 10)
        Object[][] productos = new Object[][]{
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

        // Agregar productos
        for (Object[] p : productos) {
            String nombre = (String) p[0];
            Double precio = (Double) p[1];
            productoService.agregarProducto(nombre, precio);
        }

        // Recuperar productos creados y establecer algunas relaciones de ejemplo
        List<Producto> creados = productoService.encontrarTodosProductos();
        // Crear una cadena de relaciones 0-1-2-3... y alguna cruzada para enriquecer el grafo
        for (int i = 0; i < creados.size() - 1; i++) {
            Producto a = creados.get(i);
            Producto b = creados.get(i + 1);
            productoService.relacionarProductos(a, b);
        }

        // Relaciones adicionales (ejemplos)
        if (creados.size() >= 5) {
            productoService.relacionarProductos(creados.get(0), creados.get(2));
            productoService.relacionarProductos(creados.get(1), creados.get(3));
            productoService.relacionarProductos(creados.get(4), creados.get(7));
        }
    }
}
