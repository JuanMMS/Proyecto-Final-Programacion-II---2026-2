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

    public CompraFachada(Cuenta cuenta, String contrasenia, Carrito carrito, double valorTotal) {
        this(cuenta, contrasenia, carrito, valorTotal, null);
    }
    public CompraFachada(Cuenta cuenta, String contrasenia, Carrito carrito, double valorTotal, FuncionPelicula funcion) {
        this.cuenta = cuenta; this.contrasenia = contrasenia; this.carrito = carrito;
        this.valorTotal = valorTotal; this.funcion = funcion;
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
        pago.pagar(valorTotal);
        String idCompra = "COMPRA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Compra compra = new Compra(idCompra, valorTotal, LocalDate.now(), LocalTime.now(), carrito, cuenta, funcion);
        int puntos = CuentaPuntos.puntosPorValor(valorTotal);
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
            model.TarjetaVirtual tarjeta = cuenta.getTheTarjetaVirtualCliente();
            tarjeta.setSaldo(tarjeta.getSaldo() + valorTotal);
            tarjeta.registrarMovimiento("Reversión", "Reversión de compra fallida " + idCompra, valorTotal);
            throw ex;
        }
    }
    private void validarDatos() {
        if (cuenta == null) throw new IllegalArgumentException("No hay una cuenta de cliente activa.");
        if (carrito == null || carrito.estaVacio()) throw new IllegalArgumentException("El carrito está vacío.");
        if (!Double.isFinite(valorTotal) || valorTotal <= 0) throw new IllegalArgumentException("El total debe ser mayor a cero.");
        if (funcion != null && funcion.getInicio().isBefore(java.time.LocalDateTime.now())) throw new IllegalStateException("La función ya inició.");
    }
}
