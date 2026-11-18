
package viewcontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import model.Cliente;
import model.Administrador;
import navegacion.Navegador;
import service.AutenticacionService;
import service.Sesion;

public class LoginViewController {

    @FXML private ImageView logoImageView;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasenia;
    @FXML private Button btnIniciarSesion;
    @FXML private Hyperlink linkRegistro;

    private final AutenticacionService autenticacionService =
            new AutenticacionService();

    @FXML
    void handleIniciarSesion(ActionEvent event) {

        String usuario = txtUsuario.getText().trim();
        String contrasenia = txtContrasenia.getText();

        if (usuario.isBlank() || contrasenia.isBlank()) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Campos vacíos",
                    "Ingresa tu usuario y contraseña."
            );
            return;
        }

        Object usuarioAutenticado =
                autenticacionService.autenticar(usuario, contrasenia);

        if (usuarioAutenticado == null) {
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Inicio de sesión fallido",
                    "El usuario o la contraseña son incorrectos."
            );
            return;
        }

        // Guardar el usuario que inició sesión.
        Sesion.getInstancia().iniciarSesion(usuarioAutenticado);

        Stage stage = (Stage) btnIniciarSesion
                .getScene()
                .getWindow();

        Navegador navegador = new Navegador(stage);

        if (usuarioAutenticado instanceof Administrador) {
            navegador.navegar("DashboardAdmin.fxml");

        } else if (usuarioAutenticado instanceof Cliente) {
            navegador.navegar("Cartelera.fxml");
        }
    }

    @FXML
    void handleIrARegistro(ActionEvent event) {

        Stage stage = (Stage) linkRegistro
                .getScene()
                .getWindow();

        Navegador navegador = new Navegador(stage);
        navegador.navegar("Registro.fxml");
    }

    private void mostrarAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {

        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
