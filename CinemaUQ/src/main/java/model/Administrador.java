package model;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Locale;

public class Administrador {
    //Atributos
    private String nombre;
    private String usuario;
    private String contrasenia;
    private double totalRecargado;
    private int cantidadRecargas;

    //Constructor
    public Administrador(String nombre, String usuario, String contrasenia) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }
    // ---------- Recargas administrativas ----------

    /**
     * Recarga de saldo a la tarjeta virtual de un cliente
     * @param cliente cliente que recibe la recarga
     * @param monto valor a recargar (mayor a cero)
     */
    public void recargarTarjeta(Cliente cliente, double monto) {
        if (cliente == null || cliente.getCuenta() == null) {
            throw new IllegalArgumentException("El cliente no existe o no tiene cuenta");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto de la recarga debe ser mayor a cero");
        }
        TarjetaVirtual tarjeta = cliente.getCuenta().getTheTarjetaVirtualCliente();
        if (tarjeta == null) {
            throw new IllegalStateException("La cuenta no tiene tarjeta virtual");
        }
        tarjeta.setSaldo(tarjeta.getSaldo() + monto);
        totalRecargado += monto;
        cantidadRecargas++;
    }

    // ---------- Gestion de clientes ----------

    public Cliente buscarCliente(String id) {
        for (Cliente c : Cine.getInstancia().getListClientes()) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public void registrarCliente(Cliente cliente) {
        Cine cine = Cine.getInstancia();
        if (buscarCliente(cliente.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con el id " + cliente.getId());
        }
        cine.getListClientes().add(cliente);
        if (cliente.getCuenta() != null && !cine.getListCuentasClientes().contains(cliente.getCuenta())) {
            cine.getListCuentasClientes().add(cliente.getCuenta());
        }
    }

    public void actualizarCliente(String id, String nombre, String email, int edad) {
        Cliente cliente = buscarCliente(id);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe el cliente " + id);
        }
        cliente.setNombreCompleto(nombre);
        cliente.setEmail(email);
        cliente.setEdad(edad);
    }

    public void eliminarCliente(String id) {
        Cliente cliente = buscarCliente(id);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe el cliente " + id);
        }
        Cine.getInstancia().getListClientes().remove(cliente);
        Cine.getInstancia().getListCuentasClientes().remove(cliente.getCuenta());
    }

    // ---------- Gestion de productos (confiteria) ----------

    public Confiteria buscarProducto(String nombre) {
        for (Confiteria p : Cine.getInstancia().getListConfiterias()) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    public void agregarProducto(Confiteria producto) {
        if (producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (buscarProducto(producto.getNombre()) != null) {
            throw new IllegalArgumentException("Ya existe el producto " + producto.getNombre());
        }
        Cine.getInstancia().getListConfiterias().add(producto);
    }

    public void actualizarPrecioProducto(String nombre, double nuevoPrecio) {
        Confiteria producto = buscarProducto(nombre);
        if (producto == null) {
            throw new IllegalArgumentException("No existe el producto " + nombre);
        }
        if (nuevoPrecio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        producto.setPrecio(nuevoPrecio);
    }

    public void eliminarProducto(String nombre) {
        Confiteria producto = buscarProducto(nombre);
        if (producto == null) {
            throw new IllegalArgumentException("No existe el producto " + nombre);
        }
        Cine.getInstancia().getListConfiterias().remove(producto);
    }

    // ---------- Vencimiento de puntos ----------

    /**
     * Elimina los puntos vencidos de todas las cuentas
     * @return total de puntos eliminados
     */
    public int depurarPuntosVencidos() {
        int total = 0;
        for (Cuenta cuenta : Cine.getInstancia().getListCuentasClientes()) {
            total += CuentaPuntos.eliminarVencidos(cuenta, LocalDate.now());
        }
        return total;
    }

    // ---------- Estadisticas y reportes ----------

    public int contarComprasActivas() {
        int n = 0;
        for (Compra c : Cine.getInstancia().getListCompras()) {
            if (!c.isCancelada()) {
                n++;
            }
        }
        return n;
    }

    public int contarComprasCanceladas() {
        return Cine.getInstancia().getListCompras().size() - contarComprasActivas();
    }

    /**
     * @return dinero pagado en compras que siguen activas
     */
    public double calcularVentasActivas() {
        double total = 0;
        for (Compra c : Cine.getInstancia().getListCompras()) {
            if (!c.isCancelada()) {
                total += c.getValorPagado();
            }
        }
        return total;
    }

    public double calcularTotalReembolsado() {
        double total = 0;
        for (Compra c : Cine.getInstancia().getListCompras()) {
            total += c.getMontoReembolsado();
        }
        return total;
    }

    /**
     * @return lo pagado en todas las compras menos lo reembolsado
     */
    public double calcularIngresosNetos() {
        double pagado = 0;
        for (Compra c : Cine.getInstancia().getListCompras()) {
            pagado += c.getValorPagado();
        }
        return pagado - calcularTotalReembolsado();
    }

    public int calcularPuntosVigentes() {
        int total = 0;
        for (Cuenta cuenta : Cine.getInstancia().getListCuentasClientes()) {
            total += CuentaPuntos.puntosDisponibles(cuenta, LocalDate.now());
        }
        return total;
    }

    /**
     * Genera el texto del reporte con las estadisticas del cine
     */
    public String generarReporte() {
        Cine cine = Cine.getInstancia();
        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE ").append(cine.getNombre()).append(" (NIT ").append(cine.getNit()).append(")\n");
        sb.append("Generado: ").append(LocalDate.now()).append(" por ").append(nombre).append("\n");
        sb.append("==========================================\n\n");
        sb.append("VENTAS\n");
        sb.append("  Compras activas:      ").append(contarComprasActivas()).append("\n");
        sb.append("  Compras canceladas:   ").append(contarComprasCanceladas()).append("\n");
        sb.append("  Ventas activas:       ").append(dinero(calcularVentasActivas())).append("\n");
        sb.append("  Total reembolsado:    ").append(dinero(calcularTotalReembolsado())).append("\n");
        sb.append("  Ingresos netos:       ").append(dinero(calcularIngresosNetos())).append("\n\n");
        sb.append("CLIENTES Y PUNTOS\n");
        sb.append("  Clientes registrados: ").append(cine.getListClientes().size()).append("\n");
        sb.append("  Puntos vigentes:      ").append(calcularPuntosVigentes()).append("\n\n");
        sb.append("RECARGAS DE ESTE ADMINISTRADOR\n");
        sb.append("  Cantidad:             ").append(cantidadRecargas).append("\n");
        sb.append("  Total recargado:      ").append(dinero(totalRecargado)).append("\n\n");
        sb.append("DETALLE DE COMPRAS\n");
        if (cine.getListCompras().isEmpty()) {
            sb.append("  Sin compras\n");
        }
        for (Compra c : cine.getListCompras()) {
            sb.append("  ").append(c.getId()).append(" | ").append(c.getFecha())
                    .append(" | ").append(dinero(c.getValorPagado())).append(" | ");
            if (c.isCancelada()) {
                sb.append("CANCELADA (").append(c.getPorcentajeReembolso()).append("% = ")
                        .append(dinero(c.getMontoReembolsado())).append(")");
            } else {
                sb.append("ACTIVA");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Exporta el reporte a un archivo TXT
     */
    public void exportarReporteTxt(Path destino) throws IOException {
        Files.writeString(destino, generarReporte(), StandardCharsets.UTF_8);
    }

    private String dinero(double valor) {
        return String.format(Locale.US, "$%,.0f", valor);
    }
    //Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
