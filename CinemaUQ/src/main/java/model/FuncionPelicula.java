package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class FuncionPelicula {
    //Atributos
    private String id;
    private LocalDate fecha;
    private LocalTime hora;
    private ArrayList<Entrada> listEntradasFuncion;
    private SalaCine theSalaCine;
    private Pelicula thePeliculaFuncion;

    //Observer
    private ArrayList<Cuenta> listCuentasSuscritas;

    /**
     * Constructor
     * @param id
     * @param fecha
     * @param hora
     * @param listEntradasFuncion
     * @param theSalaCine
     * @param thePeliculaFuncion
     */
    public FuncionPelicula(String id, LocalDate fecha, LocalTime hora, ArrayList<Entrada> listEntradasFuncion, SalaCine theSalaCine, Pelicula thePeliculaFuncion) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.listEntradasFuncion = listEntradasFuncion;
        this.theSalaCine = theSalaCine;
        this.thePeliculaFuncion = thePeliculaFuncion;
        this.listCuentasSuscritas = new ArrayList<>();
    }

    /**
     * @return fecha y hora en que inicia la funcion
     */
    public LocalDateTime getInicio() {
        return LocalDateTime.of(fecha, hora);
    }

    //Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ArrayList<Entrada> getListEntradasFuncion() {
        return listEntradasFuncion;
    }

    public SalaCine getTheSalaCine() {
        return theSalaCine;
    }

    public Pelicula getThePeliculaFuncion() {
        return thePeliculaFuncion;
    }

    public ArrayList<Cuenta> getListCuentasSuscritas() {
        return listCuentasSuscritas;
    }

    @Override
    public String toString() {
        String pelicula = thePeliculaFuncion != null ? thePeliculaFuncion.getNombre() : "Sin pelicula";
        return pelicula + " - " + fecha + " " + hora;
    }
}