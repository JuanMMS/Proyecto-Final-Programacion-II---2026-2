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
import controller.ConfiteriaController;

import java.util.Locale;

public class ConfiteriaViewController {
    @FXML private FlowPane contenedorCombos;
    @FXML private CheckBox chkSal, chkDulce;
    @FXML private ComboBox<String> cmbTipoGaseosa, cmbTipoChocolatina;
    @FXML private ComboBox<Confiteria> cmbProductoInventario;
    @FXML private ListView<Elemento> listViewCarrito;
    @FXML private Label lblTotalCarrito;
    private final FlujoCompra flujo = FlujoCompra.getInstancia();
    private final ConfiteriaController confiteriaController = new ConfiteriaController();

    @FXML public void initialize() {
        cmbTipoGaseosa.getItems().setAll("PEPSI", "COCACOLA", "FANTA");
        cmbTipoChocolatina.getItems().setAll("CONLECHE", "BLANCO", "CARAMELIZADO");
        cmbProductoInventario.getItems().setAll(confiteriaController.obtenerInventario());
        listViewCarrito.setCellFactory(v -> new ListCell<>() {
            @Override protected void updateItem(Elemento item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre() + " — " + moneda(item.getPrecio()));
            }
        });
        refrescarCarrito();
    }
    @FXML private void handleAgregarCombo1(ActionEvent e) {
        confiteriaController.agregarCombo();
        refrescarCarrito();
    }
    @FXML private void handleAgregarPalomitas(ActionEvent e) {
        confiteriaController.agregarPalomitas(chkSal.isSelected(), chkDulce.isSelected());
        chkSal.setSelected(false); chkDulce.setSelected(false);
        refrescarCarrito();
    }
    @FXML private void handleAgregarGaseosa(ActionEvent e) {
        try { confiteriaController.agregarGaseosa(cmbTipoGaseosa.getValue()); }
        catch (IllegalArgumentException ex) { alerta(ex.getMessage()); return; }
        refrescarCarrito();
    }
    @FXML private void handleAgregarChocolatina(ActionEvent e) {
        try { confiteriaController.agregarChocolatina(cmbTipoChocolatina.getValue()); }
        catch (IllegalArgumentException ex) { alerta(ex.getMessage()); return; }
        refrescarCarrito();
    }
    @FXML private void handleAgregarProductoInventario(ActionEvent e) {
        Confiteria producto = cmbProductoInventario.getValue();
        try { confiteriaController.agregarProducto(producto); }
        catch (IllegalArgumentException ex) { alerta(ex.getMessage()); return; }
        refrescarCarrito();
    }
    @FXML private void handleMenuPrincipal(ActionEvent e) {
        confiteriaController.reiniciarCompra();
        new Navegador((Stage)((javafx.scene.Node)e.getSource()).getScene().getWindow()).navegar("Cartelera.fxml");
    }
    @FXML private void handleIrACheckout(ActionEvent e) {
        if (!confiteriaController.puedeIrACheckout()) { alerta("Primero selecciona una función y tus asientos."); return; }
        new Navegador((Stage)((javafx.scene.Node)e.getSource()).getScene().getWindow()).navegar("Checkout.fxml");
    }
    private void refrescarCarrito() {
        listViewCarrito.getItems().setAll(confiteriaController.obtenerCarrito());
        lblTotalCarrito.setText(moneda(confiteriaController.obtenerTotal()));
    }
    private String moneda(double valor) { return String.format(Locale.forLanguageTag("es-CO"), "$ %,.0f", valor); }
    private void alerta(String m) { new Alert(Alert.AlertType.WARNING, m).showAndWait(); }
}
