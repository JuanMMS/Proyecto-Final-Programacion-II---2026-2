package proxy;

public interface MedioPago {



    double consultarSaldo();


    void pagar(double monto);
}
