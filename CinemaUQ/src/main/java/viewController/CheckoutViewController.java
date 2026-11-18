package viewController;

import composite.Elemento;
import fachada.CompraFachada;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import navegacion.FlujoCompra;
import navegacion.Navegador;

import java.util.Locale;

public class CheckoutViewController {
    @FXML private TextArea txtDetalleOrden;
    @FXML private Label lblTotalPagar, lblPuntosGanados, lblSaldoDisponible, lblPuntosDisponibles, lblCostoEnPuntos;
    @FXML private RadioButton rbTarjetaVirtual, rbPuntos;
    @FXML private ToggleGroup grupoPago;
    @FXML private PasswordField txtContraseniaConfirmacion;
    @FXML private Button btnConfirmarPago;
    private final FlujoCompra flujo = FlujoCompra.getInstancia();
    private Cuenta cuenta;
    private double total;

    @FXML public void initialize() {
        cuenta = flujo.getCuentaActual();
        if (cuenta == null) { btnConfirmarPago.setDisable(true); alerta(Alert.AlertType.WARNING, "Inicia sesión como cliente para pagar."); return; }
        if (flujo.getFuncion() == null || flujo.getAsientos().isEmpty()) { btnConfirmarPago.setDisable(true); alerta(Alert.AlertType.WARNING, "No hay función o asientos seleccionados."); return; }
        // Añadir entradas una sola vez al carrito del flujo.
        for (Asiento asiento : flujo.getAsientos()) {
            boolean existe = flujo.getCarrito().getElementos().stream().anyMatch(x -> x instanceof Entrada en && en.getElAsiento().equals(asiento));
            if (!existe) flujo.getCarrito().agregar(new Entrada("ENT-" + asiento.getIdAsiento(), 12000, asiento, flujo.getFuncion()));
        }
        StringBuilder detalle = new StringBuilder();
        for (Elemento item : flujo.getCarrito().getElementos()) {
            detalle.append("• ").append(item.getNombre()).append(" — ").append(moneda(item.getPrecio())).append('\n');
        }
        total = flujo.getCarrito().getPrecio();
        txtDetalleOrden.setText(detalle.toString());
        lblTotalPagar.setText(moneda(total));
        lblSaldoDisponible.setText("Saldo disponible: " + moneda(cuenta.getTheTarjetaVirtualCliente().getSaldo()));
        int puntos = CuentaPuntos.puntosDisponibles(cuenta, java.time.LocalDate.now());
        lblPuntosDisponibles.setText("Puntos vigentes: " + puntos + " pts");
        lblCostoEnPuntos.setText("Equivalencia: " + (int)Math.ceil(total / CuentaPuntos.VALOR_PUNTO) + " pts");
        lblPuntosGanados.setText("Con esta compra ganarás: " + CuentaPuntos.puntosPorValor(total) + " pts ⭐");
        rbPuntos.setDisable(true); // Redención no está conectada a una operación transaccional de pago en este proyecto.
    }
    @FXML private void handleConfirmarPago(ActionEvent event) {
        if (!rbTarjetaVirtual.isSelected()) { alerta(Alert.AlertType.INFORMATION, "Selecciona tarjeta virtual para continuar."); return; }
        try {
            CompraFachada fachada = new CompraFachada(cuenta, txtContraseniaConfirmacion.getText(), flujo.getCarrito(), total, flujo.getFuncion());
            fachada.hacerCompra();
            int puntosGanados = CuentaPuntos.puntosPorValor(total);
            CuentaPuntos.acumular(cuenta, puntosGanados, java.time.LocalDate.now(), "COMPRA-" + Cine.getInstancia().getListCompras().size());
            Alert confirmacion = new Alert(Alert.AlertType.INFORMATION);
            confirmacion.setTitle("Compra realizada");
            confirmacion.setHeaderText("¡Tu pago fue aprobado!");
            confirmacion.setContentText("Total: " + moneda(total));
            confirmacion.showAndWait();
            flujo.reiniciar();
            new Navegador((Stage)btnConfirmarPago.getScene().getWindow()).navegar("Cartelera.fxml");
        } catch (RuntimeException ex) { alerta(Alert.AlertType.ERROR, ex.getMessage()); }
    }
    @FXML private void handleCancelar(ActionEvent event) {
        String destino = flujo.getAsientos().isEmpty() ? "Cartelera.fxml" : "Confiteria.fxml";
        new Navegador((Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow()).navegar(destino);
    }
    private String moneda(double valor) { return String.format(Locale.forLanguageTag("es-CO"), "$ %,.0f", valor); }
    private void alerta(Alert.AlertType tipo, String mensaje) { new Alert(tipo, mensaje).showAndWait(); }
}
