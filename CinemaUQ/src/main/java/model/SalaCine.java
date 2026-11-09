package model;

import java.util.ArrayList;
import java.util.List;

public class SalaCine {
    private String idSalaCine;
    private List<Asiento> listAsientosSalaCine;
    private List<FuncionPelicula> listFuncionesPelicula;

    SalaCine(String idSalaCine) {
        this.idSalaCine = idSalaCine;
        this.listAsientosSalaCine = new ArrayList<>();
        this.listFuncionesPelicula = new ArrayList<>();
    }

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
}
