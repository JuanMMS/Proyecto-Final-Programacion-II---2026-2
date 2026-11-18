package viewController;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import model.*;
import navegacion.Navegador;
import navegacion.FlujoCompra;
import service.Sesion;
import java.time.format.DateTimeFormatter;

public class CarteleraViewController {
    @FXML private Label lblPuntos;
    @FXML private Label lblUsuario;
    @FXML private FlowPane contenedorPeliculas;

    @FXML public void initialize() {
        Object actual = Sesion.getInstancia().getUsuarioActual();
        if (!(actual instanceof Cliente cliente)) { lblUsuario.setText("Hola, invitado"); lblPuntos.setText("Inicia sesión para comprar"); }
        else {
            lblUsuario.setText("Hola, " + cliente.getNombreCompleto());
            lblPuntos.setText("Mis Puntos: " + CuentaPuntos.puntosDisponibles(cliente.getCuenta(), java.time.LocalDate.now()) + " pts ⭐");
        }
        contenedorPeliculas.getChildren().clear();
        for (Pelicula pelicula : Cine.getInstancia().obtenerCartelera()) agregarTarjeta(pelicula);
        if (contenedorPeliculas.getChildren().isEmpty()) {
            Label vacio = new Label("Aún no hay películas programadas. El administrador puede agregarlas desde Gestión.");
            vacio.setStyle("-fx-text-fill: white; -fx-font-size: 15px;");
            contenedorPeliculas.getChildren().add(vacio);
        }
    }

    private void agregarTarjeta(Pelicula pelicula) {
        VBox tarjeta = new VBox(10); tarjeta.setPadding(new Insets(15)); tarjeta.setPrefWidth(220); tarjeta.getStyleClass().add("movie-card");
        ImageView poster = new ImageView(new Image(getClass().getResourceAsStream("/uniquindio/cinemauq/images/poster_placeholder.png")));
        poster.setFitWidth(190); poster.setFitHeight(250); poster.setPreserveRatio(false);
        Label titulo = new Label(pelicula.getNombre()); titulo.setStyle("-fx-text-fill:white;-fx-font-size:16px;-fx-font-weight:bold;"); titulo.setWrapText(true);
        Label director = new Label("Director: " + pelicula.getDirector()); director.setStyle("-fx-text-fill:#a0a0a8;"); director.setWrapText(true);
        Button funciones = new Button("Ver funciones"); funciones.getStyleClass().add("button-primary"); funciones.setMaxWidth(Double.MAX_VALUE);
        funciones.setOnAction(e -> elegirFuncion(pelicula));
        tarjeta.getChildren().addAll(poster,titulo,director,funciones); contenedorPeliculas.getChildren().add(tarjeta);
    }

    private void elegirFuncion(Pelicula pelicula) {
        if (pelicula.getListFuncionesPelicula().isEmpty()) { alerta("Sin funciones", "Esta película aún no tiene funciones programadas."); return; }
        ChoiceDialog<FuncionPelicula> dialog = new ChoiceDialog<>(pelicula.getListFuncionesPelicula().get(0), pelicula.getListFuncionesPelicula());
        dialog.setTitle("Seleccionar función"); dialog.setHeaderText("Funciones para " + pelicula.getNombre()); dialog.setContentText("Elige fecha, hora y sala:");
        dialog.showAndWait().ifPresent(funcion -> {
            FlujoCompra.iniciar(funcion);
            navegar("SeleccionAsientos.fxml");
        });
    }
    @FXML private void handleIrPerfil() { navegar("PerfilCliente.fxml"); }
    private void navegar(String fxml) { new Navegador((Stage) contenedorPeliculas.getScene().getWindow()).navegar(fxml); }
    private void alerta(String t,String m) { Alert a=new Alert(Alert.AlertType.INFORMATION);a.setTitle(t);a.setHeaderText(null);a.setContentText(m);a.showAndWait(); }
}
