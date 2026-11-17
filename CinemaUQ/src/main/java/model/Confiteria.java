package model;

import composite.Elemento;

/** Producto individual de confitería (hoja del patrón Composite). */
public abstract class Confiteria implements Elemento {
    private String nombre;
    private double precio;

    protected Confiteria(String nombre, double precio) {
        setNombre(nombre);
        setPrecio(precio);
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    @Override
    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (!Double.isFinite(precio) || precio < 0) {
            throw new IllegalArgumentException("El precio debe ser un valor positivo o cero.");
        }
        this.precio = precio;
    }

    @Override
    public String toString() {
        return nombre + " - $" + String.format("%.2f", precio);
    }
}
