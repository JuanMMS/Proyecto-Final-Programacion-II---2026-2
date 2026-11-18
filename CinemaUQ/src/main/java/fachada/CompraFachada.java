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
        if (funcion != null) {
            for (composite.Elemento elemento : carrito.getElementos()) {
                if (elemento instanceof model.Entrada entrada
                        && funcion.isAsientoOcupado(entrada.getElAsiento())) {
                    throw new IllegalStateException("El asiento " + entrada.getElAsiento().getIdAsiento() + " ya fue vendido.");
                }
            }
        }
        MedioPago pago = new PagoTarjetaProxy(cuenta, contrasenia);
        pago.pagar(valorTotal);
        Compra compra = new Compra("COMPRA-" + (cine.getListCompras().size() + 1), valorTotal,
                LocalDate.now(), LocalTime.now(), carrito, cuenta, funcion);
        cine.getListCompras().add(compra);
        if (funcion != null) {
            for (composite.Elemento elemento : carrito.getElementos()) {
                if (elemento instanceof model.Entrada entrada) {
                    funcion.ocuparAsiento(entrada.getElAsiento());
                    funcion.getListEntradasFuncion().add(entrada);
                }
            }
        }
    }
    private void validarDatos() {
        if (cuenta == null) throw new IllegalArgumentException("No hay una cuenta de cliente activa.");
        if (carrito == null || carrito.estaVacio()) throw new IllegalArgumentException("El carrito está vacío.");
        if (!Double.isFinite(valorTotal) || valorTotal <= 0) throw new IllegalArgumentException("El total debe ser mayor a cero.");
        if (funcion != null && funcion.getInicio().isBefore(java.time.LocalDateTime.now())) throw new IllegalStateException("La función ya inició.");
    }
}
