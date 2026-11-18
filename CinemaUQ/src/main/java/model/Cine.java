package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal del cine, usa singleton
 */
public class Cine {
    // Instancia unica
    private static Cine instancia;

    // Atributos
    private String nombre;
    private String nit;
    private String direccion;

    // Listas del sistema
    private ArrayList<Cliente> listClientes;
    private ArrayList<Cuenta> listCuentasClientes;
    private ArrayList<Compra> listCompras;
    private ArrayList<Administrador> listAdministradores;
    private ArrayList<Confiteria> listConfiterias;
    private ArrayList<SalaCine> listSalasCine;

    // LISTA CRÍTICA AGREGADA PARA LA CARTELERA (Integrante 2)
    private ArrayList<Pelicula> listPeliculas;
    private ArrayList<Promocion> listPromociones;

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
        this.listPeliculas = new ArrayList<>();
        this.listPromociones = new ArrayList<>();
    }

    /**
     * Funcion para devolver la unica instancia de la clase
     */
    public static Cine getInstancia() {
        if (instancia == null) {
            instancia = new Cine();
        }
        return instancia;
    }

    // ==========================================
    // MÉTODOS PARA LA CARTELERA (Integrante 2)
    // ==========================================

    public void agregarPelicula(Pelicula pelicula) {
        if (pelicula != null && !listPeliculas.contains(pelicula)) {
            listPeliculas.add(pelicula);
        }
    }

    public ArrayList<Pelicula> obtenerCartelera() {
        return this.listPeliculas;
    }

    public void agregarSala(SalaCine sala) {
        if (sala != null && !listSalasCine.contains(sala)) {
            listSalasCine.add(sala);
        }
    }

    // ==========================================
    // GETTERS Y SETTERS TRADICIONALES
    // ==========================================

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public ArrayList<Cliente> getListClientes() { return listClientes; }
    public void setListClientes(ArrayList<Cliente> listClientes) { this.listClientes = listClientes; }

    public ArrayList<Cuenta> getListCuentasClientes() { return listCuentasClientes; }
    public void setListCuentasClientes(ArrayList<Cuenta> listCuentasClientes) { this.listCuentasClientes = listCuentasClientes; }

    public ArrayList<Compra> getListCompras() { return listCompras; }
    public void setListCompras(ArrayList<Compra> listCompras) { this.listCompras = listCompras; }

    public ArrayList<Administrador> getListAdministradores() { return listAdministradores; }
    public void setListAdministradores(ArrayList<Administrador> listAdministradores) { this.listAdministradores = listAdministradores; }

    public ArrayList<Confiteria> getListConfiterias() { return listConfiterias; }
    public void setListConfiterias(ArrayList<Confiteria> listConfiterias) { this.listConfiterias = listConfiterias; }

    public ArrayList<SalaCine> getListSalasCine() { return listSalasCine; }
    public void setListSalasCine(ArrayList<SalaCine> listSalasCine) { this.listSalasCine = listSalasCine; }

    public ArrayList<Promocion> getListPromociones() { return listPromociones; }
    public ArrayList<Pelicula> getListPeliculas() { return listPeliculas; }
    public void setListPeliculas(ArrayList<Pelicula> listPeliculas) { this.listPeliculas = listPeliculas; }
}