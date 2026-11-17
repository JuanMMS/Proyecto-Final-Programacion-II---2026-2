package proxy;

import model.TarjetaVirtual;


public class PagoTarjetaVirtual implements MedioPago {

    //Atributos
    private final TarjetaVirtual tarjeta;

    //Constructor
    public PagoTarjetaVirtual(TarjetaVirtual tarjeta) {
        this.tarjeta = tarjeta;
    }


    @Override
    public double consultarSaldo() {
        return tarjeta.getSaldo();
    }


    @Override
    public void pagar(double monto) {
        tarjeta.setSaldo(tarjeta.getSaldo() - monto);
    }
}