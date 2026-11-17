
package viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import navegacion.Navegador;
import service.RegistroService;

public class RegistroViewController {

    @FXML private TextField txtNombreCompleto;
    @FXML private TextField txtDocumento;
    @FXML private TextField txtEdad;
    @FXML private TextField txtEmail;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasenia;
    @FXML private Button btnRegistrar;
    @FXML private Hyperlink linkLogin;

    private final RegistroService registroService =
            new RegistroService();

    @FXML
    void handleRegistrar(ActionEvent event) {

        try {
            String nombre = txtNombreCompleto.getText().trim();
            String documento = txtDocumento.getText().trim();
            String correo = txtEmail.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String contrasenia = txtContrasenia.getText();

            int edad = Integer.parseInt(txtEdad.getText().trim());

            registroService.registrarCliente(
                    nombre,
                    documento,
                    edad,
                    correo,
                    usuario,
                    contrasenia
            );

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Registro exitoso",
                    "Tu cuenta fue creada correctamente. "
                            + "Ahora puedes iniciar sesión."
            );

            navegarALogin();

        } catch (NumberFormatException e) {
            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Dato inválido",
                    "La edad debe ser un número entero."
            );

        } catch (IllegalArgumentException e) {
            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "No se pudo registrar",
                    e.getMessage()
            );

        } catch (Exception e) {
            e.printStackTrace();

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Ocurrió un error al registrar el usuario."
            );
        }
    }

    @FXML
    void handleIrALogin(ActionEvent event) {
        navegarALogin();
    }

    private void navegarALogin() {
        Stage stage = (Stage) linkLogin.getScene().getWindow();
        Navegador navegador = new Navegador(stage);
        navegador.navegar("Login.fxml");
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
