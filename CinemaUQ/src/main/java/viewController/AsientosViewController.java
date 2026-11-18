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
    private static final double PRECIO_ENTRADA = 12000;
    @FXML private Label lblPelicula, lblFechaHora, lblSala, lblSubtotal, lblTotal;
    @FXML private GridPane gridAsientos;
    @FXML private FlowPane flowAsientosSeleccionados;
    @FXML private Button btnContinuar;
    private FuncionPelicula funcion;
    private final List<Asiento> seleccionados = new ArrayList<>();

    @FXML public void initialize() {
        funcion = FlujoCompra.getFuncionActual();
        if (funcion == null) { btnContinuar.setDisable(true); alerta("Función no seleccionada", "Regresa a cartelera y selecciona una función."); return; }
        lblPelicula.setText("🎬 " + funcion.getThePeliculaFuncion().getNombre());
        lblFechaHora.setText("📅 " + funcion.getFecha() + " - " + funcion.getHora());
        lblSala.setText("🚪 " + funcion.getTheSalaCine());
        List<Asiento> asientos = funcion.getTheSalaCine().getListAsientosSalaCine();
        if (asientos.isEmpty()) {
            for (int fila=0; fila<5; fila++) for (int col=1; col<=8; col++) funcion.getTheSalaCine().agregarAsiento(new Asiento(""+(char)('A'+fila)+col, funcion.getTheSalaCine()));
            asientos = funcion.getTheSalaCine().getListAsientosSalaCine();
        }
        gridAsientos.getChildren().clear();
        int cols=8;
        for (int i=0;i<asientos.size();i++) {
            Asiento asiento=asientos.get(i); ToggleButton b=new ToggleButton(asiento.getIdAsiento()); b.setPrefWidth(48);
            boolean ocupado=funcion.isAsientoOcupado(asiento); b.setDisable(ocupado); b.setStyle(ocupado?"-fx-background-color:#2a2a35;-fx-text-fill:#777;":"-fx-background-color:#3b2e5a;-fx-text-fill:white;");
            b.setOnAction(e -> { if (b.isSelected()) seleccionados.add(asiento); else seleccionados.remove(asiento); actualizarResumen(); });
            gridAsientos.add(b,i%cols,i/cols);
        }
        actualizarResumen();
    }
    private void actualizarResumen() {
        flowAsientosSeleccionados.getChildren().clear();
        for (Asiento a:seleccionados) { Label l=new Label(a.getIdAsiento()); l.setStyle("-fx-text-fill:white;-fx-background-color:#3b2e5a;-fx-padding:5 8;-fx-background-radius:5;"); flowAsientosSeleccionados.getChildren().add(l); }
        String total=String.format("$ %,.0f",seleccionados.size()*PRECIO_ENTRADA); lblSubtotal.setText(total); lblTotal.setText(total);
    }
    @FXML private void handleContinuarPago(ActionEvent event) {
        if (seleccionados.isEmpty()) { alerta("Selecciona asientos", "Debes seleccionar al menos un asiento."); return; }
        FlujoCompra.getCarrito().vaciar();
        for (Asiento a:seleccionados) FlujoCompra.getCarrito().agregar(new Entrada("ENT-"+funcion.getId()+"-"+a.getIdAsiento(),PRECIO_ENTRADA,a,funcion));
        FlujoCompra.getAsientos().clear(); FlujoCompra.getAsientos().addAll(seleccionados);
        new Navegador((Stage)btnContinuar.getScene().getWindow()).navegar("Confiteria.fxml");
    }
    @FXML private void handleVolver(ActionEvent event) { new Navegador((Stage)gridAsientos.getScene().getWindow()).navegar("Cartelera.fxml"); }
    private void alerta(String t,String m){Alert a=new Alert(Alert.AlertType.INFORMATION,t,m,ButtonType.OK);a.showAndWait();}
}
