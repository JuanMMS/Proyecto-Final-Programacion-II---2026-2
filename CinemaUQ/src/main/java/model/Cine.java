package model;

import java.util.ArrayList;

public class Cine {
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
     * Constructor
     * @param nombre
     * @param nit
     * @param direccion
     * @param listClientes
     * @param listCuentasClientes
     * @param listCompras
     * @param listAdministradores
     * @param listConfiterias
     * @param listSalasCine
     */
    public Cine(String nombre, String nit, String direccion, ArrayList<Cliente> listClientes, ArrayList<Cuenta> listCuentasClientes, ArrayList<Compra> listCompras, ArrayList<Administrador> listAdministradores, ArrayList<Confiteria> listConfiterias, ArrayList<SalaCine> listSalasCine) {
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.listClientes = listClientes;
        this.listCuentasClientes = listCuentasClientes;
        this.listCompras = listCompras;
        this.listAdministradores = listAdministradores;
        this.listConfiterias = listConfiterias;
        this.listSalasCine = listSalasCine;
    }
}
