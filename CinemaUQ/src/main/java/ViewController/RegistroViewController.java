package ViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistroViewController {

    @FXML private TextField txtNombreCompleto;
    @FXML private TextField txtDocumento;
    @FXML private TextField txtEdad;
    @FXML private TextField txtEmail;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasenia;
    @FXML private Button btnRegistrar;
    @FXML private Hyperlink linkLogin;

    @FXML
    void handleRegistrar(ActionEvent event) {
        // Llamar a RegistroController para crear Cliente, Cuenta, Tarjeta y Puntos
        mostrarAlerta(Alert.AlertType.INFORMATION, "Registro", "Registrando usuario...");
    }

    @FXML
    void handleIrALogin(ActionEvent event) {
        // Cambiar de escena al Login
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}