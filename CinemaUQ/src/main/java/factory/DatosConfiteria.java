package factory;

import model.TipoChocolatina;
import model.TipoGaseosa;

/** Datos de entrada para construir un producto mediante Factory Method. */
public class DatosConfiteria {
    private final String nombre;
    private final double precio;
    private final TipoChocolatina tipoChocolatina;
    private final TipoGaseosa tipoGaseosa;

    public DatosConfiteria(String nombre, double precio) {
        this(nombre, precio, null, null);
    }

    public DatosConfiteria(String nombre, double precio, TipoChocolatina tipoChocolatina) {
        this(nombre, precio, tipoChocolatina, null);
    }

    public DatosConfiteria(String nombre, double precio, TipoGaseosa tipoGaseosa) {
        this(nombre, precio, null, tipoGaseosa);
    }

    private DatosConfiteria(String nombre, double precio,
                            TipoChocolatina tipoChocolatina, TipoGaseosa tipoGaseosa) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (!Double.isFinite(precio) || precio < 0) {
            throw new IllegalArgumentException("El precio debe ser positivo o cero.");
        }
        this.nombre = nombre.trim();
        this.precio = precio;
        this.tipoChocolatina = tipoChocolatina;
        this.tipoGaseosa = tipoGaseosa;
    }

    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public TipoChocolatina getTipoChocolatina() { return tipoChocolatina; }
    public TipoGaseosa getTipoGaseosa() { return tipoGaseosa; }
}
