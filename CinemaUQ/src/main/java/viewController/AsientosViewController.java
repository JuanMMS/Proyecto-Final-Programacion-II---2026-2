package viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.*;
import navegacion.FlujoCompra;
import navegacion.Navegador;

import java.util.ArrayList;
import java.util.List;

public class AsientosViewController {
    @FXML private Label lblPelicula, lblFechaHora, lblSala, lblSubtotal, lblTotal;
    @FXML private GridPane gridAsientos;
    @FXML private FlowPane flowAsientosSeleccionados;
    @FXML private Button btnContinuar;
    private final FlujoCompra flujo = FlujoCompra.getInstancia();
    private static final double PRECIO_ENTRADA = 12000.0;

    @FXML public void initialize() {
        FuncionPelicula funcion = flujo.getFuncion();
        if (funcion == null) { btnContinuar.setDisable(true); alerta("No hay una función seleccionada. Regresa a cartelera."); return; }
        lblPelicula.setText("🎬 " + funcion.getThePeliculaFuncion().getNombre());
        lblFechaHora.setText("📅 " + funcion.getFecha() + " - " + funcion.getHora());
        lblSala.setText("🚪 Sala " + funcion.getTheSalaCine().getIdSalaCine());
        gridAsientos.getChildren().clear();
        List<Asiento> asientos = funcion.getTheSalaCine().getListAsientosSalaCine();
        for (int i = 0; i < asientos.size(); i++) {
            Asiento asiento = asientos.get(i);
            ToggleButton boton = new ToggleButton(asiento.getIdAsiento());
            boton.setPrefWidth(48);
            boolean ocupado = funcion.isAsientoOcupado(asiento);
            boton.setDisable(ocupado);
            boton.setStyle(ocupado ? "-fx-background-color: #555;" : "-fx-background-color: #3b2e5a; -fx-text-fill: white;");
            boton.setOnAction(e -> { flujo.alternarAsiento(asiento); actualizarResumen(); });
            gridAsientos.add(boton, i % 8, i / 8);
        }
        actualizarResumen();
    }
    private void actualizarResumen() {
        flowAsientosSeleccionados.getChildren().clear();
        for (Asiento asiento : flujo.getAsientos()) flowAsientosSeleccionados.getChildren().add(new Label(asiento.getIdAsiento()));
        String valor = String.format("$ %.0f", flujo.getAsientos().size() * PRECIO_ENTRADA);
        lblSubtotal.setText(valor); lblTotal.setText(valor);
        btnContinuar.setDisable(flujo.getAsientos().isEmpty());
    }
    @FXML private void handleContinuarPago(ActionEvent event) {
        if (flujo.getAsientos().isEmpty()) { alerta("Selecciona al menos un asiento."); return; }
        new Navegador((Stage)btnContinuar.getScene().getWindow()).navegar("Confiteria.fxml");
    }
    @FXML private void handleVolver(ActionEvent event) { new Navegador((Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow()).navegar("Cartelera.fxml"); }
    private void alerta(String m) { new Alert(Alert.AlertType.WARNING, m).showAndWait(); }
}
