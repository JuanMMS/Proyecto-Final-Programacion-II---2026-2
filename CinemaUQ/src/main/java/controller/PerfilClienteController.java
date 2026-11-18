package controller;

import model.*;
import service.Sesion;
import java.time.LocalDate;
import java.util.List;

/** Consultas y operaciones del perfil de cliente. */
public class PerfilClienteController {
    public Cliente obtenerClienteActual() {
        Object actual = Sesion.getInstancia().getUsuarioActual();
        return actual instanceof Cliente c ? c : null;
    }
    public int obtenerPuntosDisponibles(Cuenta cuenta) { return CuentaPuntos.puntosDisponibles(cuenta, LocalDate.now()); }
    public List<Compra> obtenerCompras(Cuenta cuenta) {
        return Cine.getInstancia().getListCompras().stream().filter(c -> c.getCuenta() == cuenta).toList();
    }
    public double cancelarCompra(Compra compra) {
        if (compra == null) throw new IllegalArgumentException("Selecciona una compra.");
        if (compra.isCancelada()) throw new IllegalArgumentException("Esta compra ya está cancelada.");
        if (!compra.puedeCancelarse(java.time.LocalDateTime.now()))
            throw new IllegalArgumentException("Esta compra ya no se puede cancelar porque la función inició o no tiene una función válida.");
        return compra.cancelar(java.time.LocalDateTime.now());
    }
    public void cerrarSesion() { Sesion.getInstancia().cerrarSesion(); }
}
