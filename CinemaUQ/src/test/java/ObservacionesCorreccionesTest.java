import composite.Carrito;
import fachada.CompraFachada;
import model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ObservacionesCorreccionesTest {
    @Test
    void compraDebeActualizarMovimientosPuntosHistorialYAsiento() {
        Cuenta cuenta = new Cuenta("TEST-1", "cliente-test", "123456", null, null);
        TarjetaVirtual tarjeta = new TarjetaVirtual("TV-TEST", 50000, cuenta);
        cuenta.setTheTarjetaVirtualCliente(tarjeta);

        SalaCine sala = new SalaCine("TEST-SALA");
        Asiento asiento = new Asiento("A1", sala);
        sala.agregarAsiento(asiento);
        Pelicula pelicula = new Pelicula("TEST-PEL", "Película de prueba", "Director");
        FuncionPelicula funcion = new FuncionPelicula("TEST-FUN", LocalDate.now().plusDays(2), LocalTime.of(20, 0), sala, pelicula);

        Carrito carrito = new Carrito("TEST-CARRITO");
        carrito.agregar(new Entrada("TEST-ENT", 12000, asiento, funcion));
        int comprasPrevias = Cine.getInstancia().getListCompras().size();

        new CompraFachada(cuenta, "123456", carrito, 12000, funcion).hacerCompra();

        assertEquals(38000, tarjeta.getSaldo());
        assertEquals(1, tarjeta.getMovimientos().size());
        assertEquals(-12000, tarjeta.getMovimientos().get(0).getMonto());
        assertEquals(12, CuentaPuntos.puntosDisponibles(cuenta, LocalDate.now()));
        assertEquals(comprasPrevias + 1, Cine.getInstancia().getListCompras().size());
        assertTrue(funcion.isAsientoOcupado(asiento));

        Compra compra = Cine.getInstancia().getListCompras().get(Cine.getInstancia().getListCompras().size() - 1);
        assertSame(cuenta, compra.getCuenta());
        compra.cancelar(LocalDateTime.now());

        assertEquals(50000, tarjeta.getSaldo());
        assertEquals(2, tarjeta.getMovimientos().size());
        assertEquals(0, CuentaPuntos.puntosDisponibles(cuenta, LocalDate.now()));
        assertTrue(compra.isCancelada());
        assertFalse(funcion.isAsientoOcupado(asiento));
    }
}
