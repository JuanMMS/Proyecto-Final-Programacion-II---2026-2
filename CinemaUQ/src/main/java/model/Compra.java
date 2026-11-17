package model;

import composite.Carrito;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/** Registro de una compra realizada por una cuenta. */
public class Compra {
    private String id;
    private double valorTotal;
    private LocalDate fecha;
    private LocalTime hora;
    private Carrito theCompraCarrito;
    private Cuenta cuenta;

    /** Constructor compatible con el código anterior. */
    public Compra(String id, double valorTotal, LocalDate fecha, LocalTime hora, Carrito carrito) {
        this(id, valorTotal, fecha, hora, carrito, null);
    }

    public Compra(String id, double valorTotal, LocalDate fecha, LocalTime hora,
                  Carrito carrito, Cuenta cuenta) {
        setId(id);
        setValorTotal(valorTotal);
        this.fecha = Objects.requireNonNull(fecha, "La fecha es obligatoria.");
        this.hora = Objects.requireNonNull(hora, "La hora es obligatoria.");
        this.theCompraCarrito = Objects.requireNonNull(carrito, "El carrito es obligatorio.");
        this.cuenta = cuenta;
    }

    public String getId() { return id; }

    public void setId(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("El id es obligatorio.");
        this.id = id;
    }

    public double getValorTotal() { return valorTotal; }

    public void setValorTotal(double valorTotal) {
        if (!Double.isFinite(valorTotal) || valorTotal <= 0) {
            throw new IllegalArgumentException("El valor total debe ser mayor a cero.");
        }
        this.valorTotal = valorTotal;
    }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = Objects.requireNonNull(fecha); }
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = Objects.requireNonNull(hora); }
    public Carrito getTheCompraCarrito() { return theCompraCarrito; }
    public void setTheCompraCarrito(Carrito carrito) {
        this.theCompraCarrito = Objects.requireNonNull(carrito);
    }
    public Cuenta getCuenta() { return cuenta; }
    public void setCuenta(Cuenta cuenta) { this.cuenta = cuenta; }
}
