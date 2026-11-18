package viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import navegacion.Navegador;
<<<<<<< Updated upstream
=======
import navegacion.FlujoCompra;
import service.AutenticacionService;
import service.Sesion;
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
=======
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
        FlujoCompra.limpiar();

        Stage stage = (Stage) btnIniciarSesion
                .getScene()
                .getWindow();

        Navegador navegador = new Navegador(stage);

        if (usuarioAutenticado instanceof Administrador) {
            navegador.navegar("DashboardAdmin.fxml");

        } else if (usuarioAutenticado instanceof Cliente) {
            navegador.navegar("Cartelera.fxml");
        }
>>>>>>> Stashed changes
    }

    @FXML
    void handleIrARegistro(ActionEvent event) {

        Stage stage = (Stage) linkRegistro
                .getScene()
                .getWindow();

        Navegador navegador = new Navegador(stage);

        navegador.navegar("Registro.fxml");
    }
}