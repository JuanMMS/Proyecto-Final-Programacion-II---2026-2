package viewcontroller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import navegacion.Navegador;
import service.Sesion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PerfilClienteViewController {
    @FXML private Label lblSaldoTarjeta, lblIdTarjeta, lblTotalPuntosVigentes;
    @FXML private TextField txtMontoRecarga;
    @FXML private TableView<Compra> tablaCompras;
    @FXML private TableView<MovimientoTarjeta> tablaMovimientos;
    @FXML private TableView<CuentaPuntos> tablaPuntos;
    @FXML private TableColumn<MovimientoTarjeta,String> colFechaMov, colTipoMov;
    @FXML private TableColumn<MovimientoTarjeta,Number> colMontoMov;
    @FXML private TableColumn<Compra,String> colIdCompra, colFechaCompra, colDetalleCompra;
    @FXML private TableColumn<Compra,Number> colTotalCompra;
    @FXML private TableColumn<CuentaPuntos,Number> colCantidadPuntos;
    @FXML private TableColumn<CuentaPuntos,String> colFechaObtencion, colFechaVencimiento, colEstadoPuntos;
    private Cliente cliente;
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML public void initialize() {
        Object actual = Sesion.getInstancia().getUsuarioActual();
        if (!(actual instanceof Cliente c)) {
            mostrarVacio("Inicia sesión como cliente para consultar tu perfil.");
            return;
        }
        cliente = c;
        configurarColumnas();
        refrescarDatos();
    }

    private void configurarColumnas() {
        colFechaMov.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getFecha().format(FECHA_HORA)));
        colTipoMov.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getTipo() + " — " + v.getValue().getDescripcion()));
        colMontoMov.setCellValueFactory(v -> new SimpleDoubleProperty(v.getValue().getMonto()));
        colMontoMov.setCellFactory(col -> monedaCell());
        colIdCompra.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getId()));
        colFechaCompra.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getFecha() + " " + v.getValue().getHora()));
        colDetalleCompra.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().getTheCompraCarrito() == null ? "—" :
                v.getValue().getTheCompraCarrito().getElementos().stream().map(Elemento -> Elemento.getNombre()).reduce((a,b) -> a + ", " + b).orElse("Sin detalle")));
        colTotalCompra.setCellValueFactory(v -> new SimpleDoubleProperty(v.getValue().getValorPagado()));
        colTotalCompra.setCellFactory(col -> monedaCell());
        colCantidadPuntos.setCellValueFactory(v -> new SimpleIntegerProperty(v.getValue().getPuntos()));
        colFechaObtencion.setCellValueFactory(v -> new SimpleStringProperty(String.valueOf(v.getValue().getFechaObtencion())));
        colFechaVencimiento.setCellValueFactory(v -> new SimpleStringProperty(String.valueOf(v.getValue().getFechaVencimiento())));
        colEstadoPuntos.setCellValueFactory(v -> new SimpleStringProperty(v.getValue().estaVencido(LocalDate.now()) ? "Vencidos" : "Vigentes"));
        tablaMovimientos.setPlaceholder(new Label("Aún no tienes movimientos en tu tarjeta."));
        tablaCompras.setPlaceholder(new Label("Aún no has realizado compras."));
        tablaPuntos.setPlaceholder(new Label("Aún no tienes puntos registrados."));
    }

    private <T> TableCell<T, Number> monedaCell() {
        return new TableCell<>() {
            @Override protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : moneda(value.doubleValue()));
            }
        };
    }

    private void refrescarDatos() {
        if (cliente == null) return;
        Cuenta cuenta = cliente.getCuenta();
        TarjetaVirtual tarjeta = cuenta.getTheTarjetaVirtualCliente();
        lblSaldoTarjeta.setText(moneda(tarjeta.getSaldo()));
        lblIdTarjeta.setText("Tarjeta #" + tarjeta.getId());
        int puntos = CuentaPuntos.puntosDisponibles(cuenta, LocalDate.now());
        lblTotalPuntosVigentes.setText("Total Puntos Vigentes: " + puntos + " pts");
        tablaMovimientos.getItems().setAll(tarjeta.getMovimientos().stream().toList());
        tablaCompras.getItems().setAll(Cine.getInstancia().getListCompras().stream()
                .filter(x -> x.getCuenta() == cuenta).toList());
        tablaPuntos.getItems().setAll(cuenta.getListCuentaPuntos());
        tablaMovimientos.refresh(); tablaCompras.refresh(); tablaPuntos.refresh();
    }

    @FXML private void handleSolicitarRecarga(ActionEvent event) {
        if (cliente == null) return;
        try {
            double monto = Double.parseDouble(txtMontoRecarga.getText().trim());
            if (!Double.isFinite(monto) || monto <= 0) throw new NumberFormatException();
            TarjetaVirtual tarjeta = cliente.getCuenta().getTheTarjetaVirtualCliente();
            tarjeta.setSaldo(tarjeta.getSaldo() + monto);
            tarjeta.registrarMovimiento("Recarga", "Recarga de saldo solicitada por el cliente", monto);
            txtMontoRecarga.clear(); refrescarDatos();
            alerta(Alert.AlertType.INFORMATION, "Recarga aplicada correctamente.");
        } catch (NumberFormatException ex) { alerta(Alert.AlertType.WARNING, "Ingresa un monto numérico mayor que cero."); }
    }

    @FXML private void handleSolicitarReembolso(ActionEvent event) {
        Compra compra = tablaCompras.getSelectionModel().getSelectedItem();
        if (compra == null) { alerta(Alert.AlertType.WARNING, "Selecciona una compra."); return; }
        try {
            double monto = compra.cancelar(java.time.LocalDateTime.now());
            refrescarDatos();
            alerta(Alert.AlertType.INFORMATION, "Compra cancelada. Reembolso: " + moneda(monto));
        } catch (RuntimeException ex) { alerta(Alert.AlertType.ERROR, ex.getMessage()); }
    }

    @FXML private void handleVolverCartelera(ActionEvent event) {
        new Navegador((Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow()).navegar("Cartelera.fxml");
    }
    private void mostrarVacio(String texto) {
        if (tablaCompras != null) tablaCompras.setPlaceholder(new Label(texto));
        if (tablaMovimientos != null) tablaMovimientos.setPlaceholder(new Label(texto));
        if (tablaPuntos != null) tablaPuntos.setPlaceholder(new Label(texto));
    }
    private void alerta(Alert.AlertType tipo, String mensaje) { new Alert(tipo, mensaje).showAndWait(); }
    private String moneda(double n) { return String.format(Locale.forLanguageTag("es-CO"), "$ %,.0f", n); }
}
