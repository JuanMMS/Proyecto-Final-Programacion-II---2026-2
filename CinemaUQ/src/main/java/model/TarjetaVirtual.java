package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TarjetaVirtual {
    //Atributos
    private String id;
    private double saldo;
    private Cuenta theCuentaTarjetaVirtual;
    private final List<MovimientoTarjeta> movimientos = new ArrayList<>();

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

    public synchronized double getSaldo() {
        return saldo;
    }

    public synchronized void setSaldo(double saldo) {
        if (!Double.isFinite(saldo) || saldo < 0) throw new IllegalArgumentException("El saldo no puede ser negativo.");
        this.saldo = saldo;
    }

    public synchronized void registrarMovimiento(String tipo, String descripcion, double monto) {
        if (tipo == null || tipo.isBlank()) throw new IllegalArgumentException("Tipo de movimiento obligatorio.");
        movimientos.add(new MovimientoTarjeta(tipo, descripcion == null ? "" : descripcion, monto, saldo));
    }

    public synchronized List<MovimientoTarjeta> getMovimientos() {
        return Collections.unmodifiableList(new ArrayList<>(movimientos));
    }

    public Cuenta getTheCuentaTarjetaVirtual() {
        return theCuentaTarjetaVirtual;
    }

    public void setTheCuentaTarjetaVirtual(Cuenta theCuentaTarjetaVirtual) {
        this.theCuentaTarjetaVirtual = theCuentaTarjetaVirtual;
    }
}
