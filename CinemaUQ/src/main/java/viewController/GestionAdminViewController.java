package viewcontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import navegacion.Navegador;
import service.Sesion;

import java.time.LocalTime;
import java.util.UUID;

public class GestionAdminViewController {
    @FXML private TextField txtTituloPeli, txtDirectorPeli, txtHoraFuncion;
    @FXML private ComboBox<Pelicula> cmbPelicula;
    @FXML private ComboBox<SalaCine> cmbSala;
    @FXML private DatePicker dpFechaFuncion;
    @FXML private TableView<Pelicula> tablaPeliculas;
    @FXML private TableView<FuncionPelicula> tablaFunciones;

    @FXML public void initialize() {
        tablaPeliculas.getColumns().clear();
        TableColumn<Pelicula,String> titulo = new TableColumn<>("Película");
        titulo.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Pelicula,String> director = new TableColumn<>("Director");
        director.setCellValueFactory(new PropertyValueFactory<>("director"));
        tablaPeliculas.getColumns().addAll(titulo,director);
        tablaFunciones.getColumns().clear();
        TableColumn<FuncionPelicula,String> peli = new TableColumn<>("Película");
        peli.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getThePeliculaFuncion().getNombre()));
        TableColumn<FuncionPelicula,String> fecha = new TableColumn<>("Fecha");
        fecha.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFecha().toString()));
        TableColumn<FuncionPelicula,String> hora = new TableColumn<>("Hora");
        hora.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getHora().toString()));
        TableColumn<FuncionPelicula,String> sala = new TableColumn<>("Sala");
        sala.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTheSalaCine().getIdSalaCine()));
        tablaFunciones.getColumns().addAll(peli,fecha,hora,sala);
        refrescar();
    }
    private void refrescar() {
        tablaPeliculas.getItems().setAll(Cine.getInstancia().getListPeliculas());
        cmbPelicula.getItems().setAll(Cine.getInstancia().getListPeliculas());
        cmbSala.getItems().setAll(Cine.getInstancia().getListSalasCine());
        tablaFunciones.getItems().clear();
        Cine.getInstancia().getListSalasCine().forEach(s -> tablaFunciones.getItems().addAll(s.getListFuncionesPelicula()));
    }
    @FXML private void volverDashboard(ActionEvent e) { navegar(e,"DashboardAdmin.fxml"); }
    @FXML private void cerrarSesion(ActionEvent e) { Sesion.getInstancia().cerrarSesion(); navegar(e,"Login.fxml"); }
    @FXML private void guardarPelicula(ActionEvent e) {
        String titulo = txtTituloPeli.getText() == null ? "" : txtTituloPeli.getText().trim();
        String director = txtDirectorPeli.getText() == null ? "" : txtDirectorPeli.getText().trim();
        if (titulo.isBlank() || director.isBlank()) { alerta("Completa título y director."); return; }
        boolean existe = Cine.getInstancia().getListPeliculas().stream().anyMatch(p -> p.getNombre().equalsIgnoreCase(titulo));
        if (existe) { alerta("Ya existe una película con ese título."); return; }
        Cine.getInstancia().agregarPelicula(new Pelicula(UUID.randomUUID().toString(),titulo,director));
        txtTituloPeli.clear(); txtDirectorPeli.clear(); refrescar();
    }
    @FXML private void eliminarPelicula(ActionEvent e) {
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) { alerta("Selecciona una película."); return; }
        if (!seleccionada.getListFuncionesPelicula().isEmpty()) { alerta("No puedes eliminar una película que tiene funciones programadas."); return; }
        Cine.getInstancia().getListPeliculas().remove(seleccionada); refrescar();
    }
    @FXML private void programarFuncion(ActionEvent e) {
        Pelicula pelicula = cmbPelicula.getValue(); SalaCine sala = cmbSala.getValue();
        if (pelicula == null || sala == null || dpFechaFuncion.getValue() == null) { alerta("Selecciona película, sala y fecha."); return; }
        try {
            LocalTime hora = LocalTime.parse(txtHoraFuncion.getText().trim());
            FuncionPelicula funcion = new FuncionPelicula(UUID.randomUUID().toString(),dpFechaFuncion.getValue(),hora,sala,pelicula);
            pelicula.agregarFuncion(funcion); sala.agregarFuncion(funcion); refrescar();
        } catch (RuntimeException ex) { alerta("La hora debe tener formato HH:mm (ej. 19:30)."); }
    }
    @FXML private void cancelarFuncion(ActionEvent e) {
        FuncionPelicula funcion = tablaFunciones.getSelectionModel().getSelectedItem();
        if (funcion == null) { alerta("Selecciona una función."); return; }
        funcion.notificarObservadores("La función " + funcion + " fue cancelada por el cine.");
        funcion.getTheSalaCine().getListFuncionesPelicula().remove(funcion);
        funcion.getThePeliculaFuncion().getListFuncionesPelicula().remove(funcion);
        refrescar();
        alerta("Función cancelada. Se notificó a los observadores registrados.");
    }
    private void navegar(ActionEvent e,String fxml) { new Navegador((Stage)((Node)e.getSource()).getScene().getWindow()).navegar(fxml); }
    private void alerta(String m) { new Alert(Alert.AlertType.INFORMATION,m).showAndWait(); }
}
