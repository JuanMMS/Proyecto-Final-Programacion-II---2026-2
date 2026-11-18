package viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

// Si necesitas importar tus modelos luego, descomenta esto:
// import model.Pelicula;
// import model.FuncionPelicula;

public class GestionAdminViewController {

    // ==========================================
    // ELEMENTOS DE LA PESTAÑA: PELÍCULAS Y FUNCIONES
    // ==========================================

    // Formulario Película
    @FXML private TextField txtTituloPeli;
    @FXML private TextField txtDirectorPeli;

    // Formulario Función
    @FXML private ComboBox<String> cmbPelicula; // Luego lo cambiaremos a ComboBox<Pelicula>
    @FXML private ComboBox<String> cmbSala;     // Luego lo cambiaremos a ComboBox<SalaCine>
    @FXML private DatePicker dpFechaFuncion;
    @FXML private TextField txtHoraFuncion;

    // Tablas (Dejamos los genéricos <?> hasta conectar el modelo)
    @FXML private TableView<?> tablaPeliculas;
    @FXML private TableView<?> tablaFunciones;

    @FXML
    public void initialize() {
        // Aquí configuraremos las columnas de las tablas y cargaremos
        // los ComboBox con los datos del Singleton Cine.
    }

    // ==========================================
    // MÉTODOS DE LA BARRA SUPERIOR (NAVEGACIÓN)
    // ==========================================

    @FXML
    void volverDashboard(ActionEvent event) {
        cambiarEscena(event, "DashboardAdmin.fxml");
    }

    @FXML
    void cerrarSesion(ActionEvent event) {
        cambiarEscena(event, "Login.fxml");
    }

    // ==========================================
    // MÉTODOS DE LOS BOTONES (LÓGICA DEL NEGOCIO)
    // ==========================================

    @FXML
    void guardarPelicula(ActionEvent event) {
        System.out.println("Guardando película: " + txtTituloPeli.getText());
        // Aquí agregaremos la película al Singleton y refrescaremos la tabla
    }

    @FXML
    void eliminarPelicula(ActionEvent event) {
        System.out.println("Eliminando película seleccionada...");
    }

    @FXML
    void programarFuncion(ActionEvent event) {
        System.out.println("Programando función para: " + cmbPelicula.getValue());
        // Aquí crearemos la FuncionPelicula y la guardaremos en el Singleton
    }

    @FXML
    void cancelarFuncion(ActionEvent event) {
        System.out.println("Cancelando función y notificando a las cuentas...");
        // Aquí usaremos el patrón Observer (notificarObservadores) y reembolsaremos
    }

    // ==========================================
    // MÉTODO MAESTRO PARA CAMBIAR DE PANTALLA
    // ==========================================
    private void cambiarEscena(ActionEvent event, String archivoFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/cinemauq/" + archivoFxml));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar abrir la pantalla: " + archivoFxml);
            e.printStackTrace();
        }
    }
}