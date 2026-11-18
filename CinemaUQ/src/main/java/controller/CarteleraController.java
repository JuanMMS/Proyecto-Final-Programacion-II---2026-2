package controller;

import model.*;
import navegacion.FlujoCompra;
import service.Sesion;
import java.time.LocalDate;
import java.util.List;

/** Casos de uso de consulta de cartelera y selección de función. */
public class CarteleraController {
    public List<Pelicula> obtenerPeliculas() { return Cine.getInstancia().obtenerCartelera(); }
    public Cliente obtenerClienteActual() {
        Object actual = Sesion.getInstancia().getUsuarioActual();
        return actual instanceof Cliente c ? c : null;
    }
    public int obtenerPuntosDisponibles(Cliente cliente) {
        return CuentaPuntos.puntosDisponibles(cliente.getCuenta(), LocalDate.now());
    }
    public void seleccionarFuncion(FuncionPelicula funcion) {
        if (funcion == null) throw new IllegalArgumentException("Selecciona una función para continuar.");
        FlujoCompra.getInstancia().setFuncion(funcion);
    }
    public void cerrarSesion() {
        Sesion.getInstancia().cerrarSesion();
        FlujoCompra.getInstancia().reiniciar();
    }
}
