package service;

import model.Cliente;
import model.Administrador;

public class AutenticacionService {

    private final GestorUsuarios gestor;

    public AutenticacionService() {
        this.gestor = GestorUsuarios.getInstancia();
    }

    public Object autenticar(
            String usuario,
            String contrasenia) {

        Cliente cliente = gestor.buscarCliente(usuario);

        if (cliente != null &&
                cliente.verificarContrasenia(contrasenia)) {
            return cliente;
        }

        Administrador administrador =
                gestor.buscarAdministrador(usuario);

        if (administrador != null &&
                administrador.verificarContrasenia(contrasenia)) {
            return administrador;
        }

        return null;
    }
}