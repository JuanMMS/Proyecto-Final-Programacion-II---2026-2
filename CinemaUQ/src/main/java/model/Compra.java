package model;

import composite.Carrito;
import fachada.Fachada;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.time.LocalDateTime;

public class Compra implements Fachada {

    //Atributos
    private String id;
    private double valorTotal;
    private LocalDate fecha;
    private LocalTime hora;
    private Carrito theCompraCarrito;

    private Cuenta cuenta;
    private FuncionPelicula funcion;
    private boolean cancelada;
    private int puntosGenerados;
    private int puntosRedimidos;
    private double descuentoPuntos;
    private double montoReembolsado;
    private int porcentajeReembolso;

    // ---------- Patron Strategy para reembolsos ----------

    /**
     * Cada politica de devolucion define que porcentaje se reembolsa
     */
    public interface PoliticaReembolso {
        int getPorcentaje();

        default double calcular(double valorPagado) {
            return valorPagado * getPorcentaje() / 100.0;
        }
    }

    public static final PoliticaReembolso REEMBOLSO_100 = () -> 100;
    public static final PoliticaReembolso REEMBOLSO_80 = () -> 80;
    public static final PoliticaReembolso REEMBOLSO_0 = () -> 0;

    /**
     * Elige la politica segun el tiempo que falta para la funcion:
     * 24 horas o mas = 100 %, entre 2 y 24 horas = 80 %, menos de 2 horas = 0 %
     */
    public static PoliticaReembolso seleccionarPolitica(LocalDateTime ahora, LocalDateTime inicioFuncion) {
        long minutos = Duration.between(ahora, inicioFuncion).toMinutes();
        if (minutos >= 24 * 60) {
            return REEMBOLSO_100;
        }
        if (minutos >= 2 * 60) {
            return REEMBOLSO_80;
        }
        return REEMBOLSO_0;
    }

    //Constructor
    public Compra(String id, double valorTotal, LocalDate fecha, LocalTime hora, Carrito carrito) {
        this.id = id;
        this.valorTotal = valorTotal;
        this.fecha = fecha;
        this.hora = hora;
        this.theCompraCarrito = carrito;
    }

    /**
     * Constructor completo: incluye la cuenta que compro y la funcion
     */
    public Compra(String id, double valorTotal, LocalDate fecha, LocalTime hora, Carrito carrito,
                  Cuenta cuenta, FuncionPelicula funcion) {
        this(id, valorTotal, fecha, hora, carrito);
        this.cuenta = cuenta;
        this.funcion = funcion;
    }

    /**
     * @return dinero que realmente pago el cliente (valor total menos descuento por puntos)
     */
    public double getValorPagado() {
        return Math.max(0, valorTotal - descuentoPuntos);
    }

    /**
     * Registra los puntos usados como descuento en esta compra
     */
    public void registrarRedencion(int puntos, double descuento) {
        this.puntosRedimidos = puntos;
        this.descuentoPuntos = descuento;
    }

    /**
     * Politica que se aplicaria si se cancelara en el momento dado
     */
    public PoliticaReembolso politicaActual(LocalDateTime ahora) {
        if (funcion == null) {
            return REEMBOLSO_0;
        }
        return seleccionarPolitica(ahora, funcion.getInicio());
    }

    public boolean puedeCancelarse(LocalDateTime ahora) {
        return !cancelada && cuenta != null && funcion != null && ahora.isBefore(funcion.getInicio());
    }

    /**
     * Cancela la compra y devuelve el dinero segun la politica (Strategy).
     * Una compra solo se puede cancelar una vez, asi se evitan reembolsos duplicados.
     * @param ahora momento en que se cancela
     * @return monto devuelto a la tarjeta virtual
     */
    public double cancelar(LocalDateTime ahora) {
        if (cancelada) {
            throw new IllegalStateException("La compra ya fue cancelada y reembolsada");
        }
        if (cuenta == null || funcion == null) {
            throw new IllegalStateException("La compra no tiene cuenta o funcion asociada");
        }
        if (!ahora.isBefore(funcion.getInicio())) {
            throw new IllegalStateException("No se puede cancelar: la funcion ya inicio");
        }

        PoliticaReembolso politica = seleccionarPolitica(ahora, funcion.getInicio());
        double monto = politica.calcular(getValorPagado());

        //Se marca primero para que un segundo intento nunca reembolse de nuevo
        cancelada = true;
        porcentajeReembolso = politica.getPorcentaje();
        montoReembolsado = monto;

        TarjetaVirtual tarjeta = cuenta.getTheTarjetaVirtualCliente();
        if (tarjeta != null && monto > 0) {
            tarjeta.setSaldo(tarjeta.getSaldo() + monto);
        }

        //Los puntos ganados con esta compra se pierden y los redimidos se devuelven en el mismo porcentaje
        CuentaPuntos.revertir(cuenta, id);
        int restaurados = (int) Math.round(puntosRedimidos * politica.getPorcentaje() / 100.0);
        CuentaPuntos.acumular(cuenta, restaurados, ahora.toLocalDate(), "REST-" + id);

        return monto;
    }

    /**
     * Funcion del patron fachada
     */
    public void hacerCompra() {

    }

    //Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Carrito getTheCompraCarrito() {
        return theCompraCarrito;
    }

    public void setTheCompraCarrito(Carrito theCompraCarrito) {
        this.theCompraCarrito = theCompraCarrito;
    }
    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public FuncionPelicula getFuncion() {
        return funcion;
    }

    public void setFuncion(FuncionPelicula funcion) {
        this.funcion = funcion;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public int getPuntosGenerados() {
        return puntosGenerados;
    }

    public void setPuntosGenerados(int puntosGenerados) {
        this.puntosGenerados = puntosGenerados;
    }

    public int getPuntosRedimidos() {
        return puntosRedimidos;
    }

    public double getDescuentoPuntos() {
        return descuentoPuntos;
    }

    public double getMontoReembolsado() {
        return montoReembolsado;
    }

    public int getPorcentajeReembolso() {
        return porcentajeReembolso;
    }
}
