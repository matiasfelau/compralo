package ar.edu.uade.compralo.compralo;

import ar.edu.uade.compralo.compralo.model.entity.Producto;
import ar.edu.uade.compralo.compralo.model.entity.Recomendacion;
import ar.edu.uade.compralo.compralo.service.CarritoService;
import ar.edu.uade.compralo.compralo.service.ProductoService;
import ar.edu.uade.compralo.compralo.utils.DPUtils;
import ar.edu.uade.compralo.compralo.utils.DijkstraUtils;
import ar.edu.uade.compralo.compralo.utils.DYCUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CompraloApplicationTests {
	@Autowired
	ProductoService productoService;

	private List<Producto> arr = new ArrayList<>();

    @Autowired
    CarritoService carritoService;

	@BeforeEach
	void load() {
		arr.add(Producto.builder().id(0L).nombre("Mouse").precio(300.0).build());
		arr.add(Producto.builder().id(1L).nombre("Teclado").precio(200.0).build());
		arr.add(Producto.builder().id(2L).nombre("Auriculares").precio(500.0).build());
		arr.add(Producto.builder().id(3L).nombre("Monitor").precio(400.0).build());
	}

	@Test
	void contextLoads() {

	}

	@Test
	void abmProducto() {
		for (Producto o : arr) {
			Producto p = productoService.encontrarProducto(o.getId());
			if (p == null) {
				productoService.agregarProducto(o.getNombre(), o.getPrecio());
			}
		}

		List<Producto> obtenido = productoService.encontrarTodosProductos();
		List<Producto> esperado = arr;

		System.out.println(obtenido);

		for (int i = 0; i < obtenido.size(); i++) {
			System.out.println(obtenido.get(i).getNombre());
			assert (Objects.equals(obtenido.get(i).getNombre(), esperado.get(i).getNombre()));
			assert (Double.compare(obtenido.get(i).getPrecio(), esperado.get(i).getPrecio()) == 0);
		}
	}

	// VISUALIZAR EN NEO4J DESKTOP
	@Test
	void relacionarProductos() {
		List<Producto> input;
		int i = 0;

		do {
			input = productoService.encontrarTodosProductos();
			productoService.relacionarProductos(input.get(i), input.get(i+1));
			i++;
		} while (i < 3);

		input = productoService.encontrarTodosProductos();
		productoService.relacionarProductos(input.get(0), input.get(1));
	}

	@Test
	void encontrarRelacionados() {
		Producto p = productoService.encontrarProducto(0L);
		Set<Producto> obtenido = productoService.encontrarProductosRelacionados(p, 2);

        Set<Producto> esperado = new HashSet<>(arr);
		esperado.remove(arr.get(arr.size()-1)); // NO DEBERIA ENCONTRARSE 'D'

		assertEquals(obtenido, esperado);
	}

	//DIJKSTRA
	@Test
	void calcularDistancias() {
		Producto p = productoService.encontrarProducto(0L);
		Set<Producto> productos = productoService.encontrarProductosRelacionados(p, 2);

		Map<Producto, Double> obtenido = DijkstraUtils.calcularDistancias(productos, p);

		Map<Producto, Double> esperado = new HashMap<>();
		esperado.put(arr.get(0), 0.0);
		esperado.put(arr.get(1), 0.5);
		esperado.put(arr.get(2), 1.5);

		assertEquals(obtenido, esperado);
	}

	//PROGRAMACION DINAMICA
	@Test
	void maximizarRecomendaciones() {
//		productoService.agregarProducto("Mouse Pad", 600.0);
		Producto producto = productoService.encontrarProducto(0L);
//		Producto nuevo = productoService.encontrarProducto(4L);
//		productoService.relacionarProductos(producto, nuevo);

		Set<Producto> relacionados = productoService.encontrarProductosRelacionados(producto, 2);
		Map<Producto, Double> distancias = DijkstraUtils.calcularDistancias(relacionados, producto);
		List<Recomendacion> obtenido = DPUtils.facade(distancias, 3);

		List<Recomendacion> esperado = new ArrayList<>();
		esperado.add(Recomendacion.builder()
				.producto(productoService.encontrarProducto(2L))
				.interes(0.75)
				.build());
		esperado.add(Recomendacion.builder()
				.producto(productoService.encontrarProducto(1L))
				.interes(0.25)
				.build());

		for (Recomendacion r : obtenido) {
			System.out.println(r.getProducto().getNombre());
		}

//		assertEquals(esperado, obtenido);
	}

	//DIVIDE Y CONQUISTA
	@Test
	void ordenarProductos() {
		List<Producto> productos = productoService.encontrarTodosProductos();

		List<Producto> obtenido = DYCUtils.ordenarLista(productos);

		List<Producto> esperado = new ArrayList<>();
		esperado.add(productoService.encontrarProducto(1L));
		esperado.add(productoService.encontrarProducto(0L));
		esperado.add(productoService.encontrarProducto(3L));
		esperado.add(productoService.encontrarProducto(2L));

		assertEquals(esperado, obtenido);
	}

	//GREEDY
	@Test
	void llenarCarrito() {
		Set<Producto> productos = new HashSet<>(Set.of(productoService.encontrarProducto(0L)));
		Set<Producto> obtenido = carritoService.obtenerEnvioGratis(productos);
		Set<Producto> esperado = new HashSet<>();
		esperado.add(productoService.encontrarProducto(0L));
		esperado.add(productoService.encontrarProducto(1L));
		assertEquals(esperado, obtenido);
	}

	//BACKTRACKING + BRANCH & BOUND
	@Test
	void obtenerRecomendaciones() {
		Set<Producto> obtenido = carritoService.obtenerRecomendaciones(Set.of(productoService.encontrarProducto(0L)));

		for (Producto p : obtenido) {
			System.out.println(p.getNombre());
		}
	}
}
