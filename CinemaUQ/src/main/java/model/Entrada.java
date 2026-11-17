package model;

import composite.Elemento; // Importamos la interfaz del Composite

public class Entrada implements Elemento {

    // Atributos
    private String idEntrada;
    private double precio;
    private Asiento elAsiento;
    private FuncionPelicula laFuncion;

    /**
     * Constructor
     */
    public Entrada(String idEntrada, double precio, Asiento elAsiento, FuncionPelicula laFuncion) {
        this.idEntrada = idEntrada;
        this.precio = precio;
        this.elAsiento = elAsiento;
        this.laFuncion = laFuncion;
    }

    // ==========================================
    // PATRÓN COMPOSITE (Interfaz Elemento)
    // ==========================================

    @Override
    public String getNombre() {
        String nombrePeli = (laFuncion != null && laFuncion.getThePeliculaFuncion() != null)
                ? laFuncion.getThePeliculaFuncion().getNombre()
                : "Película";
        return "Entrada - " + nombrePeli + " (Asiento " + elAsiento.getIdAsiento() + ")";
    }

    @Override
    public double getPrecio() {
        return this.precio;
    }

    // ==========================================
    // GETTERS Y SETTERS TRADICIONALES
    // ==========================================

    public String getIdEntrada() { return idEntrada; }
    public void setIdEntrada(String idEntrada) { this.idEntrada = idEntrada; }

    public void setPrecio(double precio) { this.precio = precio; }

    public Asiento getElAsiento() { return elAsiento; }
    public void setElAsiento(Asiento elAsiento) { this.elAsiento = elAsiento; }

    public FuncionPelicula getLaFuncion() { return laFuncion; }
    public void setLaFuncion(FuncionPelicula laFuncion) { this.laFuncion = laFuncion; }

    @Override
    public String toString() {
        return getNombre() + " | $" + getPrecio();
    }
}