package ViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class AsientosViewController {

    @FXML private Label lblPelicula;
    @FXML private Label lblFechaHora;
    @FXML private Label lblSala;
    @FXML private GridPane gridAsientos;
    @FXML private FlowPane flowAsientosSeleccionados;
    @FXML private Label lblSubtotal;
    @FXML private Label lblTotal;
    @FXML private Button btnContinuar;

    @FXML
    public void initialize() {
        // Generar botones de asientos según la SalaCine asociada
    }

    @FXML
    void handleContinuarPago(ActionEvent event) {
        // Validar que haya asientos seleccionados y pasar al paso de Confiteria
    }

    @FXML
    void handleVolver(ActionEvent event) {
        // Regresar a la cartelera
    }
}