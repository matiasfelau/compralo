document.addEventListener("DOMContentLoaded", () => {
    const listaCarrito = document.getElementById("lista-carrito");
    const resumenCarrito = document.getElementById("resumen-carrito");
    const listaRecomendaciones = document.getElementById("lista-recomendaciones"); // Nuevo
    const btnComprar = document.getElementById("btn-comprar");
    const STORAGE_KEY = 'compraloCarrito';
    let total = 0;

    const cargarCarrito = () => {
        const carritoGuardado = localStorage.getItem(STORAGE_KEY);
        const carrito = carritoGuardado ? JSON.parse(carritoGuardado) : [];

        listaCarrito.innerHTML = '';
        total = 0;

        if (carrito.length === 0) {
            listaCarrito.innerHTML = '<p>Tu carrito está vacío. ¡Añade algunos productos!</p>';
            resumenCarrito.innerHTML = '';
            btnComprar.style.display = 'none';
            listaRecomendaciones.innerHTML = '<p>Añade productos para ver recomendaciones.</p>';
            return;
        }

        carrito.forEach(item => {
            const subtotal = item.precio * item.cantidad;
            total += subtotal;

            const itemDiv = document.createElement('div');
            itemDiv.classList.add('carrito-item');

            itemDiv.innerHTML = `
                <div class="item-info">
                    <h4 class="item-nombre">${item.nombre}</h4>
                    <p class="item-precio">$${item.precio.toFixed(2)} x ${item.cantidad}</p>
                </div>
                <p class="item-subtotal">Subtotal: <strong>$${subtotal.toFixed(2)}</strong></p>
            `;
            listaCarrito.appendChild(itemDiv);
        });

        resumenCarrito.innerHTML = `
            <hr>
            <h3>Total del Carrito: $${total.toFixed(2)}</h3>
        `;
        btnComprar.style.display = 'block';

        obtenerRecomendaciones(carrito);
    };

    const obtenerRecomendaciones = async (carrito) => {
        const carritoDtos = carrito.map(item => ({
            id: Number(item.id),
            nombre: item.nombre,
            precio: Number(item.precio)
        }));

        try {
            const respuesta = await fetch("/api/carrito/recomendaciones", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(carritoDtos)
            });

            if (!respuesta.ok) {
                const errorText = await respuesta.text();
                console.error("Respuesta detallada del servidor:", respuesta.status, errorText);
                throw new Error("Error al obtener recomendaciones");
            }

            const recomendaciones = await respuesta.json();

            renderizarRecomendaciones(recomendaciones);

        } catch (error) {
            console.error("Error al cargar recomendaciones:", error);
            listaRecomendaciones.innerHTML = '<p>No pudimos cargar las recomendaciones.</p>';
        }
    };

    const renderizarRecomendaciones = (recomendaciones) => {
        listaRecomendaciones.innerHTML = ''; // Limpiar

        if (recomendaciones.length === 0) {
            listaRecomendaciones.innerHTML = '<p>No encontramos productos para recomendarte.</p>';
            return;
        }

        recomendaciones.forEach(p => {
            const card = document.createElement("div");
            card.classList.add("producto-card", "recomendacion-card");

            card.innerHTML = `
                <img class="producto-img" src="/images/default-product.png" alt="${p.nombre}">
                <h4 class="producto-nombre">${p.nombre}</h4>
                <p class="producto-precio">$${p.precio.toFixed(2)}</p>
                `;

            listaRecomendaciones.appendChild(card);
        });
    };

    btnComprar.addEventListener('click', () => {
        alert(`Simulando compra por un total de $${total.toFixed(2)}. ¡Gracias por tu pedido!`);
        localStorage.removeItem(STORAGE_KEY);
        cargarCarrito();
    });

    cargarCarrito();
});

document.addEventListener("DOMContentLoaded", () => {
    const btnComprar = document.getElementById("btn-comprar");

    // NUEVAS DECLARACIONES:
    const btnEnvioGratis = document.getElementById("btn-envio-gratis");
    const resultadoEnvioGratis = document.getElementById("resultado-envio-gratis");

    const STORAGE_KEY = 'compraloCarrito';
    let total = 0;

    const obtenerEnvioGratis = async (carrito) => {
        resultadoEnvioGratis.style.display = 'block';
        resultadoEnvioGratis.innerHTML = '<p>Buscando la mejor selección...</p>';
        resultadoEnvioGratis.classList.remove('mostrar-productos');

        const carritoDtos = carrito.map(item => ({
            id: Number(item.id),
            nombre: item.nombre,
            precio: Number(item.precio)
        }));

        try {
            const respuesta = await fetch("/api/carrito/envio-gratis", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(carritoDtos) // Enviamos la lista de DTOs
            });

            if (!respuesta.ok) throw new Error("Error al obtener la selección de envío gratis");

            const seleccion = await respuesta.json();

            renderizarEnvioGratis(seleccion);

        } catch (error) {
            console.error("Error al cargar envío gratis:", error);
            resultadoEnvioGratis.innerHTML = '<p>😢 No pudimos procesar la búsqueda de envío gratis.</p>';
        }
    };

    const renderizarEnvioGratis = (seleccion) => {
        if (seleccion.length === 0) {
            // ... (Tu lógica de éxito) ...
            return;
        }

        seleccion.sort((a, b) => a.precio - b.precio);

        resultadoEnvioGratis.classList.add('mostrar-productos');

        let html = '<p>Para calificar para el Envío Gratis, te sugerimos añadir los siguientes productos:</p>';

        const listaHtml = seleccion.map(p => `
        <li>
            <strong>${p.nombre}</strong> - $${p.precio.toFixed(2)}
        </li>
    `).join('');

        html += `<ul class="envio-gratis-list">${listaHtml}</ul>`;

        resultadoEnvioGratis.innerHTML = html;
    };

    btnEnvioGratis.addEventListener('click', () => {
        const carritoGuardado = localStorage.getItem(STORAGE_KEY);
        const carrito = carritoGuardado ? JSON.parse(carritoGuardado) : [];
        obtenerEnvioGratis(carrito);
    });

    cargarCarrito();
});