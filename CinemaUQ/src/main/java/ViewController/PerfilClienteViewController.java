package ViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PerfilClienteViewController {

    @FXML private Label lblSaldoTarjeta;
    @FXML private Label lblIdTarjeta;
    @FXML private TextField txtMontoRecarga;
    @FXML private TableView<?> tablaMovimientos;
    @FXML private TableView<?> tablaCompras;
    @FXML private Label lblTotalPuntosVigentes;
    @FXML private TableView<?> tablaPuntos;

    @FXML
    void handleSolicitarRecarga(ActionEvent event) {
        // Registrar movimiento pendiente
    }

    @FXML
    void handleSolicitarReembolso(ActionEvent event) {
        // Validar regla de tiempo (24h/2h)
    }

    @FXML
    void handleVolverCartelera(ActionEvent event) {}
}