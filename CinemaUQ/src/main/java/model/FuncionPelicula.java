package model;

import observer.Observador; // Importamos tu interfaz
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class FuncionPelicula {
    // Atributos base
    private String id;
    private LocalDate fecha;
    private LocalTime hora;
    private ArrayList<Entrada> listEntradasFuncion;
    private SalaCine theSalaCine;
    private Pelicula thePeliculaFuncion;

    // Control de asientos específicos para ESTA función (Evita vender 2 veces)
    private ArrayList<Asiento> asientosOcupados;

    // Patrón Observer: Lista de interesados (Cuentas/Clientes)
    private ArrayList<Observador> observadores;

    /**
     * Constructor
     */
    public FuncionPelicula(String id, LocalDate fecha, LocalTime hora, SalaCine theSalaCine, Pelicula thePeliculaFuncion) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.theSalaCine = theSalaCine;
        this.thePeliculaFuncion = thePeliculaFuncion;

        // Inicializamos las listas vacías
        this.listEntradasFuncion = new ArrayList<>();
        this.asientosOcupados = new ArrayList<>();
        this.observadores = new ArrayList<>();
    }

    // ==========================================
    // MÉTODOS DE CONTROL DE ASIENTOS (Requisito)
    // ==========================================

    /**
     * Verifica si un asiento ya fue vendido en esta función.
     */
    public boolean isAsientoOcupado(Asiento asiento) {
        return asientosOcupados.contains(asiento);
    }

    /**
     * Marca un asiento como ocupado al concretar la venta.
     */
    public void ocuparAsiento(Asiento asiento) throws IllegalArgumentException {
        if (isAsientoOcupado(asiento)) {
            throw new IllegalArgumentException("El asiento ya está ocupado en esta función.");
        }
        asientosOcupados.add(asiento);
    }

    public void liberarAsiento(Asiento asiento) {
        asientosOcupados.remove(asiento);
    }

    // ==========================================
    // MÉTODOS DEL PATRÓN OBSERVER
    // ==========================================

    public void agregarObservador(Observador observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void eliminarObservador(Observador observador) {
        observadores.remove(observador);
    }

    public void notificarObservadores(String mensaje) {
        for (Observador observador : observadores) {
            observador.actualizar(mensaje);
        }
    }

    // ==========================================
    // GETTERS Y SETTERS TRADICIONALES
    // ==========================================

    public LocalDateTime getInicio() {
        return LocalDateTime.of(fecha, hora);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public ArrayList<Entrada> getListEntradasFuncion() { return listEntradasFuncion; }
    public SalaCine getTheSalaCine() { return theSalaCine; }
    public Pelicula getThePeliculaFuncion() { return thePeliculaFuncion; }
    public ArrayList<Asiento> getAsientosOcupados() { return asientosOcupados; }

    @Override
    public String toString() {
        String pelicula = thePeliculaFuncion != null ? thePeliculaFuncion.getNombre() : "Sin película";
        return pelicula + " - " + fecha + " " + hora;
    }
}