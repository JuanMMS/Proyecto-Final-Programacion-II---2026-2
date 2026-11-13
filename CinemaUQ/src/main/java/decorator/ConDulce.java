
package decorator;

/**
 * Complemento adicional para la palomita. Dulce
 */
public class ConDulce extends Complemento {

    private static final double COSTO_ADICIONAL = 2000;

    public ConDulce(ComplementoPalomita palomita) {
        super(palomita);
    }

    @Override
    protected String descripcionComplemento() {
        return "Dulce";
    }

    @Override
    protected double costoPropio() {
        return COSTO_ADICIONAL;
    }
}
