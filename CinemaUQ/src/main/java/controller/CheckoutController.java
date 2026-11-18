package controller;

import composite.Elemento;
import fachada.CompraFachada;
import model.*;
import navegacion.FlujoCompra;
import java.time.LocalDate;
import java.util.List;

/** Preparación y ejecución del caso de uso de pago. */
public class CheckoutController {
    private final FlujoCompra flujo = FlujoCompra.getInstancia();
    public Cuenta obtenerCuenta() { return flujo.getCuentaActual(); }
    public boolean tieneOrdenValida() { return flujo.getFuncion() != null && !flujo.getAsientos().isEmpty(); }
    public void prepararEntradas() {
        for (Asiento asiento : flujo.getAsientos()) {
            boolean existe = flujo.getCarrito().getElementos().stream()
                    .anyMatch(x -> x instanceof Entrada en && en.getElAsiento().equals(asiento));
            if (!existe) flujo.getCarrito().agregar(new Entrada("ENT-" + asiento.getIdAsiento(), 12000, asiento, flujo.getFuncion()));
        }
    }
    public List<Elemento> obtenerElementos() { return flujo.getCarrito().getElementos(); }
    public double obtenerTotal() { return flujo.getCarrito().getPrecio(); }
    public int obtenerPuntosDisponibles() { return CuentaPuntos.puntosDisponibles(obtenerCuenta(), LocalDate.now()); }
    /**
     * Los puntos se redimen únicamente al confirmar la compra completa del carrito.
     * No se limita la cantidad por el valor de la compra: se pueden usar todos los
     * puntos vigentes de la cuenta. El descuento monetario nunca supera el total.
     */
    public int calcularPuntosRedimibles(double total) {
        return total > 0 ? Math.max(0, obtenerPuntosDisponibles()) : 0;
    }
    public void confirmarPago(String contrasenia, boolean redimirPuntos, double total) {
        int puntos = redimirPuntos ? calcularPuntosRedimibles(total) : 0;
        new CompraFachada(obtenerCuenta(), contrasenia, flujo.getCarrito(), total, flujo.getFuncion(), puntos).hacerCompra();
        flujo.reiniciar();
    }
    public void reiniciarCompra() { flujo.reiniciar(); }
    public boolean tieneAsientosSeleccionados() { return !flujo.getAsientos().isEmpty(); }
}
