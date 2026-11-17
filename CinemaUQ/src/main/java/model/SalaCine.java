package model;

import java.util.ArrayList;
import java.util.List;

public class SalaCine {
    private String idSalaCine;
    private List<Asiento> listAsientosSalaCine;
    private List<FuncionPelicula> listFuncionesPelicula;

    public SalaCine(String idSalaCine) {
        this.idSalaCine = idSalaCine;
        this.listAsientosSalaCine = new ArrayList<>();
        this.listFuncionesPelicula = new ArrayList<>();
    }

    // Métodos seguros para manejar listas (buenas prácticas)
    public void agregarAsiento(Asiento asiento) {
        if (asiento != null && !listAsientosSalaCine.contains(asiento)) {
            listAsientosSalaCine.add(asiento);
        }
    }

    public void agregarFuncion(FuncionPelicula funcion) {
        if (funcion != null && !listFuncionesPelicula.contains(funcion)) {
            listFuncionesPelicula.add(funcion);
        }
    }

    // Getters y Setters tradicionales
    public String getIdSalaCine() {
        return idSalaCine;
    }

    public void setIdSalaCine(String idSalaCine) {
        this.idSalaCine = idSalaCine;
    }

    public List<Asiento> getListAsientosSalaCine() {
        return listAsientosSalaCine;
    }

    public void setListAsientosSalaCine(List<Asiento> listAsientosSalaCine) {
        this.listAsientosSalaCine = listAsientosSalaCine;
    }

    public List<FuncionPelicula> getListFuncionesPelicula() {
        return listFuncionesPelicula;
    }

    public void setListFuncionesPelicula(List<FuncionPelicula> listFuncionesPelicula) {
        this.listFuncionesPelicula = listFuncionesPelicula;
    }

    @Override
    public String toString() {
        return "Sala " + idSalaCine;
    }
}