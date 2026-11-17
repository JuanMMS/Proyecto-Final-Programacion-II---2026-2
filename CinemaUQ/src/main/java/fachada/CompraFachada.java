package fachada;

import composite.Carrito;
import model.Cine;
import model.Compra;
import model.Cuenta;
import model.TarjetaVirtual;
import proxy.MedioPago;
import proxy.PagoTarjetaProxy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Facade del caso de uso de compra. Coordina validación, autorización,
 * pago y registro de la compra desde un único punto de entrada.
 */
public class CompraFachada implements Fachada {
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

    /**
     * Ejecuta la compra y devuelve el comprobante de dominio.
     * Si el registro falla después del débito, restaura el saldo anterior.
     */
    public Compra ejecutarCompra() {
        validarDatos();
        MedioPago medioPago = new PagoTarjetaProxy(cuenta, contrasenia);
        double saldoAnterior = medioPago.consultarSaldo();

        if (saldoAnterior < valorTotal) {
            throw new IllegalStateException("Saldo insuficiente en la tarjeta virtual.");
        }

        TarjetaVirtual tarjeta = cuenta.getTheTarjetaVirtualCliente();
        synchronized (tarjeta) {
            double saldoAntesDelDebito = tarjeta.getSaldo();
            Compra compra = new Compra(generarId(), valorTotal,
                    LocalDate.now(), LocalTime.now(), carrito, cuenta);

            try {
                medioPago.pagar(valorTotal);
                cine.getListCompras().add(compra);
                return compra;
            } catch (RuntimeException ex) {
                tarjeta.setSaldo(saldoAntesDelDebito);
                throw new IllegalStateException(
                        "No fue posible completar la compra. El saldo fue restaurado.", ex);
            }
        }
    }

    @Override
    public void hacerCompra() {
        ejecutarCompra();
    }

    private void validarDatos() {
        Objects.requireNonNull(cuenta, "La cuenta es obligatoria.");
        Objects.requireNonNull(carrito, "El carrito es obligatorio.");
        if (carrito.estaVacio()) {
            throw new IllegalArgumentException("No se puede comprar con el carrito vacío.");
        }
        if (!Double.isFinite(valorTotal) || valorTotal <= 0) {
            throw new IllegalArgumentException("El valor total debe ser mayor a cero.");
        }
        if (cuenta.getTheTarjetaVirtualCliente() == null) {
            throw new IllegalStateException("La cuenta no tiene una tarjeta virtual asociada.");
        }
    }

    private String generarId() {
        return "COMPRA-" + UUID.randomUUID();
    }
}
