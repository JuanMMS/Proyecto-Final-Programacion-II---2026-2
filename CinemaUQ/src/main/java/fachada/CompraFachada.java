package fachada;

import composite.Carrito;
import model.Cine;
import model.Compra;
import model.Cuenta;
import proxy.MedioPago;
import proxy.PagoTarjetaProxy;

import java.time.LocalDate;
import java.time.LocalTime;


public class CompraFachada implements Fachada {

    //Atributos
    private final Cine cine;
    private final Cuenta cuenta;
    private final String contrasenia;
    private final Carrito carrito;
    private final double valorTotal;


    public CompraFachada(Cuenta cuenta, String contrasenia, Carrito carrito, double valorTotal) {
        this.cine = Cine.getInstancia();
        this.cuenta = cuenta;
        this.contrasenia = contrasenia;
        this.carrito = carrito;
        this.valorTotal = valorTotal;
    }


    @Override
    public void hacerCompra() {
        validarDatos();

        //El pago pasa por el proxy, que valida contrasenia y saldo
        MedioPago medioPago = new PagoTarjetaProxy(cuenta, contrasenia);
        medioPago.pagar(valorTotal);

        //Se crea y registra la compra
        Compra compra = new Compra(generarId(), valorTotal, LocalDate.now(), LocalTime.now(), carrito);
        cine.getListCompras().add(compra);
    }


    private void validarDatos() {
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta es obligatoria");
        }
        if (carrito == null) {
            throw new IllegalArgumentException("El carrito es obligatorio");
        }
        if (valorTotal <= 0) {
            throw new IllegalArgumentException("El valor total debe ser mayor a cero");
        }
    }


    private String generarId() {
        return "COMPRA-" + (cine.getListCompras().size() + 1);
    }
}
