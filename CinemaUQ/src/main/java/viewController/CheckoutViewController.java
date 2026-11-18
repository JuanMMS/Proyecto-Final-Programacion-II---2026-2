package viewController;

import composite.Carrito;
import fachada.CompraFachada;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import navegacion.FlujoCompra;
import navegacion.Navegador;
import service.Sesion;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.UUID;

public class CheckoutViewController {
    @FXML private TextArea txtDetalleOrden;
    @FXML private Label lblTotalPagar,lblPuntosGanados,lblSaldoDisponible,lblPuntosDisponibles,lblCostoEnPuntos;
    @FXML private RadioButton rbTarjetaVirtual,rbPuntos;
    @FXML private ToggleGroup grupoPago;
    @FXML private PasswordField txtContraseniaConfirmacion;
    @FXML private Button btnConfirmarPago;
    private Cliente cliente;
    private double total;
    @FXML public void initialize(){
        Object u=Sesion.getInstancia().getUsuarioActual();
        if(!(u instanceof Cliente)){btnConfirmarPago.setDisable(true);alerta("Sesión requerida","Inicia sesión como cliente para realizar una compra.");return;}
        cliente=(Cliente)u; Carrito c=FlujoCompra.getCarrito(); total=c.getPrecio();
        StringBuilder detalle=new StringBuilder();
        for(var item:c.getElementos()) detalle.append("• ").append(item.getNombre()).append(" - ").append(dinero(item.getPrecio())).append('\n');
        FuncionPelicula f=FlujoCompra.getFuncionActual(); if(f!=null) detalle.append("\nFunción: ").append(f.getThePeliculaFuncion().getNombre()).append("\n").append(f.getFecha()).append(" ").append(f.getHora());
        txtDetalleOrden.setText(detalle.toString()); lblTotalPagar.setText(dinero(total));
        int puntos=CuentaPuntos.puntosDisponibles(cliente.getCuenta(),LocalDate.now());
        lblPuntosDisponibles.setText("Puntos vigentes: "+puntos+" pts"); lblCostoEnPuntos.setText("Puntos necesarios: "+(int)Math.ceil(total/CuentaPuntos.VALOR_PUNTO));
        lblPuntosGanados.setText("Con esta compra ganarás: "+CuentaPuntos.puntosPorValor(total)+" pts ⭐");
        TarjetaVirtual tarjeta=cliente.getCuenta().getTheTarjetaVirtualCliente(); lblSaldoDisponible.setText("Saldo disponible: "+dinero(tarjeta==null?0:tarjeta.getSaldo()));
        rbTarjetaVirtual.setSelected(true);
    }
    @FXML private void handleConfirmarPago(ActionEvent event){
        if(cliente==null)return;
        if(total<=0){alerta("Carrito vacío","Agrega al menos un producto o asiento antes de pagar.");return;}
        Cuenta cuenta=cliente.getCuenta(); int puntosUsados=0; double descuento=0; double montoTarjeta=total;
        try {
            Compra compra;
            if(rbPuntos.isSelected()){
                puntosUsados=(int)Math.ceil(total/CuentaPuntos.VALOR_PUNTO);
                if(CuentaPuntos.puntosDisponibles(cuenta,LocalDate.now())<puntosUsados){alerta("Puntos insuficientes","No tienes puntos suficientes para cubrir toda la compra. Selecciona tarjeta virtual.");return;}
                descuento=CuentaPuntos.redimir(cuenta,puntosUsados,LocalDate.now()); montoTarjeta=Math.max(0,total-descuento);
                String id="COMPRA-"+UUID.randomUUID().toString().substring(0,8).toUpperCase(Locale.ROOT);
                compra=new Compra(id,total,LocalDate.now(),LocalTime.now(),FlujoCompra.getCarrito(),cuenta,FlujoCompra.getFuncionActual());
                compra.registrarRedencion(puntosUsados,descuento);
                Cine.getInstancia().getListCompras().add(compra);
            } else {
                String pass=txtContraseniaConfirmacion.getText();
                if(pass==null||pass.isBlank()){alerta("Contraseña requerida","Confirma tu contraseña para autorizar el pago.");return;}
                compra=new CompraFachada(cuenta,pass,FlujoCompra.getCarrito(),total,FlujoCompra.getFuncionActual()).ejecutarCompra();
            }
            String id=compra.getId();
            int ganados=CuentaPuntos.puntosPorValor(Math.max(0,total-descuento)); compra.setPuntosGenerados(ganados); CuentaPuntos.acumular(cuenta,ganados,LocalDate.now(),id);
            for(Asiento a:FlujoCompra.getAsientos()) FlujoCompra.getFuncionActual().ocuparAsiento(a);
            alerta("Compra exitosa","Tu compra fue registrada. Código: "+id+"\nTotal: "+dinero(total)+"\nPuntos ganados: "+ganados);
            FlujoCompra.limpiar(); new Navegador((Stage)btnConfirmarPago.getScene().getWindow()).navegar("Cartelera.fxml");
        } catch(RuntimeException ex){ if(puntosUsados>0 && descuento>0){ /* se mantiene la trazabilidad del error; no se simula devolución automática */ } alerta("No se pudo completar la compra",ex.getMessage()); }
    }
    @FXML private void handleCancelar(ActionEvent event){new Navegador((Stage)btnConfirmarPago.getScene().getWindow()).navegar("Confiteria.fxml");}
    private String dinero(double v){return String.format(Locale.US,"$ %,.0f",v);}
    private void alerta(String t,String m){Alert a=new Alert(Alert.AlertType.INFORMATION,t,m,ButtonType.OK);a.showAndWait();}
}
