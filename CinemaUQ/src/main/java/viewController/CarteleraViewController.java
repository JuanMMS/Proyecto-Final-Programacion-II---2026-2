package viewcontroller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import model.*;
import navegacion.FlujoCompra;
import navegacion.Navegador;
import service.Sesion;
import controller.CarteleraController;

import java.util.List;

public class CarteleraViewController {
    private final CarteleraController carteleraController = new CarteleraController();
    @FXML private Label lblPuntos;
    @FXML private Label lblUsuario;
    @FXML private FlowPane contenedorPeliculas;

    @FXML public void initialize() {
        Cliente cliente = carteleraController.obtenerClienteActual();
        if (cliente != null) {
            lblUsuario.setText("Hola, " + cliente.getNombreCompleto());
            lblPuntos.setText("Mis Puntos: " + carteleraController.obtenerPuntosDisponibles(cliente) + " pts ⭐");
        }
        contenedorPeliculas.getChildren().clear();
        List<Pelicula> peliculas = carteleraController.obtenerPeliculas();
        for (Pelicula pelicula : peliculas) {
            VBox tarjeta = new VBox(10);
            tarjeta.getStyleClass().add("movie-card");
            tarjeta.setPrefWidth(220);
            Label titulo = new Label(pelicula.getNombre());
            titulo.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            Label director = new Label("Director: " + pelicula.getDirector());
            director.getStyleClass().add("subtitle-text");
            ComboBox<FuncionPelicula> funciones = new ComboBox<>();
            funciones.getItems().setAll(pelicula.getListFuncionesPelicula());
            funciones.setPromptText("Selecciona una función");
            Button seleccionar = new Button("Seleccionar función");
            seleccionar.getStyleClass().add("button-primary");
            seleccionar.setMaxWidth(Double.MAX_VALUE);
            seleccionar.setOnAction(e -> {
                FuncionPelicula funcion = funciones.getValue();
                try { carteleraController.seleccionarFuncion(funcion); }
                catch (IllegalArgumentException ex) { alerta(ex.getMessage()); return; }
                navegar(seleccionar, "SeleccionAsientos.fxml");
            });
            tarjeta.getChildren().addAll(titulo, director, funciones, seleccionar);
            contenedorPeliculas.getChildren().add(tarjeta);
        }
    }
    @FXML private void handleIrPerfil(javafx.event.ActionEvent e) { navegar((javafx.scene.Node)e.getSource(), "PerfilCliente.fxml"); }
    @FXML private void handleCerrarSesion(javafx.event.ActionEvent e) {
        carteleraController.cerrarSesion();
        navegar((javafx.scene.Node)e.getSource(), "Login.fxml");
    }
    private void navegar(javafx.scene.Node nodo, String fxml) { new Navegador((javafx.stage.Stage)nodo.getScene().getWindow()).navegar(fxml); }
    private void alerta(String mensaje) { new Alert(Alert.AlertType.INFORMATION, mensaje).showAndWait(); }
}
