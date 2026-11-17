package viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;

public class CheckoutViewController {

    @FXML private TextArea txtDetalleOrden;
    @FXML private Label lblTotalPagar;
    @FXML private Label lblPuntosGanados;
    @FXML private RadioButton rbTarjetaVirtual;
    @FXML private ToggleGroup grupoPago;
    @FXML private Label lblSaldoDisponible;
    @FXML private PasswordField txtContraseniaConfirmacion;
    @FXML private RadioButton rbPuntos;
    @FXML private Label lblPuntosDisponibles;
    @FXML private Label lblCostoEnPuntos;
    @FXML private Button btnConfirmarPago;

    @FXML
    void handleConfirmarPago(ActionEvent event) {
        // Aquí se llama a CompraFachada y se pasa por el PagoTarjetaProxy
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        // Volver a confitería o asientos
    }
}