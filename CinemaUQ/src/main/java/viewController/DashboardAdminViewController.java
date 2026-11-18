package viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;
import navegacion.Navegador;
import navegacion.FlujoCompra;
import service.Sesion;
import java.util.*;

public class DashboardAdminViewController {
    @FXML private Button btnOverview,btnGestion,btnCerrarSesion;
    @FXML private Label lblAdminName,lblTotalEntradas,lblTotalIngresos,lblOcupacion,lblReembolsos;
    @FXML private BarChart<String,Number> barChartPeliculas;
    @FXML private PieChart pieChartVentas;
    @FXML private LineChart<String,Number> lineChartTendencias;
    @FXML public void initialize(){
        Object u=Sesion.getInstancia().getUsuarioActual();lblAdminName.setText("Admin: "+(u instanceof Administrador a?a.getNombre():"Administrador"));
        List<Compra> compras=Cine.getInstancia().getListCompras();double ingresos=compras.stream().filter(c->!c.isCancelada()).mapToDouble(Compra::getValorPagado).sum();int reembolsos=(int)compras.stream().filter(Compra::isCancelada).count();
        lblTotalEntradas.setText(String.valueOf(compras.stream().filter(c->!c.isCancelada()).mapToInt(c->(int)c.getTheCompraCarrito().getElementos().stream().filter(x->x instanceof Entrada).count()).sum()));lblTotalIngresos.setText(String.format(Locale.US,"$ %,.0f",ingresos));lblReembolsos.setText(String.valueOf(reembolsos));lblOcupacion.setText("—");
        barChartPeliculas.getData().clear();XYChart.Series<String,Number> serie=new XYChart.Series<>();for(Pelicula p:Cine.getInstancia().getListPeliculas())serie.getData().add(new XYChart.Data<>(p.getNombre(),p.getListFuncionesPelicula().size()));barChartPeliculas.getData().add(serie);
        pieChartVentas.getData().clear();pieChartVentas.getData().add(new PieChart.Data("Compras activas",compras.stream().filter(c->!c.isCancelada()).count()));pieChartVentas.getData().add(new PieChart.Data("Canceladas",reembolsos));
        lineChartTendencias.getData().clear();XYChart.Series<String,Number> ventas=new XYChart.Series<>();ventas.setName("Ingresos por compra");int n=0;for(Compra c:compras)if(!c.isCancelada())ventas.getData().add(new XYChart.Data<>(String.valueOf(++n),c.getValorPagado()));lineChartTendencias.getData().add(ventas);
    }
    @FXML private void handleNavOverview(ActionEvent e){}
    @FXML private void handleNavGestion(ActionEvent e){navegar(e,"GestionAdmin.fxml");}
    @FXML private void handleCerrarSesion(ActionEvent e){Sesion.getInstancia().cerrarSesion();FlujoCompra.limpiar();navegar(e,"Login.fxml");}
    private void navegar(ActionEvent e,String f){new Navegador((Stage)((Node)e.getSource()).getScene().getWindow()).navegar(f);}
}
