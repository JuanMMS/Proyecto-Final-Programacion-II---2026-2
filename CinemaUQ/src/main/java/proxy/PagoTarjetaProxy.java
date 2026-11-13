package proxy;

import model.Cuenta;
import model.TarjetaVirtual;


public class PagoTarjetaProxy implements MedioPago {

    //Atributos
    private final Cuenta cuenta;
    private final String contrasenia;
    private PagoTarjetaVirtual pagoReal;


    public PagoTarjetaProxy(Cuenta cuenta, String contrasenia) {
        this.cuenta = cuenta;
        this.contrasenia = contrasenia;
    }


    private PagoTarjetaVirtual obtenerPagoReal() {
        if (cuenta == null || contrasenia == null || !contrasenia.equals(cuenta.getContrasenia())) {
            throw new SecurityException("Acceso denegado: contrasenia incorrecta");
        }

        if (pagoReal == null) {
            TarjetaVirtual tarjeta = cuenta.getTheTarjetaVirtualCliente();
            if (tarjeta == null) {
                throw new IllegalStateException("La cuenta no tiene una tarjeta virtual asociada");
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
        PagoTarjetaVirtual real = obtenerPagoReal();

        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a pagar debe ser mayor a cero");
        }
        if (real.consultarSaldo() < monto) {
            throw new IllegalStateException("Saldo insuficiente en la tarjeta virtual");
        }
        real.pagar(monto);
    }
}
