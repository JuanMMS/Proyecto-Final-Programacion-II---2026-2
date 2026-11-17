package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;


public class CuentaPuntos {
    //Reglas de puntos
    public static final double PESOS_POR_PUNTO = 1000.0;   // cada $1000 pagados = 1 punto
    public static final double VALOR_PUNTO = 10.0;         // cada punto redimido descuenta $10

    //Atributos
    private int puntos;
    private LocalDate fechaObtencion, fechaVencimiento;
    private Cuenta theCuentaCuentaPuntos;
    private String idCompraOrigen;

    /**
     * Constructor. La fecha de vencimiento se calcula automaticamente a partir de la fecha de obtencion
     * @param puntos puntos obtenidos
     * @param fechaObtencion fecha de los puntos obtenidos
     * @param theCuentaCuentaPuntos cuenta asociada
     */
    public CuentaPuntos(int puntos, LocalDate fechaObtencion, Cuenta theCuentaCuentaPuntos) {
        this(puntos, fechaObtencion, theCuentaCuentaPuntos, null);
    }


    public CuentaPuntos(int puntos, LocalDate fechaObtencion, Cuenta theCuentaCuentaPuntos, String idCompraOrigen) {
        this.puntos = puntos;
        this.fechaObtencion = fechaObtencion;
        this.fechaVencimiento = fechaObtencion.plusYears(1);
        this.theCuentaCuentaPuntos = theCuentaCuentaPuntos;
        this.idCompraOrigen = idCompraOrigen;
    }


    public static int puntosPorValor(double valorPagado) {
        return (int) (valorPagado / PESOS_POR_PUNTO);
    }


    public boolean estaVencido(LocalDate hoy) {
        return !hoy.isBefore(fechaVencimiento);
    }

    // ---------- Acumulacion, redencion y vencimiento (trabajan sobre la lista de la cuenta) ----------


    public static void acumular(Cuenta cuenta, int cantidad, LocalDate fecha, String idCompra) {
        if (cantidad <= 0) {
            return;
        }
        cuenta.getListCuentaPuntos().add(new CuentaPuntos(cantidad, fecha, cuenta, idCompra));
    }


    public static int puntosDisponibles(Cuenta cuenta, LocalDate hoy) {
        int total = 0;
        for (CuentaPuntos lote : cuenta.getListCuentaPuntos()) {
            if (!lote.estaVencido(hoy)) {
                total += lote.getPuntos();
            }
        }
        return total;
    }


    public static double redimir(Cuenta cuenta, int cantidad, LocalDate hoy) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Los puntos a redimir deben ser mayores a cero");
        }
        if (cantidad > puntosDisponibles(cuenta, hoy)) {
            throw new IllegalStateException("Puntos insuficientes");
        }

        ArrayList<CuentaPuntos> ordenados = new ArrayList<>(cuenta.getListCuentaPuntos());
        ordenados.sort(Comparator.comparing(CuentaPuntos::getFechaVencimiento));

        int pendientes = cantidad;
        for (CuentaPuntos lote : ordenados) {
            if (pendientes == 0) {
                break;
            }
            if (lote.estaVencido(hoy)) {
                continue;
            }
            int usados = Math.min(pendientes, lote.getPuntos());
            lote.setPuntos(lote.getPuntos() - usados);
            pendientes -= usados;
        }
        cuenta.getListCuentaPuntos().removeIf(l -> l.getPuntos() == 0);
        return cantidad * VALOR_PUNTO;
    }


    public static int eliminarVencidos(Cuenta cuenta, LocalDate hoy) {
        int eliminados = 0;
        Iterator<CuentaPuntos> it = cuenta.getListCuentaPuntos().iterator();
        while (it.hasNext()) {
            CuentaPuntos lote = it.next();
            if (lote.estaVencido(hoy)) {
                eliminados += lote.getPuntos();
                it.remove();
            }
        }
        return eliminados;
    }


    public static void revertir(Cuenta cuenta, String idCompra) {
        cuenta.getListCuentaPuntos().removeIf(l -> idCompra != null && idCompra.equals(l.getIdCompraOrigen()));
    }

    //Getters y setters
    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public LocalDate getFechaObtencion() {
        return fechaObtencion;
    }

    public void setFechaObtencion(LocalDate fechaObtencion) {
        this.fechaObtencion = fechaObtencion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Cuenta getTheCuentaCuentaPuntos() {
        return theCuentaCuentaPuntos;
    }

    public void setTheCuentaCuentaPuntos(Cuenta theCuentaCuentaPuntos) {
        this.theCuentaCuentaPuntos = theCuentaCuentaPuntos;
    }

    public String getIdCompraOrigen() {
        return idCompraOrigen;
    }

    public void setIdCompraOrigen(String idCompraOrigen) {
        this.idCompraOrigen = idCompraOrigen;
    }
}