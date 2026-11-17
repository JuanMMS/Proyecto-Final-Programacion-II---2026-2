package ViewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

public class DashboardAdminViewController {

    @FXML private Label lblAdminName;
    @FXML private Label lblTotalEntradas;
    @FXML private Label lblTotalIngresos;
    @FXML private Label lblOcupacion;
    @FXML private Label lblReembolsos;
    @FXML private BarChart<?, ?> barChartPeliculas;
    @FXML private PieChart pieChartVentas;
    @FXML private LineChart<?, ?> lineChartTendencias;

    @FXML
    public void initialize() {
        // Llenar gráficos con datos estadísticos
    }

    @FXML
    void handleCerrarSesion(ActionEvent event) {}
}