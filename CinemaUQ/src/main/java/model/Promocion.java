package model;

import java.time.LocalDate;

/** Promoción administrable del catálogo (el descuento se registra, no se aplica al checkout aún). */
public class Promocion {
    private final String codigo;
    private final int porcentaje;
    private final LocalDate fechaCreacion;
    private boolean activa = true;
    public Promocion(String codigo, int porcentaje) {
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("El código es obligatorio.");
        if (porcentaje < 1 || porcentaje > 90) throw new IllegalArgumentException("El descuento debe estar entre 1 y 90%.");
        this.codigo = codigo.trim().toUpperCase(); this.porcentaje = porcentaje; this.fechaCreacion = LocalDate.now();
    }
    public String getCodigo() { return codigo; }
    public int getPorcentaje() { return porcentaje; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    @Override public String toString() { return codigo + " (" + porcentaje + "%)"; }
}
