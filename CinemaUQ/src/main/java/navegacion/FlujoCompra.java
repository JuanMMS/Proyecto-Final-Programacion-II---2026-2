package navegacion;

import composite.Carrito;
import model.Asiento;
import model.FuncionPelicula;
import service.Sesion;
import model.Cliente;
import model.Cuenta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Estado compartido del recorrido de compra entre las pantallas JavaFX. */
public final class FlujoCompra {
    private static final FlujoCompra INSTANCIA = new FlujoCompra();
    private FuncionPelicula funcion;
    private final List<Asiento> asientos = new ArrayList<>();
    private Carrito carrito;

    private FlujoCompra() { iniciarNuevoCarrito(); }
    public static FlujoCompra getInstancia() { return INSTANCIA; }

    public Cuenta getCuentaActual() {
        Object usuario = Sesion.getInstancia().getUsuarioActual();
        return usuario instanceof Cliente cliente ? cliente.getCuenta() : null;
    }
    public FuncionPelicula getFuncion() { return funcion; }
    public void setFuncion(FuncionPelicula funcion) {
        this.funcion = funcion;
        this.asientos.clear();
        iniciarNuevoCarrito();
    }
    public List<Asiento> getAsientos() { return Collections.unmodifiableList(asientos); }
    public void alternarAsiento(Asiento asiento) {
        if (asientos.contains(asiento)) asientos.remove(asiento); else asientos.add(asiento);
    }
    public void limpiarAsientos() { asientos.clear(); }
    public Carrito getCarrito() { return carrito; }
    public void iniciarNuevoCarrito() { carrito = new Carrito("CARRITO-ACTUAL"); }
    public void reiniciar() { funcion = null; asientos.clear(); iniciarNuevoCarrito(); }
}
