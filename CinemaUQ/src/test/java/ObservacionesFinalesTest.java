import composite.Carrito;
import fachada.CompraFachada;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ObservacionesFinalesTest {
    @BeforeEach
    void limpiarCompras() {
        Cine.getInstancia().getListCompras().clear();
    }

    private Object[] prepararCompra(LocalDate fechaFuncion, int puntos) {
        Cuenta cuenta = new Cuenta("ID-TEST", "cliente-final", "123456", null, null);
        TarjetaVirtual tarjeta = new TarjetaVirtual("TV-FINAL", 50000, cuenta);
        cuenta.setTheTarjetaVirtualCliente(tarjeta);
        CuentaPuntos.acumular(cuenta, puntos, LocalDate.now(), "PUNTOS-INICIALES");
        SalaCine sala = new SalaCine("SALA-FINAL");
        Asiento asiento = new Asiento("A1", sala);
        sala.agregarAsiento(asiento);
        Pelicula pelicula = new Pelicula("PEL-001", "Prueba", "Director");
        FuncionPelicula funcion = new FuncionPelicula("FUN-001", fechaFuncion, LocalTime.of(20, 0), sala, pelicula);
        Carrito carrito = new Carrito("CARRITO-FINAL");
        carrito.agregar(new Entrada("ENT-A1", 12000, asiento, funcion));
        new CompraFachada(cuenta, "123456", carrito, 12000, funcion, puntos).hacerCompra();
        Compra compra = Cine.getInstancia().getListCompras().get(0);
        return new Object[]{cuenta, tarjeta, asiento, funcion, compra};
    }

    @Test
    void redimirPuntosDescuentaDelTotalYDeLaCuenta() {
        Object[] datos = prepararCompra(LocalDate.now().plusDays(3), 200);
        Cuenta cuenta = (Cuenta) datos[0];
        TarjetaVirtual tarjeta = (TarjetaVirtual) datos[1];
        Compra compra = (Compra) datos[4];
        assertEquals(40000, tarjeta.getSaldo());
        assertEquals(10, CuentaPuntos.puntosDisponibles(cuenta, LocalDate.now()));
        assertEquals(10000, compra.getValorPagado());
        assertEquals(2000, compra.getDescuentoPuntos());
    }

    @Test
    void permiteUsarTodosLosPuntosAunqueSuValorSupereElTotal() {
        Object[] datos = prepararCompra(LocalDate.now().plusDays(3), 2000);
        Cuenta cuenta = (Cuenta) datos[0];
        TarjetaVirtual tarjeta = (TarjetaVirtual) datos[1];
        Compra compra = (Compra) datos[4];

        assertEquals(0, CuentaPuntos.puntosDisponibles(cuenta, LocalDate.now()));
        assertEquals(2000, compra.getPuntosRedimidos());
        assertEquals(12000, compra.getDescuentoPuntos());
        assertEquals(0, compra.getValorPagado());
        assertEquals(50000, tarjeta.getSaldo());
    }

    @Test
    void noPermiteRedimirPuntosEnUnaCompraSinEntradas() {
        Cuenta cuenta = new Cuenta("ID-SNACK", "cliente-snack", "123456", null, null);
        cuenta.setTheTarjetaVirtualCliente(new TarjetaVirtual("TV-SNACK", 50000, cuenta));
        CuentaPuntos.acumular(cuenta, 100, LocalDate.now(), "PUNTOS-SNACK");
        Carrito carrito = new Carrito("CARRITO-SNACK");
        carrito.agregar(new Palomita("Palomitas", 8000));

        assertThrows(IllegalStateException.class,
                () -> new CompraFachada(cuenta, "123456", carrito, 8000, null, 100).hacerCompra());
    }

    @Test
    void cancelarFuncionCompletaReembolsoDeCompraPreviamenteCanceladaParcialmente() {
        LocalDateTime inicio = LocalDateTime.now().plusHours(12);
        Object[] datos = prepararCompra(inicio.toLocalDate(), 0);
        // Ajustar el horario de inicio a 12 horas desde este momento para activar la política del 80%.
        FuncionPelicula funcion = (FuncionPelicula) datos[3];
        funcion.setFecha(inicio.toLocalDate());
        funcion.setHora(inicio.toLocalTime());
        Compra compra = (Compra) datos[4];
        TarjetaVirtual tarjeta = (TarjetaVirtual) datos[1];
        double primerReembolso = compra.cancelar(LocalDateTime.now());
        assertEquals(9600, primerReembolso, 0.01);
        double complemento = compra.cancelarPorCancelacionFuncion(LocalDateTime.now());
        assertEquals(2400, complemento, 0.01);
        assertEquals(50000, tarjeta.getSaldo(), 0.01);
        assertEquals(100, compra.getPorcentajeReembolso());
        assertEquals(12000, compra.getMontoReembolsado(), 0.01);
    }
}
