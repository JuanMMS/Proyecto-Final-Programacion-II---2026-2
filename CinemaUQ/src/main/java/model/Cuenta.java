package model;

import java.util.ArrayList;

/**
 * Clase de cuenta de cada cliente
 */
public class Cuenta {
    //Atributos
    private String id;
    private String usuario;
    private String contrasenia;
    private CuentaPuntos puntos;
    private ArrayList<CuentaPuntos> listCuentaPuntos;
    private TarjetaVirtual theTarjetaVirtualCliente;
    private ArrayList<FuncionPelicula> listFuncionesPelicula;

    /**
     * Constructor
     * @param id id
     * @param usuario usuario del cliente
     * @param contrasenia contraseña del cliente
     * @param puntos puntos totales de la cuenta
     * @param tarjetaVirtual tarjeta asociada
     */
    public Cuenta(String id, String usuario, String contrasenia, CuentaPuntos puntos, TarjetaVirtual tarjetaVirtual) {
        this.id = id;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.puntos = puntos;
        this.theTarjetaVirtualCliente = tarjetaVirtual;
        this.listCuentaPuntos = new ArrayList<>();
    }
}
