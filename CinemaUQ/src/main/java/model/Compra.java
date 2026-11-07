package model;

import composite.Carrito;
import fachada.Fachada;

import java.time.LocalDate;
import java.time.LocalTime;

public class Compra implements Fachada {

    //Atributos
    private String id;
    private double valorTotal;
    private LocalDate fecha;
    private LocalTime hora;
    private Carrito theCompraCarrito;

    //Constructor
    public Compra(String id, double valorTotal, LocalDate fecha, LocalTime hora, Carrito carrito) {
        this.id = id;
        this.valorTotal = valorTotal;
        this.fecha = fecha;
        this.hora = hora;
        this.theCompraCarrito = carrito;
    }


    public void hacerCompra() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Carrito getTheCompraCarrito() {
        return theCompraCarrito;
    }

    public void setTheCompraCarrito(Carrito theCompraCarrito) {
        this.theCompraCarrito = theCompraCarrito;
    }
}
