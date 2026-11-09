package model;

public class Boleta {
    private String idBoleta;

    Boleta(String idBoleta, FuncionPelicula funcionPelicula) {
        this.idBoleta = idBoleta;
    }

    public String getIdBoleta() {
        return idBoleta;
    }

    public void setIdBoleta(String idBoleta) {
        this.idBoleta = idBoleta;
    }
}
