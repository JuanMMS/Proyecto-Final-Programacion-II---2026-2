package viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class LoginViewController {

    @FXML private ImageView logoImageView;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasenia;
    @FXML private Button btnIniciarSesion;
    @FXML private Hyperlink linkRegistro;

    @FXML
    void handleIniciarSesion(ActionEvent event) {
        String usuario = txtUsuario.getText();
        String contrasenia = txtContrasenia.getText();
        // Llamar a LoginController (Lógica)
        mostrarAlerta(Alert.AlertType.INFORMATION, "Login", "Intentando iniciar sesión con: " + usuario);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}