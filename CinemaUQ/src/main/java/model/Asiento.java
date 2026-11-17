package model;

import java.util.Objects;

public class Asiento {
    private String idAsiento; // Ej: "A1", "D7"
    private SalaCine theSalaCine;

    public Asiento(String idAsiento, SalaCine theSalaCine) {
        this.idAsiento = idAsiento;
        this.theSalaCine = theSalaCine;
    }

    public String getIdAsiento() { return idAsiento; }
    public void setIdAsiento(String idAsiento) { this.idAsiento = idAsiento; }
    public SalaCine getTheSalaCine() { return theSalaCine; }
    public void setTheSalaCine(SalaCine theSalaCine) { this.theSalaCine = theSalaCine; }

    // ==========================================
    // MÉTODOS CRÍTICOS PARA VALIDAR ASIENTOS
    // ==========================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asiento asiento = (Asiento) o;
        // Dos asientos son el mismo si tienen el mismo ID y pertenecen a la misma Sala
        return Objects.equals(idAsiento, asiento.idAsiento) &&
                Objects.equals(theSalaCine, asiento.theSalaCine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAsiento, theSalaCine);
    }

    @Override
    public String toString() { return idAsiento; }
}