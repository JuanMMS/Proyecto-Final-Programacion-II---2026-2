package viewController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import navegacion.Navegador;
import navegacion.FlujoCompra;
import service.Sesion;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class GestionAdminViewController {
    @FXML private TextField txtTituloPeli,txtDirectorPeli,txtHoraFuncion;
    @FXML private ComboBox<Pelicula> cmbPelicula;
    @FXML private ComboBox<SalaCine> cmbSala;
    @FXML private DatePicker dpFechaFuncion;
    @FXML private TableView<Pelicula> tablaPeliculas;
    @FXML private TableView<FuncionPelicula> tablaFunciones;

    @FXML public void initialize(){
        Cine cine=Cine.getInstancia();
        if(cine.getListSalasCine().isEmpty()) for(int n=1;n<=3;n++){SalaCine sala=new SalaCine(""+n); for(int f=0;f<5;f++)for(int c=1;c<=8;c++)sala.agregarAsiento(new Asiento(""+(char)('A'+f)+c,sala)); cine.agregarSala(sala);}
        cmbPelicula.setItems(FXCollections.observableArrayList(cine.getListPeliculas()));
        cmbSala.setItems(FXCollections.observableArrayList(cine.getListSalasCine()));
        configurarTablas(); refrescarTablas();
    }
    private void configurarTablas(){
        tablaPeliculas.getColumns().clear();
        TableColumn<Pelicula,String> id=new TableColumn<>("ID"); id.setCellValueFactory(v->new SimpleStringProperty(v.getValue().getIdPelicula())); id.setPrefWidth(100);
        TableColumn<Pelicula,String> titulo=new TableColumn<>("Título"); titulo.setCellValueFactory(v->new SimpleStringProperty(v.getValue().getNombre())); titulo.setPrefWidth(180);
        TableColumn<Pelicula,String> director=new TableColumn<>("Director"); director.setCellValueFactory(v->new SimpleStringProperty(v.getValue().getDirector())); director.setPrefWidth(130);
        TableColumn<Pelicula,String> funciones=new TableColumn<>("Funciones"); funciones.setCellValueFactory(v->new SimpleStringProperty(String.valueOf(v.getValue().getListFuncionesPelicula().size()))); funciones.setPrefWidth(80);
        tablaPeliculas.getColumns().addAll(id,titulo,director,funciones);
        tablaFunciones.getColumns().clear();
        TableColumn<FuncionPelicula,String> fid=new TableColumn<>("ID"); fid.setCellValueFactory(v->new SimpleStringProperty(v.getValue().getId())); fid.setPrefWidth(95);
        TableColumn<FuncionPelicula,String> fp=new TableColumn<>("Película"); fp.setCellValueFactory(v->new SimpleStringProperty(v.getValue().getThePeliculaFuncion().getNombre())); fp.setPrefWidth(140);
        TableColumn<FuncionPelicula,String> fs=new TableColumn<>("Sala"); fs.setCellValueFactory(v->new SimpleStringProperty(v.getValue().getTheSalaCine().toString())); fs.setPrefWidth(65);
        TableColumn<FuncionPelicula,String> ff=new TableColumn<>("Fecha"); ff.setCellValueFactory(v->new SimpleStringProperty(v.getValue().getFecha().toString())); ff.setPrefWidth(90);
        TableColumn<FuncionPelicula,String> fh=new TableColumn<>("Hora"); fh.setCellValueFactory(v->new SimpleStringProperty(v.getValue().getHora().toString())); fh.setPrefWidth(65);
        TableColumn<FuncionPelicula,String> fa=new TableColumn<>("Ocupados"); fa.setCellValueFactory(v->new SimpleStringProperty(String.valueOf(v.getValue().getAsientosOcupados().size()))); fa.setPrefWidth(75);
        tablaFunciones.getColumns().addAll(fid,fp,fs,ff,fh,fa);
    }
    private void refrescarTablas(){tablaPeliculas.setItems(FXCollections.observableArrayList(Cine.getInstancia().getListPeliculas()));java.util.List<FuncionPelicula> fs=Cine.getInstancia().getListPeliculas().stream().flatMap(p->p.getListFuncionesPelicula().stream()).toList();tablaFunciones.setItems(FXCollections.observableArrayList(fs));}
    @FXML private void volverDashboard(ActionEvent e){navegar("DashboardAdmin.fxml");}
    @FXML private void cerrarSesion(ActionEvent e){Sesion.getInstancia().cerrarSesion();FlujoCompra.limpiar();navegar("Login.fxml");}
    @FXML private void guardarPelicula(ActionEvent e){
        String titulo=txtTituloPeli.getText()==null?"":txtTituloPeli.getText().trim(), director=txtDirectorPeli.getText()==null?"":txtDirectorPeli.getText().trim();
        if(titulo.isBlank()||director.isBlank()){alerta("Datos incompletos","Ingresa el título y el director.");return;}
        Pelicula p=new Pelicula("PEL-"+UUID.randomUUID().toString().substring(0,6),titulo,director);Cine.getInstancia().agregarPelicula(p);cmbPelicula.getItems().add(p);txtTituloPeli.clear();txtDirectorPeli.clear();refrescarTablas();
    }
    @FXML private void eliminarPelicula(ActionEvent e){Pelicula p=tablaPeliculas.getSelectionModel().getSelectedItem();if(p==null){alerta("Selecciona película","Selecciona una película de la tabla.");return;}if(!p.getListFuncionesPelicula().isEmpty()){alerta("No se puede eliminar","La película tiene funciones programadas. Elimina o cancela primero sus funciones.");return;}Cine.getInstancia().getListPeliculas().remove(p);cmbPelicula.getItems().remove(p);refrescarTablas();}
    @FXML private void programarFuncion(ActionEvent e){
        Pelicula p=cmbPelicula.getValue();SalaCine s=cmbSala.getValue();LocalDate fecha=dpFechaFuncion.getValue();
        if(p==null||s==null||fecha==null||txtHoraFuncion.getText().isBlank()){alerta("Datos incompletos","Selecciona película, sala, fecha y hora.");return;}
        try{LocalTime hora=LocalTime.parse(txtHoraFuncion.getText().trim());if(fecha.isBefore(LocalDate.now())||(fecha.equals(LocalDate.now())&&!hora.isAfter(LocalTime.now())))throw new IllegalArgumentException("La función debe programarse en el futuro.");FuncionPelicula f=new FuncionPelicula("FUN-"+UUID.randomUUID().toString().substring(0,6),fecha,hora,s,p);p.agregarFuncion(f);s.agregarFuncion(f);refrescarTablas();alerta("Función programada","La función quedó registrada correctamente.");}
        catch(DateTimeParseException ex){alerta("Hora inválida","Usa el formato HH:mm, por ejemplo 19:30.");}catch(IllegalArgumentException ex){alerta("Fecha inválida",ex.getMessage());}
    }
    @FXML private void cancelarFuncion(ActionEvent e){FuncionPelicula f=tablaFunciones.getSelectionModel().getSelectedItem();if(f==null){alerta("Selecciona función","Selecciona una función de la tabla.");return;}Alert confirm=new Alert(Alert.AlertType.CONFIRMATION,"¿Cancelar la función seleccionada? Se notificará a los clientes.",ButtonType.YES,ButtonType.NO);if(confirm.showAndWait().orElse(ButtonType.NO)!=ButtonType.YES)return;f.notificarObservadores("La función "+f.getThePeliculaFuncion().getNombre()+" ha sido cancelada.");for(Compra c:Cine.getInstancia().getListCompras())if(c.getFuncion()==f&&!c.isCancelada()){try{c.cancelar(java.time.LocalDateTime.now());}catch(RuntimeException ignored){}}f.getThePeliculaFuncion().getListFuncionesPelicula().remove(f);f.getTheSalaCine().getListFuncionesPelicula().remove(f);refrescarTablas();}
    private void navegar(String f){new Navegador((Stage)tablaPeliculas.getScene().getWindow()).navegar(f);}
    private void alerta(String t,String m){Alert a=new Alert(Alert.AlertType.INFORMATION,t,m,ButtonType.OK);a.showAndWait();}
}
