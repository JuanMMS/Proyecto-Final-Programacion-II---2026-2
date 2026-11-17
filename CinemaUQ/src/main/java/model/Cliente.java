package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Cliente {
    //Atributos
    private String nombreCompleto;
    private String id;
    private String email;
    private int edad;
    private Cuenta cuenta;

    /**
     * Constructor basico
     * @param nombreCompleto
     * @param id
     * @param email
     * @param edad
     * @param cuenta cuenta asociada al clietne
     */
    public Cliente(String nombreCompleto, String id, String email, int edad, Cuenta cuenta) {
        this.nombreCompleto = nombreCompleto;
        this.id = id;
        this.email = email;
        this.edad = edad;
        this.cuenta = cuenta;
    }
    /**
     * @return historial de compras del cliente, de la mas reciente a la mas antigua
     */
    public List<Compra> getHistorialCompras() {
        List<Compra> historial = new ArrayList<>();
        for (Compra compra : Cine.getInstancia().getListCompras()) {
            if (compra.getCuenta() == cuenta) {
                historial.add(compra);
            }
        }
        historial.sort(Comparator.comparing(Compra::getFecha).thenComparing(Compra::getHora).reversed());
        return historial;
    }

    /**
     * @return puntos vigentes de la cuenta del cliente
     */
    public int getPuntosDisponibles() {
        return CuentaPuntos.puntosDisponibles(cuenta, LocalDate.now());
    }

    /**
     * Cancela una compra propia segun la politica de reembolso
     * @return monto reembolsado
     */
    public double cancelarCompra(Compra compra) {
        if (compra == null || compra.getCuenta() != cuenta) {
            throw new IllegalArgumentException("La compra no pertenece a este cliente");
        }
        return compra.cancelar(LocalDateTime.now());
    }

    //Getters y setters
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
}
