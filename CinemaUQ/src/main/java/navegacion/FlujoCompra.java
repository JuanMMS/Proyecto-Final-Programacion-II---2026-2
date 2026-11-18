package navegacion;

import composite.Carrito;
import model.Asiento;
import model.FuncionPelicula;
import java.util.ArrayList;
import java.util.List;

/** Estado compartido del proceso de compra entre cartelera, asientos, confitería y checkout. */
public final class FlujoCompra {
    private static final Carrito CARRITO = new Carrito("CINEMAUQ-SESSION");
    private static FuncionPelicula funcionActual;
    private static final List<Asiento> ASIENTOS = new ArrayList<>();
    private FlujoCompra() {}
    public static Carrito getCarrito() { return CARRITO; }
    public static FuncionPelicula getFuncionActual() { return funcionActual; }
    public static void iniciar(FuncionPelicula funcion) { funcionActual = funcion; ASIENTOS.clear(); CARRITO.vaciar(); }
    public static List<Asiento> getAsientos() { return ASIENTOS; }
    public static void limpiar() { funcionActual = null; ASIENTOS.clear(); CARRITO.vaciar(); }
}
