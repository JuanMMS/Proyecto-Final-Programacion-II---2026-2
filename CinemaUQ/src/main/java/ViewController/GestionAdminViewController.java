package ViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GestionAdminViewController {

    @FXML private TextField txtNombrePelicula;
    @FXML private TextField txtDirectorPelicula;
    @FXML private ComboBox<?> cmbPeliculaFuncion;
    @FXML private ComboBox<?> cmbSalaFuncion;
    @FXML private DatePicker dpFechaFuncion;
    @FXML private TextField txtHoraFuncion;
    @FXML private TableView<?> tablaFunciones;
    @FXML private ComboBox<?> cmbClienteRecarga;
    @FXML private TextField txtValorRecargaAdmin;
    @FXML private ComboBox<String> cmbTipoReporte;
    @FXML private DatePicker dpReporteInicio;
    @FXML private DatePicker dpReporteFin;

    @FXML
    void handleCrearPelicula(ActionEvent event) {}

    @FXML
    void handleCrearFuncion(ActionEvent event) {}

    @FXML
    void handleCancelarFuncion(ActionEvent event) {
        // Notificar por Observer a clientes y procesar reembolsos
    }

    @FXML
    void handleAcreditarRecarga(ActionEvent event) {}

    @FXML
    void handleExportarTXT(ActionEvent event) {
        // Escribir archivo texto
    }

    @FXML
    void handleExportarPDF(ActionEvent event) {
        // Escribir archivo PDF
    }

    @FXML
    void handleVolverDashboard(ActionEvent event) {}
}