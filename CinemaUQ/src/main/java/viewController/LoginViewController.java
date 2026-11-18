
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
import controller.LoginController;

public class LoginViewController {

    @FXML private ImageView logoImageView;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasenia;
    @FXML private Button btnIniciarSesion;
    @FXML private Hyperlink linkRegistro;

    private final LoginController loginController = new LoginController();

    @FXML
    void handleIniciarSesion(ActionEvent event) {

        Object usuarioAutenticado;
        try {
            usuarioAutenticado = loginController.iniciarSesion(txtUsuario.getText(), txtContrasenia.getText());
        } catch (IllegalArgumentException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "No se pudo iniciar sesión", ex.getMessage());
            return;
        }

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
