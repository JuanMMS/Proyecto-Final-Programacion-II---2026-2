
package service;

/**
 * Clase para distinguir si un usario es un cliente o un administrador a la hora de iniciar sesion
 */
public class Sesion {

    private static Sesion instancia;
    private Object usuarioActual;

    private Sesion() {
    }

    public static Sesion getInstancia() {
        if (instancia == null) {
            instancia = new Sesion();
        }
        return instancia;
    }

    public void iniciarSesion(Object usuario) {
        this.usuarioActual = usuario;
    }

    public Object getUsuarioActual() {
        return usuarioActual;
    }

    public void cerrarSesion() {
        usuarioActual = null;
    }

    public boolean haySesionActiva() {
        return usuarioActual != null;
    }
}
