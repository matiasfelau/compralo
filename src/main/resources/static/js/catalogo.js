let carrito = [];
const STORAGE_KEY = 'compraloCarrito';

document.addEventListener("DOMContentLoaded", async () => {
    const contenedorProductos = document.getElementById("productos");

    const carritoGuardado = localStorage.getItem(STORAGE_KEY);
    if (carritoGuardado) {
        carrito = JSON.parse(carritoGuardado);
        console.log("Carrito cargado desde localStorage:", carrito);
    }

    const cargarProductos = async () => {
        try {
            const respuesta = await fetch("/api/productos");
            if (!respuesta.ok) throw new Error("Error al obtener los productos");

            const productos = await respuesta.json();
            contenedorProductos.innerHTML = "";

            productos.forEach(p => {
                const card = document.createElement("div");
                card.classList.add("producto-card");

                // IMPORTANTE: Se añade data-id para poder identificar el producto al agregarlo
                card.setAttribute('data-product-id', p.id);
                card.setAttribute('data-product-nombre', p.nombre);
                card.setAttribute('data-product-precio', p.precio.toFixed(2));


                card.innerHTML = `
                    <img class="producto-img" src="/images/default-product.png" alt="${p.nombre}">
                    <h3 class="producto-nombre">${p.nombre}</h3>
                    <p class="producto-precio">$${p.precio.toFixed(2)}</p>
                    <button class="btn-agregar" data-id="${p.id}">Agregar al carrito</button>
                `;

                contenedorProductos.appendChild(card);
            });
        } catch (error) {
            console.error(error);
            contenedorProductos.innerHTML = "<p>Error al cargar los productos 😢</p>";
        }
    };

    const agregarAlCarrito = (productoId, nombre, precio) => {
        const productoExistente = carrito.find(item => item.id === productoId);

        if (productoExistente) {
            productoExistente.cantidad += 1;
        } else {
            carrito.push({
                id: productoId,
                nombre: nombre,
                precio: parseFloat(precio),
                cantidad: 1
            });
        }

        localStorage.setItem(STORAGE_KEY, JSON.stringify(carrito));

        console.log("Carrito guardado en localStorage:", carrito);

        mostrarPopupConfirmacion(nombre);
    };


    contenedorProductos.addEventListener('click', (e) => {
        const target = e.target;

        if (target && target.classList.contains('btn-agregar')) {
            const card = target.closest('.producto-card');

            const id = card.getAttribute('data-product-id');
            const nombre = card.getAttribute('data-product-nombre');
            const precio = card.getAttribute('data-product-precio');

            if (id && nombre && precio) {
                agregarAlCarrito(id, nombre, precio);
            }
        }
    });


    const mostrarPopupConfirmacion = (nombreProducto) => {
        const popup = document.createElement('div');
        popup.id = 'popup-confirmacion';
        popup.innerHTML = `
            <div class="popup-content">
                <p>✅ <strong>${nombreProducto}</strong> agregado al carrito.</p>
            </div>
        `;

        document.body.appendChild(popup);

        setTimeout(() => {
            if (document.getElementById('popup-confirmacion')) {
                document.body.removeChild(popup);
            }
        }, 2000);
    };

    cargarProductos();
});