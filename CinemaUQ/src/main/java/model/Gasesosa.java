package model;

public class Gasesosa extends Confiteria{

    private TipoGaseosa tipoGaseosa;


    /**
     * Metodo constructor de la clase Gaseosa
     * @param nombre
     * @param precio
     * @param tipoGaseosa
     */
    public Gasesosa(String nombre, double precio, TipoGaseosa tipoGaseosa) {
        super(nombre, precio);
        this.tipoGaseosa = tipoGaseosa;
    }


    public TipoGaseosa getTipoGaseosa() {
        return tipoGaseosa;
    }

    public void setTipoGaseosa(TipoGaseosa tipoGaseosa) {
        this.tipoGaseosa = tipoGaseosa;
    }
}
