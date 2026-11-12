package model;


public class Chocolatina extends Confiteria{

    private TipoChocolatina tipoChocolatina;

    /**
     * Metodo contrsuctor d ela clase Chocolatina
     * @param nombre
     * @param precio
     * @param tipoChocolatina
     */
    public Chocolatina(String nombre, double precio, TipoChocolatina tipoChocolatina) {
        super(nombre, precio);
        this.tipoChocolatina = tipoChocolatina;
    }

    public TipoChocolatina getTipoChocolatina() {
        return tipoChocolatina;
    }

    public void setTipoChocolatina(TipoChocolatina tipoChocolatina) {
        this.tipoChocolatina = tipoChocolatina;
    }
}
