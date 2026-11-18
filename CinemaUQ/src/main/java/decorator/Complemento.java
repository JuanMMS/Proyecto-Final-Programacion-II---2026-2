package decorator;

import java.util.Objects;
import composite.Elemento;
//Java
/** Decorador base: envuelve otra palomita y añade descripción y costo. */
public abstract class Complemento implements ComplementoPalomita, Elemento {
    protected final ComplementoPalomita palomita;

    protected Complemento(ComplementoPalomita palomita) {
        this.palomita = Objects.requireNonNull(palomita, "La palomita base es obligatoria.");
    }

    protected abstract String descripcionComplemento();
    protected abstract double costoPropio();

    @Override
    public String getNombre() {
        return palomita.getNombre() + " + " + descripcionComplemento();
    }

    @Override
    public double getPrecio() {
        return palomita.getPrecio() + costoPropio();
    }
}
