package Factory;

import model.TipoChocolatina;
import model.TipoGaseosa;

public class DatosConfiteria {
    private String nombre;
    private double precio;


    //Datos para chocolatina
    private TipoChocolatina tipoChocolatina;

    //Datos para gaseosa
    private TipoGaseosa tipoGaseosa;

    //Datos Palomita FALTA EL DECORADOR DE PALOMITA
    //private DecoradorPalomita decoradorPalomita;


    /**
     * Metodo constructor con los datos necesarios para crear una confitería básica
     * @param nombre
     * @param precio
     */
    public DatosConfiteria(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    /**
     * Metodo constructor con los datos necesarios para crear una chocolatina
     * @param nombre
     * @param precio
     * @param tipoChocolatina
     */
    public DatosConfiteria(String nombre, double precio, TipoChocolatina tipoChocolatina) {
        this.nombre = nombre;
        this.precio = precio;
        this.tipoChocolatina = tipoChocolatina;
    }

    /**
     * Metodo constructor con los datos necesarios para crear una gaseosa
     * @param nombre
     * @param precio
     * @param tipoGaseosa
     */
    public DatosConfiteria(String nombre, double precio, TipoGaseosa tipoGaseosa) {
        this.nombre = nombre;
        this.precio = precio;
        this.tipoGaseosa = tipoGaseosa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public TipoChocolatina getTipoChocolatina() {
        return tipoChocolatina;
    }

    public void setTipoChocolatina(TipoChocolatina tipoChocolatina) {
        this.tipoChocolatina = tipoChocolatina;
    }

    public TipoGaseosa getTipoGaseosa() {
        return tipoGaseosa;
    }

    public void setTipoGaseosa(TipoGaseosa tipoGaseosa) {
        this.tipoGaseosa = tipoGaseosa;
    }
}
