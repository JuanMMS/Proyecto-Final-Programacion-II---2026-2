package controller;

import composite.Elemento;
import decorator.*;
import factory.*;
import factory.combos.Combo1;
import model.*;
import navegacion.FlujoCompra;
import java.util.List;

/** Casos de uso del catálogo de confitería y del carrito. */
public class ConfiteriaController {
    private final FlujoCompra flujo = FlujoCompra.getInstancia();
    public List<Confiteria> obtenerInventario() { return Cine.getInstancia().getListConfiterias(); }
    public List<Elemento> obtenerCarrito() { return flujo.getCarrito().getElementos(); }
    public double obtenerTotal() { return flujo.getCarrito().getPrecio(); }
    public void agregarCombo() {
        Combo1 combo = new Combo1();
        flujo.getCarrito().agregar(combo.crearGasesosa());
        flujo.getCarrito().agregar((Elemento)combo.crearPalomita());
        flujo.getCarrito().agregar(combo.crearChocolatina());
    }
    public void agregarPalomitas(boolean sal, boolean dulce) {
        ComplementoPalomita item = new Palomita("Palomitas grandes", 8000);
        if (sal) item = new ConSal(item);
        if (dulce) item = new ConDulce(item);
        flujo.getCarrito().agregar((Elemento)item);
    }
    public void agregarGaseosa(String nombre) {
        if (nombre == null) throw new IllegalArgumentException("Selecciona una gaseosa.");
        TipoGaseosa tipo = TipoGaseosa.valueOf(nombre);
        flujo.getCarrito().agregar(new GaseosaFactory().crearConfiteria(new DatosConfiteria(tipo.name(), 5000, tipo)));
    }
    public void agregarChocolatina(String nombre) {
        if (nombre == null) throw new IllegalArgumentException("Selecciona una chocolatina.");
        TipoChocolatina tipo = TipoChocolatina.valueOf(nombre);
        flujo.getCarrito().agregar(new ChocolatinaFactory().crearConfiteria(new DatosConfiteria(tipo.name(), 4500, tipo)));
    }
    public void agregarProducto(Confiteria producto) {
        if (producto == null) throw new IllegalArgumentException("No hay un producto del inventario seleccionado.");
        flujo.getCarrito().agregar(producto);
    }
    public boolean puedeIrACheckout() { return flujo.getFuncion() != null && !flujo.getAsientos().isEmpty(); }
    public void reiniciarCompra() { flujo.reiniciar(); }
}
