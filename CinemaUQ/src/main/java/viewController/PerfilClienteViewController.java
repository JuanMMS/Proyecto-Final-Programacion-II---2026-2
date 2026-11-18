package viewcontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import navegacion.Navegador;
import service.Sesion;

import java.time.LocalDate;
import java.util.Locale;

public class PerfilClienteViewController {
    @FXML private Label lblSaldoTarjeta, lblIdTarjeta, lblTotalPuntosVigentes;
    @FXML private TextField txtMontoRecarga;
    @FXML private TableView<Compra> tablaCompras;
    @FXML private TableView<?> tablaMovimientos, tablaPuntos;
    private Cliente cliente;

    @FXML public void initialize() {
        Object actual = Sesion.getInstancia().getUsuarioActual();
        if (!(actual instanceof Cliente c)) return;
        cliente = c;
        TarjetaVirtual tarjeta = c.getCuenta().getTheTarjetaVirtualCliente();
        lblSaldoTarjeta.setText(moneda(tarjeta.getSaldo()));
        lblIdTarjeta.setText("Tarjeta #" + tarjeta.getId());
        lblTotalPuntosVigentes.setText("Total Puntos Vigentes: " + CuentaPuntos.puntosDisponibles(c.getCuenta(), LocalDate.now()) + " pts");
        tablaCompras.getItems().setAll(Cine.getInstancia().getListCompras().stream().filter(x -> x.getCuenta() == c.getCuenta()).toList());
        tablaCompras.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    @FXML private void handleSolicitarRecarga(ActionEvent event) {
        if (cliente == null) return;
        try {
            double monto = Double.parseDouble(txtMontoRecarga.getText().trim());
            if (!Double.isFinite(monto) || monto <= 0) throw new NumberFormatException();
            cliente.getCuenta().getTheTarjetaVirtualCliente().setSaldo(cliente.getCuenta().getTheTarjetaVirtualCliente().getSaldo() + monto);
            lblSaldoTarjeta.setText(moneda(cliente.getCuenta().getTheTarjetaVirtualCliente().getSaldo()));
            txtMontoRecarga.clear();
            new Alert(Alert.AlertType.INFORMATION, "Recarga aplicada correctamente.").showAndWait();
        } catch (NumberFormatException ex) { new Alert(Alert.AlertType.WARNING, "Ingresa un monto numérico mayor que cero.").showAndWait(); }
    }
    @FXML private void handleSolicitarReembolso(ActionEvent event) {
        Compra compra = tablaCompras.getSelectionModel().getSelectedItem();
        if (compra == null) { new Alert(Alert.AlertType.WARNING, "Selecciona una compra.").showAndWait(); return; }
        try {
            double monto = compra.cancelar(java.time.LocalDateTime.now());
            lblSaldoTarjeta.setText(moneda(cliente.getCuenta().getTheTarjetaVirtualCliente().getSaldo()));
            tablaCompras.refresh();
            new Alert(Alert.AlertType.INFORMATION, "Compra cancelada. Reembolso: " + moneda(monto)).showAndWait();
        } catch (RuntimeException ex) { new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait(); }
    }
    @FXML private void handleVolverCartelera(ActionEvent event) {
        new Navegador((Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow()).navegar("Cartelera.fxml");
    }
    private String moneda(double n) { return String.format(Locale.forLanguageTag("es-CO"), "$ %,.0f", n); }
}
