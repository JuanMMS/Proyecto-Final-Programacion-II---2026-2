
package decorator;

/**
 * Complemento adicional para la palomita. Sal
 */
public class ConSal extends Complemento {

    private static final double COSTO_ADICIONAL = 1000;

    public ConSal(ComplementoPalomita palomita) {
        super(palomita);
    }

    @Override
    protected String descripcionComplemento() {
        return "Sal";
    }

    @Override
    protected double costoPropio() {
        return COSTO_ADICIONAL;
    }
}
