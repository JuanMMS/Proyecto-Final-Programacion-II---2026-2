
package decorator;

/**
 * Clase para definir el complemento
 */
public abstract class Complemento implements ComplementoPalomita {

    protected final ComplementoPalomita palomita;

    //Constructor
    public Complemento(ComplementoPalomita palomita) {
        this.palomita = palomita;
    }

    //Metodos para la herencia
    protected abstract String descripcionComplemento();

    protected abstract double costoPropio();

    /**
     * Funcion para conocer el complemento agregado
     * @return descripcion del complemento agregado
     */
    @Override
    public String getNombre() {
        return palomita.getNombre() + " + "
                + descripcionComplemento();
    }

    /**
     * Funcion para conocer el precio extra del complemento
     * @return precio extra del complemento
     */
    @Override
    public double getPrecio() {
        return palomita.getPrecio() + costoPropio();
    }
}
