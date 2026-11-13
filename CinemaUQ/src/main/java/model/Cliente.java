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
