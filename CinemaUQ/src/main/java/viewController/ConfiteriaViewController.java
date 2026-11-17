package viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
// import composite.Elemento;

public class ConfiteriaViewController {

    @FXML private FlowPane contenedorCombos;
    @FXML private CheckBox chkSal;
    @FXML private CheckBox chkDulce;
    @FXML private ComboBox<String> cmbTipoGaseosa;
    @FXML private ComboBox<String> cmbTipoChocolatina;
    // @FXML private ListView<Elemento> listViewCarrito;
    @FXML private Label lblTotalCarrito;

    @FXML
    public void initialize() {
        // Configurar celdas y cargar combos
    }

    @FXML
    void handleAgregarCombo1(ActionEvent event) {
        // controller.agregarCombo1AlCarrito(carritoActual);
    }

    @FXML
    void handleAgregarPalomitas(ActionEvent event) {
        // Uso de decoradores guiado por los checkboxes
    }

    @FXML
    void handleAgregarGaseosa(ActionEvent event) {}

    @FXML
    void handleAgregarChocolatina(ActionEvent event) {}

    @FXML
    void handleIrACheckout(ActionEvent event) {
        // Ir a CheckoutViewController
    }
}