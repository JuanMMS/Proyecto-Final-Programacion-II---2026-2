package composite;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Carrito implements Elemento {
    private String id;
    private double precioFinal;
    private List<Elemento> elementoList;

    public Carrito(String id) {

        this.id = id;
        this.precioFinal = precioFinal;
    }

    public double obtenerPrecioFinal() {
        double contador = 0;
        for (Elemento elemento : elementoList) {
            elemento.getPrecio() += contador;
        }
            return contador;
    }

    @Override
    public String getNombre() {
        return "";
    }

    @Override
    public double getPrecio() {
        return 0;
    }
}
