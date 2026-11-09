package model;

public class Asiento {
    private String idAsiento;
    private boolean disponible;
    private SalaCine theSalaCine;

    Asiento(String idAsiento, boolean disponible, SalaCine theSalaCine) {
        this.idAsiento = idAsiento;
        this.disponible = disponible;
        this.theSalaCine = theSalaCine;
    }


    public String getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(String idAsiento) {
        this.idAsiento = idAsiento;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public SalaCine getTheSalaCine() {
        return theSalaCine;
    }

    public void setTheSalaCine(SalaCine theSalaCine) {
        this.theSalaCine = theSalaCine;
    }
}
