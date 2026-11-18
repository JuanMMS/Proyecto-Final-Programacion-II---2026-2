package controller;

import service.AutenticacionService;
import service.Sesion;

/** Coordina el caso de uso de autenticación. No depende de JavaFX. */
public class LoginController {
    private final AutenticacionService autenticacionService;

    public LoginController() { this(new AutenticacionService()); }
    public LoginController(AutenticacionService autenticacionService) {
        this.autenticacionService = autenticacionService;
    }

    public Object iniciarSesion(String usuario, String contrasenia) {
        if (usuario == null || usuario.isBlank() || contrasenia == null || contrasenia.isBlank())
            throw new IllegalArgumentException("Ingresa tu usuario y contraseña.");
        Object autenticado = autenticacionService.autenticar(usuario.trim(), contrasenia);
        if (autenticado == null) throw new IllegalArgumentException("El usuario o la contraseña son incorrectos.");
        Sesion.getInstancia().iniciarSesion(autenticado);
        return autenticado;
    }
}
