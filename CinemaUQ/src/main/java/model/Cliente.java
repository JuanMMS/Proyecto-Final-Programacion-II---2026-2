package model;

/**
 * Clase para instanciar clientes
 */
public class Cliente {
    //Atributos
    private String nombreCompleto;
    private String id;
    private String email;
    private int edad;
    private Cuenta cuenta;

    public Cliente(String nombreCompleto, String id, String email, int edad, Cuenta cuenta) {
        this.nombreCompleto = nombreCompleto;
        this.id = id;
        this.email = email;
        this.edad = edad;
        this.cuenta = cuenta;
    }
}
