package proxy;

import model.Cuenta;
import model.TarjetaVirtual;

/**
 * Proxy de protección: valida identidad, contraseña, existencia de tarjeta,
 * monto y saldo antes de delegar el pago al objeto real.
 */
public class PagoTarjetaProxy implements MedioPago {
    private final Cuenta cuenta;
    private final String contrasenia;
    private PagoTarjetaVirtual pagoReal;

    public PagoTarjetaProxy(Cuenta cuenta, String contrasenia) {
        this.cuenta = cuenta;
        this.contrasenia = contrasenia;
    }

    private PagoTarjetaVirtual obtenerPagoReal() {
        if (cuenta == null || contrasenia == null
                || cuenta.getContrasenia() == null
                || !contrasenia.equals(cuenta.getContrasenia())) {
            throw new SecurityException("Acceso denegado: credenciales incorrectas.");
        }

        if (pagoReal == null) {
            TarjetaVirtual tarjeta = cuenta.getTheTarjetaVirtualCliente();
            if (tarjeta == null) {
                throw new IllegalStateException("La cuenta no tiene una tarjeta virtual asociada.");
            }
            pagoReal = new PagoTarjetaVirtual(tarjeta);
        }
        return pagoReal;
    }

    @Override
    public double consultarSaldo() {
        return obtenerPagoReal().consultarSaldo();
    }

    @Override
    public void pagar(double monto) {
        if (!Double.isFinite(monto) || monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero.");
        }
        PagoTarjetaVirtual real = obtenerPagoReal();
        if (real.consultarSaldo() < monto) {
            throw new IllegalStateException("Saldo insuficiente en la tarjeta virtual.");
        }
        real.pagar(monto);
    }
}
