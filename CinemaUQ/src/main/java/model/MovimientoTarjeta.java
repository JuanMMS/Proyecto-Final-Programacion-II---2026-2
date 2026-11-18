package model;

import java.time.LocalDateTime;

/** Registro inmutable de una operación sobre la tarjeta virtual. */
public class MovimientoTarjeta {
    private final LocalDateTime fecha;
    private final String tipo;
    private final String descripcion;
    private final double monto;
    private final double saldoResultante;

    public MovimientoTarjeta(String tipo, String descripcion, double monto, double saldoResultante) {
        this.fecha = LocalDateTime.now();
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.monto = monto;
        this.saldoResultante = saldoResultante;
    }
    public LocalDateTime getFecha() { return fecha; }
    public String getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public double getMonto() { return monto; }
    public double getSaldoResultante() { return saldoResultante; }
}
