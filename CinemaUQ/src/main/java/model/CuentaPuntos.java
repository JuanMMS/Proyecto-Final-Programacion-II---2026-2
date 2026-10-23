package model;

import java.time.LocalDate;

/**
 * Clase de puntos con sus respectivas fechas
 */
public class CuentaPuntos {
    //Atributos
    private int puntos;
    private LocalDate fechaObtencion, fechaVencimiento;
    private Cuenta theCuentaCuentaPuntos;

    /**
     * Constructor. La fecha de vencimiento se calcula automaticamente a partir de la fecha de obtencion
     * @param puntos puntos obtenidos
     * @param fechaObtencion fecha de los puntos obtenidos
     * @param theCuentaCuentaPuntos cuenta asociada
     */
    public CuentaPuntos(int puntos, LocalDate fechaObtencion, Cuenta theCuentaCuentaPuntos) {
        this.puntos = puntos;
        this.fechaObtencion = fechaObtencion;
        this.fechaVencimiento = fechaObtencion.plusYears(1);
        this.theCuentaCuentaPuntos = theCuentaCuentaPuntos;
    }
}
