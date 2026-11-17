
package service;

import model.Cliente;
import model.Administrador;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase para guardar los usuarios y contraseñas de los clientes
 */
public class GestorUsuarios {

    private static GestorUsuarios instancia;

    private final ArrayList<Cliente> clientes;
    private final ArrayList<Administrador> administradores;

    private GestorUsuarios() {
        clientes = new ArrayList<>();
        administradores = new ArrayList<>();

        // Administrador inicial para pruebas
        administradores.add(
                new Administrador(
                        "Administrador",
                        "admin",
                        "admin123"
                )
        );
    }

    public static GestorUsuarios getInstancia() {

        if (instancia == null) {
            instancia = new GestorUsuarios();
        }

        return instancia;
    }

    public void registrarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public void registrarAdministrador(Administrador administrador) {
        administradores.add(administrador);
    }

    public Cliente buscarCliente(String usuario) {

        for (Cliente cliente : clientes) {
            if (cliente.getCuenta().getUsuario()
                    .equalsIgnoreCase(usuario)) {
                return cliente;
            }
        }

        return null;
    }

    public Administrador buscarAdministrador(String usuario) {

        for (Administrador administrador : administradores) {
            if (administrador.getUsuario()
                    .equalsIgnoreCase(usuario)) {
                return administrador;
            }
        }

        return null;
    }

    public boolean existeUsuario(String usuario) {

        return buscarCliente(usuario) != null
                || buscarAdministrador(usuario) != null;
    }

    public boolean existeDocumento(String documento) {

        for (Cliente cliente : clientes) {
            if (cliente.getId().equals(documento)) {
                return true;
            }
        }

        return false;
    }

    public boolean existeCorreo(String correo) {

        for (Cliente cliente : clientes) {
            if (cliente.getEmail().equalsIgnoreCase(correo)) {
                return true;
            }
        }

        return false;
    }

    public List<Cliente> obtenerClientes() {
        return List.copyOf(clientes);
    }

    public List<Administrador> obtenerAdministradores() {
        return List.copyOf(administradores);
    }
}
