package viewController;

import composite.Elemento;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import navegacion.Navegador;
import service.Sesion;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

public class PerfilClienteViewController {
    @FXML private Label lblSaldoTarjeta,lblIdTarjeta,lblTotalPuntosVigentes;
    @FXML private TextField txtMontoRecarga;
    @FXML private TableView<Movimiento> tablaMovimientos;
    @FXML private TableColumn<Movimiento,String> colFechaMov,colTipoMov,colMontoMov;
    @FXML private TableView<Compra> tablaCompras;
    @FXML private TableColumn<Compra,String> colIdCompra,colFechaCompra,colDetalleCompra,colTotalCompra;
    @FXML private TableView<CuentaPuntos> tablaPuntos;
    @FXML private TableColumn<CuentaPuntos,String> colCantidadPuntos,colFechaObtencion,colFechaVencimiento,colEstadoPuntos;
    private Cliente cliente;
    public static class Movimiento {private final String fecha,tipo,monto;public Movimiento(String f,String t,String m){fecha=f;tipo=t;monto=m;}public String getFecha(){return fecha;}public String getTipo(){return tipo;}public String getMonto(){return monto;}}
    @FXML public void initialize(){
        Object u=Sesion.getInstancia().getUsuarioActual();if(!(u instanceof Cliente)){alerta("Sesión requerida","Debes iniciar sesión como cliente.");return;}cliente=(Cliente)u;configurarTablas();refrescar();
    }
    private void configurarTablas(){
        colFechaMov.setCellValueFactory(v->new SimpleStringProperty(v.getValue().getFecha()));colTipoMov.setCellValueFactory(v->new SimpleStringProperty(v.getValue().getTipo()));colMontoMov.setCellValueFactory(v->new SimpleStringProperty(v.getValue().getMonto()));
        colIdCompra.setCellValueFactory(v->new SimpleStringProperty(v.getValue().getId()));colFechaCompra.setCellValueFactory(v->new SimpleStringProperty(String.valueOf(v.getValue().getFecha())));colDetalleCompra.setCellValueFactory(v->{String d=v.getValue().getTheCompraCarrito().getElementos().stream().map(Elemento::getNombre).reduce((a,b)->a+", "+b).orElse("—");return new SimpleStringProperty(d);});colTotalCompra.setCellValueFactory(v->new SimpleStringProperty(dinero(v.getValue().getValorPagado())));
        colCantidadPuntos.setCellValueFactory(v->new SimpleStringProperty(String.valueOf(v.getValue().getPuntos())));colFechaObtencion.setCellValueFactory(v->new SimpleStringProperty(String.valueOf(v.getValue().getFechaObtencion())));colFechaVencimiento.setCellValueFactory(v->new SimpleStringProperty(String.valueOf(v.getValue().getFechaVencimiento())));colEstadoPuntos.setCellValueFactory(v->new SimpleStringProperty(v.getValue().estaVencido(LocalDate.now())?"Vencidos":"Vigentes"));
    }
    private void refrescar(){
        Cuenta c=cliente.getCuenta();TarjetaVirtual t=c.getTheTarjetaVirtualCliente();lblSaldoTarjeta.setText(dinero(t==null?0:t.getSaldo()));lblIdTarjeta.setText("Tarjeta #"+(t==null?"—":t.getId()));
        var compras=Cine.getInstancia().getListCompras().stream().filter(x->x.getCuenta()==c).toList();tablaCompras.setItems(FXCollections.observableArrayList(compras));
        tablaPuntos.setItems(FXCollections.observableArrayList(c.getListCuentaPuntos()));int pts=CuentaPuntos.puntosDisponibles(c,LocalDate.now());lblTotalPuntosVigentes.setText("Total Puntos Vigentes: "+pts+" pts");
        var mov=FXCollections.<Movimiento>observableArrayList();for(Compra x:compras)mov.add(new Movimiento(x.getFecha().toString(),x.isCancelada()?"Compra cancelada":"Compra",dinero(x.getValorPagado())));tablaMovimientos.setItems(mov);
    }
    @FXML private void handleSolicitarRecarga(ActionEvent e){if(cliente==null)return;try{double monto=Double.parseDouble(txtMontoRecarga.getText().trim());if(!Double.isFinite(monto)||monto<=0)throw new NumberFormatException();alerta("Solicitud registrada","Tu solicitud de recarga por "+dinero(monto)+" quedó anotada para revisión. Esta versión no incluye aún un módulo de aprobación administrativa.");txtMontoRecarga.clear();}catch(NumberFormatException ex){alerta("Monto inválido","Ingresa un monto numérico mayor que cero.");}}
    @FXML private void handleSolicitarReembolso(ActionEvent e){if(cliente==null)return;Compra compra=tablaCompras.getSelectionModel().getSelectedItem();if(compra==null){alerta("Selecciona compra","Selecciona una compra del historial.");return;}try{double monto=compra.cancelar(LocalDateTime.now());if(compra.getFuncion()!=null)for(Elemento x:compra.getTheCompraCarrito().getElementos())if(x instanceof Entrada entrada)compra.getFuncion().liberarAsiento(entrada.getElAsiento());alerta("Compra cancelada","Reembolso aplicado: "+dinero(monto)+" ("+compra.getPorcentajeReembolso()+"%).");refrescar();}catch(RuntimeException ex){alerta("No se pudo cancelar",ex.getMessage());}}
    @FXML private void handleVolverCartelera(ActionEvent e){new Navegador((Stage)lblSaldoTarjeta.getScene().getWindow()).navegar("Cartelera.fxml");}
    private String dinero(double v){return String.format(Locale.US,"$ %,.0f",v);}private void alerta(String t,String m){Alert a=new Alert(Alert.AlertType.INFORMATION,t,m,ButtonType.OK);a.showAndWait();}
}
