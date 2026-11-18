package viewcontroller;

import composite.Elemento;
import decorator.ComplementoPalomita;
import decorator.ConDulce;
import decorator.ConSal;
import factory.DatosConfiteria;
import factory.GaseosaFactory;
import factory.ChocolatinaFactory;
import factory.combos.Combo1;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import model.*;
import navegacion.FlujoCompra;
import navegacion.Navegador;

import java.util.Locale;

public class ConfiteriaViewController {
    @FXML private FlowPane contenedorCombos;
    @FXML private CheckBox chkSal, chkDulce;
    @FXML private ComboBox<String> cmbTipoGaseosa, cmbTipoChocolatina;
    @FXML private ListView<Elemento> listViewCarrito;
    @FXML private Label lblTotalCarrito;
    private final FlujoCompra flujo = FlujoCompra.getInstancia();

    @FXML public void initialize() {
        cmbTipoGaseosa.getItems().setAll("PEPSI", "COCACOLA", "FANTA");
        cmbTipoChocolatina.getItems().setAll("CONLECHE", "BLANCO", "CARAMELIZADO");
        listViewCarrito.setCellFactory(v -> new ListCell<>() {
            @Override protected void updateItem(Elemento item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre() + " — " + moneda(item.getPrecio()));
            }
        });
        refrescarCarrito();
    }
    @FXML private void handleAgregarCombo1(ActionEvent e) {
        Combo1 combo = new Combo1();
        flujo.getCarrito().agregar(combo.crearGasesosa());
        flujo.getCarrito().agregar((Elemento) combo.crearPalomita());
        flujo.getCarrito().agregar(combo.crearChocolatina());
        refrescarCarrito();
    }
    @FXML private void handleAgregarPalomitas(ActionEvent e) {
        ComplementoPalomita palomitas = new Palomita("Palomitas grandes", 8000);
        if (chkSal.isSelected()) palomitas = new ConSal(palomitas);
        if (chkDulce.isSelected()) palomitas = new ConDulce(palomitas);
        flujo.getCarrito().agregar((Elemento) palomitas);
        chkSal.setSelected(false); chkDulce.setSelected(false);
        refrescarCarrito();
    }
    @FXML private void handleAgregarGaseosa(ActionEvent e) {
        if (cmbTipoGaseosa.getValue() == null) { alerta("Selecciona una gaseosa."); return; }
        TipoGaseosa tipo = TipoGaseosa.valueOf(cmbTipoGaseosa.getValue());
        double precio = 5000;
        flujo.getCarrito().agregar(new GaseosaFactory().crearConfiteria(new DatosConfiteria(tipo.name(), precio, tipo)));
        refrescarCarrito();
    }
    @FXML private void handleAgregarChocolatina(ActionEvent e) {
        if (cmbTipoChocolatina.getValue() == null) { alerta("Selecciona una chocolatina."); return; }
        TipoChocolatina tipo = TipoChocolatina.valueOf(cmbTipoChocolatina.getValue());
        flujo.getCarrito().agregar(new ChocolatinaFactory().crearConfiteria(new DatosConfiteria(tipo.name(), 4500, tipo)));
        refrescarCarrito();
    }
    @FXML private void handleIrACheckout(ActionEvent e) {
        if (flujo.getFuncion() == null || flujo.getAsientos().isEmpty()) { alerta("Primero selecciona una función y tus asientos."); return; }
        new Navegador((Stage)((javafx.scene.Node)e.getSource()).getScene().getWindow()).navegar("Checkout.fxml");
    }
    private void refrescarCarrito() {
        listViewCarrito.getItems().setAll(flujo.getCarrito().getElementos());
        lblTotalCarrito.setText(moneda(flujo.getCarrito().getPrecio()));
    }
    private String moneda(double valor) { return String.format(Locale.forLanguageTag("es-CO"), "$ %,.0f", valor); }
    private void alerta(String m) { new Alert(Alert.AlertType.WARNING, m).showAndWait(); }
}
