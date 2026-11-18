package viewcontroller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;
import navegacion.Navegador;
import service.GestorUsuarios;
import service.Sesion;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.Locale;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GestionAdminViewController {
    @FXML private TextField txtTituloPeli, txtDirectorPeli, txtHoraFuncion;
    @FXML private ComboBox<Pelicula> cmbPelicula;
    @FXML private ComboBox<SalaCine> cmbSala;
    @FXML private DatePicker dpFechaFuncion;
    @FXML private TableView<Pelicula> tablaPeliculas;
    @FXML private TableView<FuncionPelicula> tablaFunciones;
    @FXML private TextField txtNombreSala, txtFilasSala, txtColumnasSala;
    @FXML private TableView<SalaCine> tablaSalas;
    @FXML private TextField txtNombreProducto, txtPrecioProducto;
    @FXML private ComboBox<String> cmbTipoProducto;
    @FXML private TableView<Confiteria> tablaProductos;
    @FXML private TextField txtCodigoPromo, txtPorcentajePromo;
    @FXML private TableView<Promocion> tablaPromociones;
    @FXML private TableView<Compra> tablaComprasAdmin;
    @FXML private ComboBox<Cliente> cmbClienteRecarga;
    @FXML private TextField txtMontoRecargaAdmin;
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TextArea txtReportePreview;
    private static int ultimoIdPelicula = 0;
    private static int ultimoIdFuncion = 0;

    @FXML public void initialize() {
        configurarTablaPeliculas(); configurarTablaFunciones(); configurarTablaSalas();
        configurarTablaProductos(); configurarTablaPromos(); configurarTablaCompras(); configurarTablaClientes();
        cmbTipoProducto.getItems().setAll("Palomitas", "Gaseosa", "Chocolatina");
        configurarTextoFormularioFuncion();
        refrescar();
    }

    private void configurarTextoFormularioFuncion() {
        cmbPelicula.setButtonCell(new ListCell<>() { @Override protected void updateItem(Pelicula item, boolean empty) { super.updateItem(item, empty); setText(empty || item == null ? "Seleccionar película" : item.getNombre()); setStyle("-fx-text-fill: white; -fx-background-color: #2a2a35;"); }});
        cmbPelicula.setCellFactory(list -> new ListCell<>() { @Override protected void updateItem(Pelicula item, boolean empty) { super.updateItem(item, empty); setText(empty || item == null ? null : item.getNombre()); setStyle("-fx-text-fill: white; -fx-background-color: #2a2a35;"); }});
        cmbSala.setButtonCell(new ListCell<>() { @Override protected void updateItem(SalaCine item, boolean empty) { super.updateItem(item, empty); setText(empty || item == null ? "Seleccionar sala" : item.getIdSalaCine()); setStyle("-fx-text-fill: white; -fx-background-color: #2a2a35;"); }});
        cmbSala.setCellFactory(list -> new ListCell<>() { @Override protected void updateItem(SalaCine item, boolean empty) { super.updateItem(item, empty); setText(empty || item == null ? null : item.getIdSalaCine()); setStyle("-fx-text-fill: white; -fx-background-color: #2a2a35;"); }});
        dpFechaFuncion.getEditor().setStyle("-fx-text-fill: white; -fx-prompt-text-fill: #b8b8c5; -fx-background-color: #2a2a35;");
    }

    private void configurarTablaPeliculas() {
        tablaPeliculas.getColumns().clear();
        tablaPeliculas.getColumns().add(col("ID", p -> p.getIdPelicula()));
        tablaPeliculas.getColumns().add(col("Título", Pelicula::getNombre));
        tablaPeliculas.getColumns().add(col("Director", Pelicula::getDirector));
        tablaPeliculas.getColumns().add(col("Funciones", p -> String.valueOf(p.getListFuncionesPelicula().size())));
    }
    private void configurarTablaFunciones() {
        tablaFunciones.getColumns().clear();
        tablaFunciones.getColumns().add(col("ID", FuncionPelicula::getId));
        tablaFunciones.getColumns().add(col("Película", f -> f.getThePeliculaFuncion().getNombre()));
        tablaFunciones.getColumns().add(col("Sala", f -> f.getTheSalaCine().getIdSalaCine()));
        tablaFunciones.getColumns().add(col("Fecha", f -> f.getFecha().toString()));
        tablaFunciones.getColumns().add(col("Hora", f -> f.getHora().toString()));
        tablaFunciones.getColumns().add(col("Ocupados", f -> String.valueOf(f.getAsientosOcupados().size())));
    }
    private void configurarTablaSalas() {
        tablaSalas.getColumns().clear(); tablaSalas.getColumns().add(col("Sala", SalaCine::getIdSalaCine));
        tablaSalas.getColumns().add(col("Capacidad", s -> String.valueOf(s.getListAsientosSalaCine().size())));
        tablaSalas.getColumns().add(col("Funciones", s -> String.valueOf(s.getListFuncionesPelicula().size())));
    }
    private void configurarTablaProductos() {
        tablaProductos.getColumns().clear(); tablaProductos.getColumns().add(col("Producto", Confiteria::getNombre));
        TableColumn<Confiteria, Number> precio = new TableColumn<>("Precio");
        precio.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrecio()));
        precio.setCellFactory(c -> new TableCell<>() { @Override protected void updateItem(Number n, boolean empty) { super.updateItem(n, empty); setText(empty || n == null ? null : moneda(n.doubleValue())); }});
        tablaProductos.getColumns().add(precio);
    }
    private void configurarTablaPromos() {
        tablaPromociones.getColumns().clear(); tablaPromociones.getColumns().add(col("Código", Promocion::getCodigo));
        tablaPromociones.getColumns().add(col("Descuento", p -> p.getPorcentaje() + "%"));
        tablaPromociones.getColumns().add(col("Estado", p -> p.isActiva() ? "Activa" : "Inactiva"));
    }
    private void configurarTablaCompras() {
        tablaComprasAdmin.getColumns().clear(); tablaComprasAdmin.getColumns().add(col("Compra", Compra::getId));
        tablaComprasAdmin.getColumns().add(col("Cliente", c -> c.getCuenta() == null ? "—" : c.getCuenta().getUsuario()));
        tablaComprasAdmin.getColumns().add(col("Fecha", c -> c.getFecha() + " " + c.getHora()));
        tablaComprasAdmin.getColumns().add(col("Estado", c -> c.isCancelada() ? "Cancelada" : "Pagada"));
        tablaComprasAdmin.getColumns().add(col("Valor", c -> moneda(c.getValorPagado())));
    }
    private void configurarTablaClientes() {
        tablaClientes.getColumns().clear(); tablaClientes.getColumns().add(col("Nombre", Cliente::getNombreCompleto));
        tablaClientes.getColumns().add(col("Usuario", c -> c.getCuenta().getUsuario()));
        tablaClientes.getColumns().add(col("Correo", Cliente::getEmail));
        tablaClientes.getColumns().add(col("Saldo", c -> moneda(c.getCuenta().getTheTarjetaVirtualCliente().getSaldo())));
    }
    private <T> TableColumn<T,String> col(String titulo, java.util.function.Function<T,String> getter) {
        TableColumn<T,String> column = new TableColumn<>(titulo);
        column.setCellValueFactory(c -> new SimpleStringProperty(getter.apply(c.getValue())));
        return column;
    }

    private void refrescar() {
        Cine cine = Cine.getInstancia();
        tablaPeliculas.getItems().setAll(cine.getListPeliculas());
        cmbPelicula.getItems().setAll(cine.getListPeliculas()); cmbSala.getItems().setAll(cine.getListSalasCine());
        tablaFunciones.getItems().setAll(cine.getListSalasCine().stream().flatMap(s -> s.getListFuncionesPelicula().stream()).toList());
        tablaSalas.getItems().setAll(cine.getListSalasCine()); tablaProductos.getItems().setAll(cine.getListConfiterias());
        tablaPromociones.getItems().setAll(cine.getListPromociones());
        tablaComprasAdmin.getItems().setAll(cine.getListCompras());
        tablaClientes.getItems().setAll(GestorUsuarios.getInstancia().obtenerClientes());
        cmbClienteRecarga.getItems().setAll(GestorUsuarios.getInstancia().obtenerClientes());
    }

    @FXML private void volverDashboard(ActionEvent e) { navegar(e,"DashboardAdmin.fxml"); }
    @FXML private void cerrarSesion(ActionEvent e) { Sesion.getInstancia().cerrarSesion(); navegar(e,"Login.fxml"); }
    @FXML private void guardarPelicula(ActionEvent e) {
        String titulo = valor(txtTituloPeli), director = valor(txtDirectorPeli);
        if (titulo.isBlank() || director.isBlank()) { alerta("Completa título y director."); return; }
        if (Cine.getInstancia().getListPeliculas().stream().anyMatch(p -> p.getNombre().equalsIgnoreCase(titulo))) { alerta("Ya existe una película con ese título."); return; }
        String id = siguienteId("PEL-", Cine.getInstancia().getListPeliculas().stream().map(Pelicula::getIdPelicula).toList());
        Cine.getInstancia().agregarPelicula(new Pelicula(id,titulo,director)); txtTituloPeli.clear(); txtDirectorPeli.clear(); refrescar(); alerta("Película creada con ID " + id);
    }
    @FXML private void eliminarPelicula(ActionEvent e) {
        Pelicula p = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (p == null) { alerta("Selecciona una película."); return; }
        if (!p.getListFuncionesPelicula().isEmpty()) { alerta("No puedes eliminar una película con funciones programadas."); return; }
        Cine.getInstancia().getListPeliculas().remove(p); refrescar();
    }
    @FXML private void programarFuncion(ActionEvent e) {
        Pelicula p = cmbPelicula.getValue(); SalaCine s = cmbSala.getValue();
        if (p == null || s == null || dpFechaFuncion.getValue() == null) { alerta("Selecciona película, sala y fecha."); return; }
        try {
            LocalTime hora = LocalTime.parse(valor(txtHoraFuncion));
            if (dpFechaFuncion.getValue().isBefore(java.time.LocalDate.now())) { alerta("La fecha no puede estar en el pasado."); return; }
            String id = siguienteId("FUN-", Cine.getInstancia().getListSalasCine().stream().flatMap(sala -> sala.getListFuncionesPelicula().stream()).map(FuncionPelicula::getId).toList());
            FuncionPelicula f = new FuncionPelicula(id,dpFechaFuncion.getValue(),hora,s,p);
            p.agregarFuncion(f); s.agregarFuncion(f); refrescar(); alerta("Función programada con ID " + id + ".");
        } catch (RuntimeException ex) { alerta("La hora debe tener formato HH:mm."); }
    }
    @FXML private void cancelarFuncion(ActionEvent e) {
        FuncionPelicula f = tablaFunciones.getSelectionModel().getSelectedItem();
        if (f == null) { alerta("Selecciona una función."); return; }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "Se cancelará la función " + f.getId() + " y se reembolsará el 100% a todos los clientes con compras asociadas. ¿Continuar?", ButtonType.CANCEL, ButtonType.OK);
        confirmacion.setHeaderText("Confirmar cancelación de función");
        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;
        double reembolsado = 0; int comprasCanceladas = 0;
        for (Compra compra : Cine.getInstancia().getListCompras()) {
            if (compra.getFuncion() == f && !compra.isCancelada()) {
                reembolsado += compra.cancelarPorCancelacionFuncion(LocalDateTime.now());
                comprasCanceladas++;
            }
        }
        f.notificarObservadores("La función " + f + " fue cancelada por el cine. Se reembolsó el 100% de las compras.");
        f.getTheSalaCine().getListFuncionesPelicula().remove(f); f.getThePeliculaFuncion().getListFuncionesPelicula().remove(f);
        refrescar(); alerta("Función cancelada. Compras reembolsadas: " + comprasCanceladas + ". Total devuelto: " + moneda(reembolsado));
    }

    @FXML private void crearSala(ActionEvent e) {
        String id = valor(txtNombreSala); if (id.isBlank()) { alerta("Ingresa el identificador de la sala."); return; }
        if (Cine.getInstancia().getListSalasCine().stream().anyMatch(s -> s.getIdSalaCine().equalsIgnoreCase(id))) { alerta("Ya existe esa sala."); return; }
        try {
            int filas = Integer.parseInt(valor(txtFilasSala)), columnas = Integer.parseInt(valor(txtColumnasSala));
            if (filas < 1 || filas > 15 || columnas < 1 || columnas > 20) throw new NumberFormatException();
            SalaCine sala = new SalaCine(id);
            for (int f=0; f<filas; f++) for (int c=1; c<=columnas; c++) sala.agregarAsiento(new Asiento((char)('A'+f) + String.valueOf(c), sala));
            Cine.getInstancia().agregarSala(sala); txtNombreSala.clear(); refrescar(); alerta("Sala creada con " + (filas*columnas) + " asientos.");
        } catch (NumberFormatException ex) { alerta("Filas (1-15) y columnas (1-20) deben ser números enteros."); }
    }
    @FXML private void eliminarSala(ActionEvent e) {
        SalaCine s = tablaSalas.getSelectionModel().getSelectedItem(); if (s == null) { alerta("Selecciona una sala."); return; }
        if (!s.getListFuncionesPelicula().isEmpty()) { alerta("No se puede eliminar una sala con funciones programadas."); return; }
        Cine.getInstancia().getListSalasCine().remove(s); refrescar();
    }

    @FXML private void agregarProducto(ActionEvent e) {
        String nombre = valor(txtNombreProducto), tipo = cmbTipoProducto.getValue();
        if (nombre.isBlank() || tipo == null) { alerta("Ingresa nombre y tipo de producto."); return; }
        try {
            double precio = Double.parseDouble(valor(txtPrecioProducto)); if (precio <= 0 || !Double.isFinite(precio)) throw new NumberFormatException();
            Confiteria producto = switch (tipo) {
                case "Palomitas" -> new Palomita(nombre, precio);
                case "Gaseosa" -> new Gasesosa(nombre, precio, TipoGaseosa.PEPSI);
                default -> new Chocolatina(nombre, precio, TipoChocolatina.CONLECHE);
            };
            Cine.getInstancia().getListConfiterias().add(producto); txtNombreProducto.clear(); txtPrecioProducto.clear(); refrescar();
        } catch (RuntimeException ex) { alerta("Ingresa un precio válido mayor que cero."); }
    }
    @FXML private void eliminarProducto(ActionEvent e) {
        Confiteria p = tablaProductos.getSelectionModel().getSelectedItem(); if (p == null) { alerta("Selecciona un producto."); return; }
        Cine.getInstancia().getListConfiterias().remove(p); refrescar();
    }

    @FXML private void crearPromocion(ActionEvent e) {
        try {
            Promocion p = new Promocion(valor(txtCodigoPromo), Integer.parseInt(valor(txtPorcentajePromo)));
            if (Cine.getInstancia().getListPromociones().stream().anyMatch(x -> x.getCodigo().equals(p.getCodigo()))) { alerta("Ese código ya existe."); return; }
            Cine.getInstancia().getListPromociones().add(p); txtCodigoPromo.clear(); txtPorcentajePromo.clear(); refrescar();
        } catch (RuntimeException ex) { alerta("Ingresa un código y un descuento entero entre 1 y 90."); }
    }
    @FXML private void alternarPromocion(ActionEvent e) {
        Promocion p = tablaPromociones.getSelectionModel().getSelectedItem(); if (p == null) { alerta("Selecciona una promoción."); return; }
        p.setActiva(!p.isActiva()); refrescar();
    }

    @FXML private void recargarTarjeta(ActionEvent e) {
        Cliente c = cmbClienteRecarga.getValue(); if (c == null) { alerta("Selecciona un cliente."); return; }
        try {
            double monto = Double.parseDouble(valor(txtMontoRecargaAdmin)); if (monto <= 0 || !Double.isFinite(monto)) throw new NumberFormatException();
            TarjetaVirtual t = c.getCuenta().getTheTarjetaVirtualCliente(); t.setSaldo(t.getSaldo()+monto); t.registrarMovimiento("Recarga administrativa", "Recarga aprobada por administrador", monto);
            txtMontoRecargaAdmin.clear(); refrescar(); alerta("Recarga realizada correctamente.");
        } catch (NumberFormatException ex) { alerta("Ingresa un monto válido mayor que cero."); }
    }

    @FXML private void generarReporte(ActionEvent e) {
        String reporte = construirReporte();
        txtReportePreview.setText(reporte);
        System.out.println("\n" + reporte);
    }
    @FXML private void guardarReporte(ActionEvent e) {
        String contenido = txtReportePreview.getText().isBlank() ? construirReporte() : txtReportePreview.getText();
        FileChooser chooser = new FileChooser(); chooser.setTitle("Guardar reporte CinemaUQ"); chooser.setInitialFileName("reporte-cinemauq.txt");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo de texto", "*.txt"));
        File archivo = chooser.showSaveDialog((Stage)((Node)e.getSource()).getScene().getWindow());
        if (archivo == null) return;
        try { Files.writeString(archivo.toPath(), contenido); alerta("Reporte guardado en: " + archivo.getAbsolutePath()); }
        catch (Exception ex) { alerta("No se pudo guardar el reporte: " + ex.getMessage()); }
    }
    private String construirReporte() {
        Cine cine = Cine.getInstancia();
        double ingresos = cine.getListCompras().stream().filter(c -> !c.isCancelada()).mapToDouble(Compra::getValorPagado).sum();
        long canceladas = cine.getListCompras().stream().filter(Compra::isCancelada).count();
        StringBuilder b = new StringBuilder("REPORTE ADMINISTRATIVO — CINEMAUQ\nGenerado: ").append(java.time.LocalDateTime.now())
                .append("\n\nRESUMEN\nPelículas: ").append(cine.getListPeliculas().size())
                .append("\nSalas: ").append(cine.getListSalasCine().size())
                .append("\nClientes: ").append(GestorUsuarios.getInstancia().obtenerClientes().size())
                .append("\nProductos de confitería: ").append(cine.getListConfiterias().size())
                .append("\nPromociones: ").append(cine.getListPromociones().size())
                .append("\nCompras: ").append(cine.getListCompras().size())
                .append("\nCompras canceladas: ").append(canceladas)
                .append("\nIngresos netos: ").append(moneda(ingresos)).append("\n\nDETALLE DE COMPRAS\n");
        for (Compra c : cine.getListCompras()) b.append(c.getId()).append(" | ").append(c.getFecha()).append(" ").append(c.getHora())
                .append(" | ").append(c.getCuenta()==null?"—":c.getCuenta().getUsuario()).append(" | ")
                .append(c.isCancelada()?"CANCELADA":"PAGADA").append(" | ").append(moneda(c.getValorPagado())).append('\n');
        return b.toString();
    }

    private String siguienteId(String prefijo, java.util.List<String> ids) {
        int maximo = 0;
        Pattern patron = Pattern.compile(Pattern.quote(prefijo) + "(\\d+)");
        for (String id : ids) {
            Matcher coincidencia = patron.matcher(id == null ? "" : id);
            if (coincidencia.matches()) maximo = Math.max(maximo, Integer.parseInt(coincidencia.group(1)));
        }
        if ("PEL-".equals(prefijo)) maximo = Math.max(maximo, ultimoIdPelicula);
        if ("FUN-".equals(prefijo)) maximo = Math.max(maximo, ultimoIdFuncion);
        int siguiente = maximo + 1;
        if ("PEL-".equals(prefijo)) ultimoIdPelicula = siguiente;
        if ("FUN-".equals(prefijo)) ultimoIdFuncion = siguiente;
        return prefijo + String.format(Locale.ROOT, "%03d", siguiente);
    }

    private String valor(TextField f) { return f.getText() == null ? "" : f.getText().trim(); }
    private String moneda(double n) { return String.format(Locale.forLanguageTag("es-CO"), "$ %,.0f", n); }
    private void navegar(ActionEvent e,String fxml) { new Navegador((Stage)((Node)e.getSource()).getScene().getWindow()).navegar(fxml); }
    private void alerta(String m) { new Alert(Alert.AlertType.INFORMATION,m).showAndWait(); }
}
