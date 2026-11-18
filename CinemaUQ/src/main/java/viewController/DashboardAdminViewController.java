package viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardAdminViewController {

    // --- Botones del Menú Lateral ---
    @FXML
    private Button btnOverview;
    @FXML
    private Button btnPeliculas;
    @FXML
    private Button btnSalas;
    @FXML
    private Button btnConfiteria;
    @FXML
    private Button btnReportes;
    @FXML
    private Button btnCerrarSesion;

    // --- Elementos del Dashboard ---
    @FXML
    private Label lblAdminName;
    @FXML
    private Label lblTotalEntradas;
    @FXML
    private Label lblTotalIngresos;
    @FXML
    private Label lblOcupacion;
    @FXML
    private Label lblReembolsos;
    @FXML
    private BarChart<?, ?> barChartPeliculas;
    @FXML
    private PieChart pieChartVentas;
    @FXML
    private LineChart<?, ?> lineChartTendencias;

    @FXML
    public void initialize() {
        // Aquí llenaremos los gráficos con datos estadísticos del Singleton Cine
    }

    // ==========================================
    // MÉTODO MAESTRO PARA CAMBIAR DE PANTALLA
    // ==========================================
    private void cambiarEscena(ActionEvent event, String archivoFxml) {
        try {
            // ¡AQUÍ ESTÁ LA MAGIA! Agregamos la ruta exacta de tu carpeta de recursos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/cinemauq/" + archivoFxml));
            Parent root = loader.load();

            // Obtener la ventana (Stage) actual a partir del botón que se hizo clic
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Poner la nueva escena en la ventana
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al intentar abrir la pantalla: " + archivoFxml);
            e.printStackTrace();
        }
    }

    @FXML
    void handleNavOverview(ActionEvent event) {
        System.out.println("Ya estás en el Overview");
    }

    @FXML
    void handleNavGestion(ActionEvent event) {
        cambiarEscena(event, "GestionAdmin.fxml");
    }

    @FXML
    void handleCerrarSesion(ActionEvent event) {
        cambiarEscena(event, "Login.fxml");
    }
}