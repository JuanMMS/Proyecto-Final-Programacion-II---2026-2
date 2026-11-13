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

    //Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Cuenta getTheCuentaTarjetaVirtual() {
        return theCuentaTarjetaVirtual;
    }

    public void setTheCuentaTarjetaVirtual(Cuenta theCuentaTarjetaVirtual) {
        this.theCuentaTarjetaVirtual = theCuentaTarjetaVirtual;
    }
}
