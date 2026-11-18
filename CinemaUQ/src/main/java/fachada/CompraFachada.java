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

/** Coordina validación, pago protegido y registro de una compra. */
public class CompraFachada implements Fachada {
    private final Cine cine;
    private final Cuenta cuenta;
    private final String contrasenia;
    private final Carrito carrito;
    private final double valorTotal;
    private final FuncionPelicula funcion;

    public CompraFachada(Cuenta cuenta, String contrasenia, Carrito carrito, double valorTotal) {
        this(cuenta, contrasenia, carrito, valorTotal, null);
    }
    public CompraFachada(Cuenta cuenta, String contrasenia, Carrito carrito, double valorTotal, FuncionPelicula funcion) {
        this.cine=Cine.getInstancia();this.cuenta=cuenta;this.contrasenia=contrasenia;this.carrito=carrito;this.valorTotal=valorTotal;this.funcion=funcion;
    }
    @Override public void hacerCompra() { ejecutarCompra(); }
    public Compra ejecutarCompra() {
        validarDatos();
        MedioPago medioPago=new PagoTarjetaProxy(cuenta,contrasenia);
        medioPago.pagar(valorTotal);
        String id="COMPRA-"+UUID.randomUUID().toString().substring(0,8).toUpperCase();
        Compra compra=new Compra(id,valorTotal,LocalDate.now(),LocalTime.now(),carrito,cuenta,funcion);
        cine.getListCompras().add(compra);
        return compra;
    }
    private void validarDatos(){
        if(cuenta==null)throw new IllegalArgumentException("La cuenta es obligatoria.");
        if(carrito==null||carrito.estaVacio())throw new IllegalArgumentException("El carrito está vacío.");
        if(!Double.isFinite(valorTotal)||valorTotal<=0)throw new IllegalArgumentException("El valor total debe ser mayor a cero.");
        if(Math.abs(carrito.getPrecio()-valorTotal)>0.01)throw new IllegalArgumentException("El total no coincide con el contenido del carrito.");
    }
}
