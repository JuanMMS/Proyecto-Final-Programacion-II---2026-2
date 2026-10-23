package model;

import java.time.LocalDate;
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
}
