package controller;

import model.Cliente;
import service.RegistroService;

/** Coordina el registro de clientes sin conocer controles ni ventanas JavaFX. */
public class RegistroController {
    private final RegistroService registroService;
    public RegistroController() { this(new RegistroService()); }
    public RegistroController(RegistroService registroService) { this.registroService = registroService; }

    public Cliente registrar(String nombre, String documento, String edadTexto, String correo,
                             String usuario, String contrasenia) {
        final int edad;
        try { edad = Integer.parseInt(edadTexto == null ? "" : edadTexto.trim()); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("La edad debe ser un número entero."); }
        return registroService.registrarCliente(nombre, documento, edad, correo, usuario, contrasenia);
    }
}
