package composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Composite: contiene productos (hojas) y otros carritos (composites).
 */
public class Carrito implements Elemento {
    private final String id;
    private final List<Elemento> elementos = new ArrayList<>();

    public Carrito(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del carrito es obligatorio.");
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void agregar(Elemento elemento) {
        Objects.requireNonNull(elemento, "No se puede agregar un elemento nulo.");
        if (elemento == this) {
            throw new IllegalArgumentException("Un carrito no puede contenerse a sí mismo.");
        }
        if (elemento instanceof Carrito carrito && carrito.contiene(this)) {
            throw new IllegalArgumentException("La operación crearía un ciclo entre carritos.");
        }
        elementos.add(elemento);
    }

    public boolean eliminar(Elemento elemento) {
        return elementos.remove(elemento);
    }

    public void vaciar() {
        elementos.clear();
    }

    public List<Elemento> getElementos() {
        return Collections.unmodifiableList(elementos);
    }

    public boolean estaVacio() {
        return elementos.isEmpty();
    }

    private boolean contiene(Carrito buscado) {
        if (this == buscado) return true;
        for (Elemento elemento : elementos) {
            if (elemento instanceof Carrito carrito && carrito.contiene(buscado)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getNombre() {
        return "Carrito " + id;
    }

    @Override
    public double getPrecio() {
        return elementos.stream().mapToDouble(Elemento::getPrecio).sum();
    }

    @Override
    public String toString() {
        return getNombre() + " (" + elementos.size() + " elementos)";
    }
}
