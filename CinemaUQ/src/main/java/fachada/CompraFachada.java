package fachada;

import composite.Carrito;
import model.Cine;
import model.Compra;
import model.Cuenta;
import model.FuncionPelicula;
import proxy.MedioPago;
import proxy.PagoTarjetaProxy;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;
import model.CuentaPuntos;

/** Coordina validación, pago y registro de la compra. */
public class CompraFachada implements Fachada {
    private final Cine cine = Cine.getInstancia();
    private final Cuenta cuenta;
    private final String contrasenia;
    private final Carrito carrito;
    private final double valorTotal;
    private final FuncionPelicula funcion;
    private final int puntosARedimir;

    public CompraFachada(Cuenta cuenta, String contrasenia, Carrito carrito, double valorTotal) {
        this(cuenta, contrasenia, carrito, valorTotal, null, 0);
    }
    public CompraFachada(Cuenta cuenta, String contrasenia, Carrito carrito, double valorTotal, FuncionPelicula funcion) {
        this(cuenta, contrasenia, carrito, valorTotal, funcion, 0);
    }
    public CompraFachada(Cuenta cuenta, String contrasenia, Carrito carrito, double valorTotal, FuncionPelicula funcion, int puntosARedimir) {
        this.cuenta = cuenta; this.contrasenia = contrasenia; this.carrito = carrito;
        this.valorTotal = valorTotal; this.funcion = funcion; this.puntosARedimir = puntosARedimir;
    }
    @Override public void hacerCompra() {
        validarDatos();
        MedioPago pago = new PagoTarjetaProxy(cuenta, contrasenia);
        Set<model.Asiento> asientosEnCompra = new HashSet<>();
        for (composite.Elemento elemento : carrito.getElementos()) {
            if (elemento instanceof model.Entrada entrada && funcion != null) {
                if (!asientosEnCompra.add(entrada.getElAsiento())) {
                    throw new IllegalStateException("El carrito contiene dos veces el asiento " + entrada.getElAsiento().getIdAsiento() + ".");
                }
                if (funcion.isAsientoOcupado(entrada.getElAsiento())) {
                    throw new IllegalStateException("El asiento " + entrada.getElAsiento().getIdAsiento() + " ya fue vendido.");
                }
            }
        }
        double descuento = puntosARedimir * CuentaPuntos.VALOR_PUNTO;
        if (puntosARedimir < 0 || puntosARedimir > CuentaPuntos.puntosDisponibles(cuenta, LocalDate.now()))
            throw new IllegalStateException("No tienes suficientes puntos vigentes.");
        if (descuento > valorTotal) throw new IllegalArgumentException("Los puntos no pueden cubrir un valor superior a la compra.");
        double valorTarjeta = valorTotal - descuento;
        // consultarSaldo también valida las credenciales cuando los puntos cubren todo el total.
        pago.consultarSaldo();
        if (valorTarjeta > 0) pago.pagar(valorTarjeta);
        if (puntosARedimir > 0) CuentaPuntos.redimir(cuenta, puntosARedimir, LocalDate.now());
        String idCompra = "COMPRA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Compra compra = new Compra(idCompra, valorTotal, LocalDate.now(), LocalTime.now(), carrito, cuenta, funcion);
        compra.registrarRedencion(puntosARedimir, descuento);
        int puntos = CuentaPuntos.puntosPorValor(valorTarjeta);
        compra.setPuntosGenerados(puntos);
        CuentaPuntos.acumular(cuenta, puntos, LocalDate.now(), idCompra);
        java.util.List<model.Entrada> entradasReservadas = new java.util.ArrayList<>();
        try {
            if (funcion != null) {
                for (composite.Elemento elemento : carrito.getElementos()) {
                    if (elemento instanceof model.Entrada entrada) {
                        funcion.ocuparAsiento(entrada.getElAsiento());
                        funcion.getListEntradasFuncion().add(entrada);
                        entradasReservadas.add(entrada);
                    }
                }
            }
            cine.getListCompras().add(compra);
        } catch (RuntimeException ex) {
            if (funcion != null) for (model.Entrada entrada : entradasReservadas) {
                funcion.liberarAsiento(entrada.getElAsiento());
                funcion.getListEntradasFuncion().remove(entrada);
            }
            CuentaPuntos.revertir(cuenta, idCompra);
            if (puntosARedimir > 0) CuentaPuntos.acumular(cuenta, puntosARedimir, LocalDate.now(), "REST-" + idCompra);
            model.TarjetaVirtual tarjeta = cuenta.getTheTarjetaVirtualCliente();
            if (valorTarjeta > 0) {
                tarjeta.setSaldo(tarjeta.getSaldo() + valorTarjeta);
                tarjeta.registrarMovimiento("Reversión", "Reversión de compra fallida " + idCompra, valorTarjeta);
            }
            throw ex;
        }
    }
    private void validarDatos() {
        if (cuenta == null) throw new IllegalArgumentException("No hay una cuenta de cliente activa.");
        if (carrito == null || carrito.estaVacio()) throw new IllegalArgumentException("El carrito está vacío.");
        if (!Double.isFinite(valorTotal) || valorTotal <= 0) throw new IllegalArgumentException("El total debe ser mayor a cero.");
        if (funcion != null && funcion.getInicio().isBefore(java.time.LocalDateTime.now())) throw new IllegalStateException("La función ya inició.");
        if (puntosARedimir < 0 || puntosARedimir > CuentaPuntos.puntosDisponibles(cuenta, LocalDate.now())) throw new IllegalStateException("Puntos insuficientes o inválidos.");
    }
}
