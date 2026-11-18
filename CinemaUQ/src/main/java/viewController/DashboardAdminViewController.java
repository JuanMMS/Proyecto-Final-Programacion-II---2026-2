package viewcontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.stage.Stage;
import model.Cine;
import navegacion.Navegador;
import service.Sesion;

public class DashboardAdminViewController {
    @FXML private Label lblAdminName, lblTotalEntradas, lblTotalIngresos, lblOcupacion, lblReembolsos;
    @FXML private BarChart<String, Number> barChartPeliculas;
    @FXML private PieChart pieChartVentas;
    @FXML private LineChart<String, Number> lineChartTendencias;

    @FXML public void initialize() {
        Object usuario = Sesion.getInstancia().getUsuarioActual();
        lblAdminName.setText(usuario instanceof model.Administrador a ? "Admin: " + a.getNombre() : "Administrador");
        double ingresos = Cine.getInstancia().getListCompras().stream().filter(c -> !c.isCancelada()).mapToDouble(Compra -> Compra.getValorPagado()).sum();
        lblTotalEntradas.setText(String.valueOf(Cine.getInstancia().getListCompras().stream().filter(c -> !c.isCancelada()).mapToInt(c -> c.getTheCompraCarrito().getElementos().size()).sum()));
        lblTotalIngresos.setText(String.format("$ %,.0f", ingresos));
        lblOcupacion.setText("—");
        lblReembolsos.setText(String.valueOf(Cine.getInstancia().getListCompras().stream().filter(model.Compra::isCancelada).count()));
    }
    @FXML private void handleNavOverview(ActionEvent e) { }
    @FXML private void handleNavGestion(ActionEvent e) { navegar(e, "GestionAdmin.fxml"); }
    @FXML private void handleCerrarSesion(ActionEvent e) { Sesion.getInstancia().cerrarSesion(); navegar(e, "Login.fxml"); }
    private void navegar(ActionEvent e, String fxml) { new Navegador((Stage)((Node)e.getSource()).getScene().getWindow()).navegar(fxml); }
}
