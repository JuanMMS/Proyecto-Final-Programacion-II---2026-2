package viewcontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.*;
import navegacion.Navegador;
import service.Sesion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardAdminViewController {
    @FXML private Label lblAdminName, lblTotalEntradas, lblTotalIngresos, lblOcupacion, lblReembolsos;
    @FXML private BarChart<String, Number> barChartPeliculas;
    @FXML private PieChart pieChartVentas;
    @FXML private LineChart<String, Number> lineChartTendencias;

    @FXML public void initialize() {
        Object usuario = Sesion.getInstancia().getUsuarioActual();
        lblAdminName.setText(usuario instanceof Administrador a ? "Admin: " + a.getNombre() : "Administrador");
        actualizarDashboard();
    }

    @FXML private void handleNavOverview(ActionEvent e) { actualizarDashboard(); }
    @FXML private void handleNavGestion(ActionEvent e) { navegar(e, "GestionAdmin.fxml"); }
    @FXML private void handleCerrarSesion(ActionEvent e) { Sesion.getInstancia().cerrarSesion(); navegar(e, "Login.fxml"); }

    private void actualizarDashboard() {
        List<Compra> compras = Cine.getInstancia().getListCompras();
        List<Compra> activas = compras.stream().filter(c -> !c.isCancelada()).toList();
        double ingresos = activas.stream().mapToDouble(Compra::getValorPagado).sum();
        int entradas = activas.stream().filter(c -> c.getTheCompraCarrito() != null)
                .mapToInt(c -> (int)c.getTheCompraCarrito().getElementos().stream().filter(e -> e instanceof Entrada).count()).sum();
        lblTotalEntradas.setText(String.valueOf(entradas));
        lblTotalIngresos.setText(String.format(Locale.forLanguageTag("es-CO"), "$ %,.0f", ingresos));
        lblReembolsos.setText(String.valueOf(compras.stream().filter(Compra::isCancelada).count()));
        var funciones = Cine.getInstancia().getListSalasCine().stream()
                .flatMap(s -> s.getListFuncionesPelicula().stream()).toList();
        int capacidad = funciones.stream().mapToInt(f -> f.getTheSalaCine().getListAsientosSalaCine().size()).sum();
        int ocupados = funciones.stream().mapToInt(f -> f.getAsientosOcupados().size()).sum();
        double porcentaje = capacidad == 0 ? 0 : Math.min(100, ocupados * 100.0 / capacidad);
        lblOcupacion.setText(String.format(Locale.ROOT, "%.1f%%", porcentaje));

        barChartPeliculas.getData().clear();
        Map<String, Double> ingresosPorPelicula = new LinkedHashMap<>();
        for (Compra compra : activas) {
            String pelicula = compra.getFuncion() == null || compra.getFuncion().getThePeliculaFuncion() == null
                    ? "Sin función" : compra.getFuncion().getThePeliculaFuncion().getNombre();
            ingresosPorPelicula.merge(pelicula, compra.getValorPagado(), Double::sum);
        }
        XYChart.Series<String, Number> peliculas = new XYChart.Series<>();
        peliculas.setName("Ingresos por película");
        ingresosPorPelicula.entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5).forEach(e -> peliculas.getData().add(new XYChart.Data<>(e.getKey(), e.getValue())));
        barChartPeliculas.getData().add(peliculas);

        pieChartVentas.getData().clear();
        double entradasIngresos = activas.stream().filter(c -> c.getTheCompraCarrito() != null)
                .flatMap(c -> c.getTheCompraCarrito().getElementos().stream()).filter(e -> e instanceof Entrada)
                .mapToDouble(e -> e.getPrecio()).sum();
        double confiteriaIngresos = Math.max(0, ingresos - entradasIngresos);
        if (entradasIngresos > 0) pieChartVentas.getData().add(new PieChart.Data("Entradas", entradasIngresos));
        if (confiteriaIngresos > 0) pieChartVentas.getData().add(new PieChart.Data("Confitería", confiteriaIngresos));
        if (pieChartVentas.getData().isEmpty()) pieChartVentas.setTitle("Sin ventas registradas");
        else pieChartVentas.setTitle("Distribución de ventas");

        lineChartTendencias.getData().clear();
        Map<LocalDate, Double> ventasDia = activas.stream().collect(Collectors.groupingBy(Compra::getFecha,
                TreeMap::new, Collectors.summingDouble(Compra::getValorPagado)));
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM");
        XYChart.Series<String, Number> tendenciaVentas = new XYChart.Series<>();
        tendenciaVentas.setName("Ventas");
        ventasDia.forEach((fecha, valor) -> tendenciaVentas.getData().add(new XYChart.Data<>(fecha.format(formato), valor)));
        Map<LocalDate, Double> recargasDia = new TreeMap<>();
        for (Cliente cliente : service.GestorUsuarios.getInstancia().obtenerClientes()) {
            if (cliente.getCuenta().getTheTarjetaVirtualCliente() == null) continue;
            for (MovimientoTarjeta movimiento : cliente.getCuenta().getTheTarjetaVirtualCliente().getMovimientos()) {
                if (movimiento.getTipo().toLowerCase(Locale.ROOT).contains("recarga")) {
                    LocalDate fecha = movimiento.getFecha().toLocalDate();
                    recargasDia.merge(fecha, movimiento.getMonto(), Double::sum);
                }
            }
        }
        XYChart.Series<String, Number> tendenciaRecargas = new XYChart.Series<>();
        tendenciaRecargas.setName("Recargas");
        recargasDia.forEach((fecha, valor) -> tendenciaRecargas.getData().add(new XYChart.Data<>(fecha.format(formato), valor)));
        if (!tendenciaVentas.getData().isEmpty()) lineChartTendencias.getData().add(tendenciaVentas);
        if (!tendenciaRecargas.getData().isEmpty()) lineChartTendencias.getData().add(tendenciaRecargas);
    }

    private void navegar(ActionEvent e, String fxml) { new Navegador((Stage)((Node)e.getSource()).getScene().getWindow()).navegar(fxml); }
}
