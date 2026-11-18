package viewcontroller;

import composite.Elemento;
import fachada.CompraFachada;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import navegacion.FlujoCompra;
import navegacion.Navegador;
import controller.CheckoutController;

import java.util.Locale;

public class CheckoutViewController {
    @FXML private TextArea txtDetalleOrden;
    @FXML private Label lblTotalPagar, lblPuntosGanados, lblSaldoDisponible, lblPuntosDisponibles, lblCostoEnPuntos;
    @FXML private RadioButton rbTarjetaVirtual, rbPuntos;
    @FXML private ToggleGroup grupoPago;
    @FXML private PasswordField txtContraseniaConfirmacion;
    @FXML private Button btnConfirmarPago;
    private final FlujoCompra flujo = FlujoCompra.getInstancia();
    private final CheckoutController checkoutController = new CheckoutController();
    private Cuenta cuenta;
    private double total;

    @FXML public void initialize() {
        cuenta = checkoutController.obtenerCuenta();
        if (cuenta == null) { btnConfirmarPago.setDisable(true); alerta(Alert.AlertType.WARNING, "Inicia sesión como cliente para pagar."); return; }
        if (!checkoutController.tieneOrdenValida()) { btnConfirmarPago.setDisable(true); alerta(Alert.AlertType.WARNING, "No hay función o asientos seleccionados."); return; }
        // Añadir entradas una sola vez al carrito del flujo.
        checkoutController.prepararEntradas();
        StringBuilder detalle = new StringBuilder();
        for (Elemento item : checkoutController.obtenerElementos()) {
            detalle.append("• ").append(item.getNombre()).append(" — ").append(moneda(item.getPrecio())).append('\n');
        }
        total = checkoutController.obtenerTotal();
        txtDetalleOrden.setText(detalle.toString());
        lblTotalPagar.setText(moneda(total));
        lblSaldoDisponible.setText("Saldo disponible: " + moneda(cuenta.getTheTarjetaVirtualCliente().getSaldo()));
        int puntos = checkoutController.obtenerPuntosDisponibles();
        lblPuntosDisponibles.setText("Puntos vigentes: " + puntos + " pts");
        lblCostoEnPuntos.setText("Equivalencia: " + (int)Math.ceil(total / CuentaPuntos.VALOR_PUNTO) + " pts");
        lblPuntosGanados.setText("Con esta compra ganarás: " + CuentaPuntos.puntosPorValor(total) + " pts ⭐");
        rbPuntos.setDisable(puntos <= 0 || (int)Math.floor(total / CuentaPuntos.VALOR_PUNTO) <= 0);
        rbPuntos.setText("Redimir puntos (" + Math.min(puntos, (int)Math.floor(total / CuentaPuntos.VALOR_PUNTO)) + " pts máx.)");
    }
    @FXML private void handleConfirmarPago(ActionEvent event) {
        if (!rbTarjetaVirtual.isSelected() && !rbPuntos.isSelected()) { alerta(Alert.AlertType.INFORMATION, "Selecciona un medio de pago."); return; }
        try {
            int puntosUsar = rbPuntos.isSelected() ? checkoutController.calcularPuntosRedimibles(total) : 0;
            checkoutController.confirmarPago(txtContraseniaConfirmacion.getText(), rbPuntos.isSelected(), total);
            Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
            confirmacion.setTitle("Compra realizada");
            confirmacion.setHeaderText("¡Tu pago fue aprobado!");
            double descuento = puntosUsar * CuentaPuntos.VALOR_PUNTO;
            confirmacion.setContentText("Total: " + moneda(total - descuento) + (descuento > 0 ? "\nDescuento por puntos: " + moneda(descuento) : ""));
            confirmacion.showAndWait();
            new Navegador((Stage)btnConfirmarPago.getScene().getWindow()).navegar("Cartelera.fxml");
        } catch (RuntimeException ex) { alerta(Alert.AlertType.ERROR, ex.getMessage()); }
    }
    @FXML private void handleMenuPrincipal(ActionEvent event) {
        checkoutController.reiniciarCompra();
        new Navegador((Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow()).navegar("Cartelera.fxml");
    }
    @FXML private void handleCancelar(ActionEvent event) {
        String destino = flujo.getAsientos().isEmpty() ? "Cartelera.fxml" : "Confiteria.fxml";
        new Navegador((Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow()).navegar(destino);
    }
    private String moneda(double valor) { return String.format(Locale.forLanguageTag("es-CO"), "$ %,.0f", valor); }
    private void alerta(Alert.AlertType tipo, String mensaje) { new Alert(tipo, mensaje).showAndWait(); }
}
