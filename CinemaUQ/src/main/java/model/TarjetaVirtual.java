package model;

public class TarjetaVirtual {
    //Atributos
    private String id;
    private double saldo;
    private Cuenta theCuentaTarjetaVirtual;

    /**
     * Constructor
     * @param id id
     * @param saldo saldo disponible
     * @param theCuentaTarjetaVirtual cuenta asociada
     */
    public TarjetaVirtual(String id, double saldo, Cuenta theCuentaTarjetaVirtual) {
        this.id = id;
        this.saldo = saldo;
        this.theCuentaTarjetaVirtual = theCuentaTarjetaVirtual;
    }
}
