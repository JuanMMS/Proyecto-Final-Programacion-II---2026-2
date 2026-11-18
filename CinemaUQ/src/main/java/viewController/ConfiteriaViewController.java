package viewController;

import composite.Elemento;
import decorator.*;
import factory.*;
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

    @FXML public void initialize() {
        cmbTipoGaseosa.getItems().setAll("PEPSI", "COCACOLA", "FANTA");
        cmbTipoChocolatina.getItems().setAll("CONLECHE", "BLANCO", "CARAMELIZADO");
        listViewCarrito.setCellFactory(v -> new ListCell<>() {
            @Override protected void updateItem(Elemento item, boolean empty) { super.updateItem(item,empty); setText(empty||item==null?null:item.getNombre()+"  -  "+dinero(item.getPrecio())); }
        });
        actualizarCarrito();
    }
    @FXML private void handleAgregarCombo1(ActionEvent e) {
        Combo1 combo=new Combo1(); FlujoCompra.getCarrito().agregar(combo.crearPalomita()); FlujoCompra.getCarrito().agregar(combo.crearGasesosa()); FlujoCompra.getCarrito().agregar(combo.crearChocolatina()); actualizarCarrito();
    }
    @FXML private void handleAgregarPalomitas(ActionEvent e) {
        ComplementoPalomita p=new Palomita("Palomitas grandes",8000);
        if(chkSal.isSelected()) p=new ConSal(p); if(chkDulce.isSelected()) p=new ConDulce(p);
        FlujoCompra.getCarrito().agregar(p); chkSal.setSelected(false); chkDulce.setSelected(false); actualizarCarrito();
    }
    @FXML private void handleAgregarGaseosa(ActionEvent e) {
        if(cmbTipoGaseosa.getValue()==null){alerta("Selecciona gaseosa","Elige una bebida antes de agregarla.");return;}
        TipoGaseosa tipo=TipoGaseosa.valueOf(cmbTipoGaseosa.getValue());
        FlujoCompra.getCarrito().agregar(new GaseosaFactory().crearConfiteria(new DatosConfiteria(tipo.name(),5000,tipo))); actualizarCarrito();
    }
    @FXML private void handleAgregarChocolatina(ActionEvent e) {
        if(cmbTipoChocolatina.getValue()==null){alerta("Selecciona chocolatina","Elige una chocolatina antes de agregarla.");return;}
        TipoChocolatina tipo=TipoChocolatina.valueOf(cmbTipoChocolatina.getValue());
        FlujoCompra.getCarrito().agregar(new ChocolatinaFactory().crearConfiteria(new DatosConfiteria("Chocolatina "+tipo.name().toLowerCase(Locale.ROOT),4500,tipo))); actualizarCarrito();
    }
    @FXML private void handleIrACheckout(ActionEvent e) { new Navegador((Stage)lblTotalCarrito.getScene().getWindow()).navegar("Checkout.fxml"); }
    private void actualizarCarrito(){ listViewCarrito.getItems().setAll(FlujoCompra.getCarrito().getElementos()); lblTotalCarrito.setText(dinero(FlujoCompra.getCarrito().getPrecio())); }
    private String dinero(double v){return String.format(Locale.US,"$ %,.0f",v);}
    private void alerta(String t,String m){Alert a=new Alert(Alert.AlertType.WARNING,t,ButtonType.OK);a.showAndWait();}
}
