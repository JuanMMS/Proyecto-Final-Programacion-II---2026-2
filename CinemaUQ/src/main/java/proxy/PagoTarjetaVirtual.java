package proxy;

import model.TarjetaVirtual;
import java.util.Objects;

/** Servicio real de pago contra el saldo de una tarjeta virtual. */
public class PagoTarjetaVirtual implements MedioPago {
    private final TarjetaVirtual tarjeta;

    public PagoTarjetaVirtual(TarjetaVirtual tarjeta) {
        this.tarjeta = Objects.requireNonNull(tarjeta, "La tarjeta es obligatoria.");
    }

    @Override
    public double consultarSaldo() {
        synchronized (tarjeta) {
            return tarjeta.getSaldo();
        }
    }

    @Override
    public void pagar(double monto) {
        validarMonto(monto);
        synchronized (tarjeta) {
            if (tarjeta.getSaldo() < monto) {
                throw new IllegalStateException("Saldo insuficiente en la tarjeta virtual.");
            }
            tarjeta.setSaldo(tarjeta.getSaldo() - monto);
            tarjeta.registrarMovimiento("Compra", "Pago de compra CinemaUQ", -monto);
        }
    }

    private void validarMonto(double monto) {
        if (!Double.isFinite(monto) || monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }
    }
}
