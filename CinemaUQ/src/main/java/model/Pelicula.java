package model;

import java.util.ArrayList;
import java.util.List;

public class Pelicula {
    private String idPelicula;
    private String nombre;
    private String director;
    private List<FuncionPelicula> listFuncionesPelicula;

    Pelicula(String idPelicula, String nombre, String director) {
        this.idPelicula = idPelicula;
        this.nombre = nombre;
        this.director = director;
        this.listFuncionesPelicula = new ArrayList<FuncionPelicula>();
    }

    public String getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(String idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public List<FuncionPelicula> getListFuncionesPelicula() {
        return listFuncionesPelicula;
    }

    public void setListFuncionesPelicula(List<FuncionPelicula> listFuncionesPelicula) {
        this.listFuncionesPelicula = listFuncionesPelicula;
    }
}
