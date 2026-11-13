package model;

import fachada.Fachada;

import java.util.ArrayList;

/**
 * Clase de cuenta de cada cliente
 */
public class Cuenta implements Fachada {
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

    /**
     * Funcion del patron fachada
     */
    @Override
    public void hacerCompra() {

    }

    //Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public CuentaPuntos getPuntos() {
        return puntos;
    }

    public void setPuntos(CuentaPuntos puntos) {
        this.puntos = puntos;
    }

    public ArrayList<CuentaPuntos> getListCuentaPuntos() {
        return listCuentaPuntos;
    }

    public void setListCuentaPuntos(ArrayList<CuentaPuntos> listCuentaPuntos) {
        this.listCuentaPuntos = listCuentaPuntos;
    }

    public TarjetaVirtual getTheTarjetaVirtualCliente() {
        return theTarjetaVirtualCliente;
    }

    public void setTheTarjetaVirtualCliente(TarjetaVirtual theTarjetaVirtualCliente) {
        this.theTarjetaVirtualCliente = theTarjetaVirtualCliente;
    }

    public ArrayList<FuncionPelicula> getListFuncionesPelicula() {
        return listFuncionesPelicula;
    }

    public void setListFuncionesPelicula(ArrayList<FuncionPelicula> listFuncionesPelicula) {
        this.listFuncionesPelicula = listFuncionesPelicula;
    }
}
