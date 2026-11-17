package service;

/**
 * Interfaz para simplificar el inicio de sesion
 */
public interface Autenticable {

    String getNombreUsuario();

    /**
     * Funcion que verifica que la contraseña sea correcta
     * @param contrasenia contraseña a verificar
     * @return
     */
    boolean verificarContrasenia(String contrasenia);
}
