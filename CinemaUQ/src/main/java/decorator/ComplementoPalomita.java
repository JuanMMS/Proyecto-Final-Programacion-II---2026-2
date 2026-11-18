
package decorator;

import composite.Elemento;

/**
 * Clase para los metodos en comun de los complementos de la palomita
 */
public interface ComplementoPalomita extends Elemento {

    String getNombre();

    double getPrecio();

}
