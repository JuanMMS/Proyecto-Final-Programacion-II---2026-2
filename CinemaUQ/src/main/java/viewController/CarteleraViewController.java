package viewController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class CarteleraViewController {

    @FXML private Label lblPuntos;
    @FXML private Label lblUsuario;
    @FXML private FlowPane contenedorPeliculas;

    @FXML
    public void initialize() {
        // Llamar a CarteleraController para obtener el Iterator de Peliculas
        // y generar visualmente las tarjetas en contenedorPeliculas
    }
}