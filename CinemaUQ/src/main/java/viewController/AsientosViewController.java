package viewcontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.*;
import navegacion.FlujoCompra;
import navegacion.Navegador;
import controller.AsientosController;

import java.util.ArrayList;
import java.util.List;

public class AsientosViewController {
    @FXML private Label lblPelicula, lblFechaHora, lblSala, lblSubtotal, lblTotal;
    @FXML private GridPane gridAsientos;
    @FXML private FlowPane flowAsientosSeleccionados;
    @FXML private Button btnContinuar;
    private final FlujoCompra flujo = FlujoCompra.getInstancia();
    private final AsientosController asientosController = new AsientosController();

    @FXML public void initialize() {
        FuncionPelicula funcion = asientosController.obtenerFuncion();
        if (funcion == null) { btnContinuar.setDisable(true); alerta("No hay una función seleccionada. Regresa a cartelera."); return; }
        lblPelicula.setText("🎬 " + funcion.getThePeliculaFuncion().getNombre());
        lblFechaHora.setText("📅 " + funcion.getFecha() + " - " + funcion.getHora());
        lblSala.setText("🚪 Sala " + funcion.getTheSalaCine().getIdSalaCine());
        gridAsientos.getChildren().clear();
        List<Asiento> asientos = asientosController.obtenerAsientos();
        for (int i = 0; i < asientos.size(); i++) {
            Asiento asiento = asientos.get(i);
            ToggleButton boton = new ToggleButton(asiento.getIdAsiento());
            boton.setPrefWidth(48);
            boolean ocupado = funcion.isAsientoOcupado(asiento);
            boton.setDisable(ocupado);
            boton.setStyle(ocupado ? "-fx-background-color: #555;" : "-fx-background-color: #3b2e5a; -fx-text-fill: white;");
            boton.setOnAction(e -> {
                asientosController.alternarSeleccion(asiento);
                actualizarEstiloAsientos();
                actualizarResumen();
            });
            gridAsientos.add(boton, i % 8, i / 8);
        }
        actualizarResumen();
    }
    private void actualizarEstiloAsientos() {
        for (javafx.scene.Node nodo : gridAsientos.getChildren()) {
            if (nodo instanceof ToggleButton boton) {
                boolean seleccionado = asientosController.obtenerSeleccionados().stream().anyMatch(a -> a.getIdAsiento().equals(boton.getText()));
                boolean ocupado = flujo.getFuncion().getAsientosOcupados().stream().anyMatch(a -> a.getIdAsiento().equals(boton.getText()));
                if (ocupado) boton.setStyle("-fx-background-color: #555; -fx-text-fill: white;");
                else if (seleccionado) boton.setStyle("-fx-background-color: #f5c542; -fx-text-fill: #171717; -fx-font-weight: bold;");
                else boton.setStyle("-fx-background-color: #3b2e5a; -fx-text-fill: white;");
                boton.setSelected(seleccionado);
            }
        }
    }

    private void actualizarResumen() {
        flowAsientosSeleccionados.getChildren().clear();
        for (Asiento asiento : asientosController.obtenerSeleccionados()) flowAsientosSeleccionados.getChildren().add(new Label(asiento.getIdAsiento()));
        String valor = String.format("$ %.0f", asientosController.calcularSubtotal());
        lblSubtotal.setText(valor); lblTotal.setText(valor);
        btnContinuar.setDisable(!asientosController.puedeContinuar());
    }
    @FXML private void handleContinuarPago(ActionEvent event) {
        if (!asientosController.puedeContinuar()) { alerta("Selecciona al menos un asiento."); return; }
        new Navegador((Stage)btnContinuar.getScene().getWindow()).navegar("Confiteria.fxml");
    }
    @FXML private void handleVolver(ActionEvent event) { new Navegador((Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow()).navegar("Cartelera.fxml"); }
    private void alerta(String m) { new Alert(Alert.AlertType.WARNING, m).showAndWait(); }
}
