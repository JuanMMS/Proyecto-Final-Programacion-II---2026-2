package controller;

import model.*;
import navegacion.FlujoCompra;
import java.util.List;

/** Reglas de selección de asientos; no conoce componentes visuales. */
public class AsientosController {
    public static final double PRECIO_ENTRADA = 12000.0;
    private final FlujoCompra flujo = FlujoCompra.getInstancia();
    public FuncionPelicula obtenerFuncion() { return flujo.getFuncion(); }
    public List<Asiento> obtenerAsientos() {
        return flujo.getFuncion() == null ? List.of() : flujo.getFuncion().getTheSalaCine().getListAsientosSalaCine();
    }
    public List<Asiento> obtenerSeleccionados() { return flujo.getAsientos(); }
    public boolean estaOcupado(Asiento asiento) {
        return flujo.getFuncion() != null && flujo.getFuncion().getAsientosOcupados().contains(asiento);
    }
    public void alternarSeleccion(Asiento asiento) {
        if (estaOcupado(asiento)) throw new IllegalArgumentException("El asiento ya está ocupado.");
        flujo.alternarAsiento(asiento);
    }
    public double calcularSubtotal() { return flujo.getAsientos().size() * PRECIO_ENTRADA; }
    public boolean puedeContinuar() { return !flujo.getAsientos().isEmpty(); }
}
