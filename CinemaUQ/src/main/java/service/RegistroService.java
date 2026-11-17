
package service;

import model.Cliente;
import model.Cuenta;
import model.CuentaPuntos;
import model.TarjetaVirtual;

import java.time.LocalDate;

/**
 * Clase para realizar el registro de un nuevo usuario
 */
public class RegistroService {

    private final GestorUsuarios gestor;

    public RegistroService() {
        this.gestor = GestorUsuarios.getInstancia();
    }

    public Cliente registrarCliente(
            String nombre,
            String documento,
            int edad,
            String correo,
            String usuario,
            String contrasenia) {

        if (nombre == null || nombre.isBlank()
                || documento == null || documento.isBlank()
                || correo == null || correo.isBlank()
                || usuario == null || usuario.isBlank()
                || contrasenia == null || contrasenia.isBlank()) {
            throw new IllegalArgumentException(
                    "Todos los campos son obligatorios.");
        }

        if (edad < 1 || edad > 120) {
            throw new IllegalArgumentException("La edad no es válida.");
        }

        if (!correo.contains("@") || !correo.contains(".")) {
            throw new IllegalArgumentException(
                    "Ingresa un correo electrónico válido.");
        }

        if (contrasenia.length() < 6) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 6 caracteres.");
        }

        if (gestor.existeUsuario(usuario)) {
            throw new IllegalArgumentException(
                    "Ese nombre de usuario ya está registrado.");
        }

        if (gestor.existeDocumento(documento)) {
            throw new IllegalArgumentException(
                    "Ese documento ya está registrado.");
        }

        if (gestor.existeCorreo(correo)) {
            throw new IllegalArgumentException(
                    "Ese correo ya está registrado.");
        }

        // Primero se crea la cuenta sin sus objetos relacionados.
        Cuenta cuenta = new Cuenta(
                documento, usuario, contrasenia, null, null);

        // Después se crean los objetos que necesitan la cuenta.
        CuentaPuntos puntos = new CuentaPuntos(
                0, LocalDate.now(), cuenta);

        TarjetaVirtual tarjeta = new TarjetaVirtual(
                documento, 0.0, cuenta);

        // Se conectan los objetos.
        cuenta.setPuntos(puntos);
        cuenta.setTheTarjetaVirtualCliente(tarjeta);

        Cliente cliente = new Cliente(
                nombre, documento, correo, edad, cuenta);

        gestor.registrarCliente(cliente);

        return cliente;
    }
}
