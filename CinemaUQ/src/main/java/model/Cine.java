package model;

import java.util.ArrayList;

/**
 * Clase principal del cine, va a usar singleton
 */
public class Cine {
    //Instancia unica
    private static Cine instancia;

    //Atributos
    private String nombre;
    private String nit;
    private String direccion;
    private ArrayList<Cliente> listClientes;
    private ArrayList<Cuenta> listCuentasClientes;
    private ArrayList<Compra> listCompras;
    private ArrayList<Administrador> listAdministradores;
    private ArrayList<Confiteria> listConfiterias;
    private ArrayList<SalaCine> listSalasCine;

    /**
     * Constructor privado con datos por defecto
     */
    private Cine() {
        this.nombre = "CinemaUQ";
        this.nit = "123456789";
        this.direccion = "Al lado del auditorio";
        this.listClientes = new ArrayList<>();
        this.listCuentasClientes = new ArrayList<>();
        this.listCompras = new ArrayList<>();
        this.listAdministradores = new ArrayList<>();
        this.listConfiterias = new ArrayList<>();
        this.listSalasCine = new ArrayList<>();
    }

    /**
     * Funcion para devolver la unica instancia de la clase
     * @return unica instancia de la clase
     */
    public static Cine getInstancia() {
        if (instancia == null) {
            instancia = new Cine();
        }
        return instancia;
    }

    //Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public ArrayList<Cliente> getListClientes() {
        return listClientes;
    }

    public void setListClientes(ArrayList<Cliente> listClientes) {
        this.listClientes = listClientes;
    }

    public ArrayList<Cuenta> getListCuentasClientes() {
        return listCuentasClientes;
    }

    public void setListCuentasClientes(ArrayList<Cuenta> listCuentasClientes) {
        this.listCuentasClientes = listCuentasClientes;
    }

    public ArrayList<Compra> getListCompras() {
        return listCompras;
    }

    public void setListCompras(ArrayList<Compra> listCompras) {
        this.listCompras = listCompras;
    }

    public ArrayList<Administrador> getListAdministradores() {
        return listAdministradores;
    }

    public void setListAdministradores(ArrayList<Administrador> listAdministradores) {
        this.listAdministradores = listAdministradores;
    }

    public ArrayList<Confiteria> getListConfiterias() {
        return listConfiterias;
    }

    public void setListConfiterias(ArrayList<Confiteria> listConfiterias) {
        this.listConfiterias = listConfiterias;
    }

    public ArrayList<SalaCine> getListSalasCine() {
        return listSalasCine;
    }

    public void setListSalasCine(ArrayList<SalaCine> listSalasCine) {
        this.listSalasCine = listSalasCine;
    }
}
