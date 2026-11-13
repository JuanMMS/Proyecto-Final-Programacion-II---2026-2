package composite;

import java.time.LocalDate;
import java.time.LocalTime;

public class Carrito implements Elemento {
    private String id;

    public Carrito(String id) {
        this.id = id;
    }
}
