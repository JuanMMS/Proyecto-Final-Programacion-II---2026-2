
package model;

import decorator.ComplementoPalomita;

/**
 * Clase para la confiteria de palomitas
 */
public class Palomita extends Confiteria implements ComplementoPalomita {
    //Constructor
    public Palomita(String nombre, double precio) {
        super(nombre, precio);
    }

}
