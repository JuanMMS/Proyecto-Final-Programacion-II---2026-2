package composite;

/**
 * Componente común del patrón Composite.
 * Tanto los productos individuales como los carritos pueden tratarse
 * de la misma manera al consultar su nombre y precio.
 */
public interface Elemento {
    String getNombre();
    double getPrecio();
}
