import composite.Carrito;
import decorator.ComplementoPalomita;
import decorator.ConDulce;
import decorator.ConSal;
import fachada.CompraFachada;
import factory.ChocolatinaFactory;
import factory.DatosConfiteria;
import factory.FabricaCombo;
import factory.GaseosaFactory;
import factory.PalomitaFactory;
import factory.combos.Combo1;
import iterator.Cartelera;
import model.Administrador;
import model.Asiento;
import model.Chocolatina;
import model.Cliente;
import model.Compra;
import model.Confiteria;
import model.Cuenta;
import model.CuentaPuntos;
import model.Entrada;
import model.FuncionPelicula;
import model.Gasesosa;
import model.Palomita;
import model.Pelicula;
import model.SalaCine;
import model.TarjetaVirtual;
import model.TipoChocolatina;
import model.TipoGaseosa;
import observer.Observador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proxy.MedioPago;
import proxy.PagoTarjetaProxy;
import proxy.PagoTarjetaVirtual;
import service.AutenticacionService;
import service.GestorUsuarios;
import service.RegistroService;
import service.Sesion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class PruebasUnitariasTest {

    private static int contador = 0;

    private String unico(String prefijo) {
        contador++;
        return prefijo + contador;
    }

    private Cuenta crearCuenta(String usuario) {
        return new Cuenta(
                "ID-" + usuario,
                usuario,
                "123456",
                null,
                null
        );
    }

    private Cliente crearCliente(String usuario) {

        Cuenta cuenta = crearCuenta(usuario);

        CuentaPuntos puntos =
                new CuentaPuntos(
                        0,
                        LocalDate.now(),
                        cuenta
                );

        TarjetaVirtual tarjeta =
                new TarjetaVirtual(
                        "T-" + usuario,
                        100000,
                        cuenta
                );

        cuenta.setPuntos(puntos);
        cuenta.setTheTarjetaVirtualCliente(tarjeta);

        return new Cliente(
                "Cliente " + usuario,
                "DOC-" + usuario,
                usuario + "@correo.com",
                20,
                cuenta
        );
    }

    @BeforeEach
    void limpiarDatosGlobales() {

        CineHelper.limpiarCine();

        Cartelera.getInstancia()
                .setListPeliculas(new ArrayList<>());
    }


    // ADMINISTRADOR


    @Test
    void debeCrearAdministradorYVerificarCredenciales() {

        Administrador administrador =
                new Administrador(
                        "Admin",
                        "admin",
                        "123456"
                );

        assertEquals(
                "Admin",
                administrador.getNombre()
        );

        assertEquals(
                "admin",
                administrador.getUsuario()
        );

        assertEquals(
                "admin",
                administrador.getNombreUsuario()
        );

        assertTrue(
                administrador.verificarContrasenia("123456")
        );

        assertFalse(
                administrador.verificarContrasenia("incorrecta")
        );

        administrador.setNombre(
                "Administrador Principal"
        );

        administrador.setUsuario(
                "principal"
        );

        administrador.setContrasenia(
                "654321"
        );

        assertEquals(
                "Administrador Principal",
                administrador.getNombre()
        );

        assertEquals(
                "principal",
                administrador.getUsuario()
        );

        assertTrue(
                administrador.verificarContrasenia("654321")
        );
    }


    // CLIENTE


    @Test
    void debeCrearClienteYVerificarCredenciales() {

        String usuario = unico("cliente");

        Cliente cliente =
                crearCliente(usuario);

        assertEquals(
                "Cliente " + usuario,
                cliente.getNombreCompleto()
        );

        assertEquals(
                "DOC-" + usuario,
                cliente.getId()
        );

        assertEquals(
                usuario + "@correo.com",
                cliente.getEmail()
        );

        assertEquals(
                20,
                cliente.getEdad()
        );

        assertEquals(
                usuario,
                cliente.getNombreUsuario()
        );

        assertTrue(
                cliente.verificarContrasenia("123456")
        );

        assertFalse(
                cliente.verificarContrasenia("000000")
        );

        cliente.setNombreCompleto(
                "Nuevo Nombre"
        );

        cliente.setEmail(
                "nuevo@correo.com"
        );

        cliente.setEdad(25);

        assertEquals(
                "Nuevo Nombre",
                cliente.getNombreCompleto()
        );

        assertEquals(
                "nuevo@correo.com",
                cliente.getEmail()
        );

        assertEquals(
                25,
                cliente.getEdad()
        );
    }


    // CUENTA


    @Test
    void debeCrearCuentaYModificarSusDatos() {

        Cuenta cuenta =
                crearCuenta("usuario1");

        assertEquals(
                "ID-usuario1",
                cuenta.getId()
        );

        assertEquals(
                "usuario1",
                cuenta.getUsuario()
        );

        assertEquals(
                "123456",
                cuenta.getContrasenia()
        );

        assertNotNull(
                cuenta.getListCuentaPuntos()
        );

        assertNotNull(
                cuenta.getListFuncionesPelicula()
        );

        assertTrue(
                cuenta.getListCuentaPuntos().isEmpty()
        );

        assertTrue(
                cuenta.getListFuncionesPelicula().isEmpty()
        );

        cuenta.setId("NUEVO-ID");
        cuenta.setUsuario("nuevoUsuario");
        cuenta.setContrasenia("654321");

        assertEquals(
                "NUEVO-ID",
                cuenta.getId()
        );

        assertEquals(
                "nuevoUsuario",
                cuenta.getUsuario()
        );

        assertEquals(
                "654321",
                cuenta.getContrasenia()
        );
    }


    // TARJETA VIRTUAL


    @Test
    void debeCrearYModificarTarjetaVirtual() {

        Cuenta cuenta =
                crearCuenta("usuario2");

        TarjetaVirtual tarjeta =
                new TarjetaVirtual(
                        "T1",
                        50000,
                        cuenta
                );

        assertEquals(
                "T1",
                tarjeta.getId()
        );

        assertEquals(
                50000,
                tarjeta.getSaldo()
        );

        assertSame(
                cuenta,
                tarjeta.getTheCuentaTarjetaVirtual()
        );

        tarjeta.setId("T2");
        tarjeta.setSaldo(75000);

        assertEquals(
                "T2",
                tarjeta.getId()
        );

        assertEquals(
                75000,
                tarjeta.getSaldo()
        );
    }


    // PELICULA


    @Test
    void debeCrearPeliculaYAgregarFunciones() {

        Pelicula pelicula =
                new Pelicula(
                        "P1",
                        "Avatar",
                        "James Cameron"
                );

        SalaCine sala =
                new SalaCine("S1");

        FuncionPelicula funcion =
                new FuncionPelicula(
                        "F1",
                        LocalDate.of(2026, 12, 1),
                        LocalTime.of(18, 30),
                        sala,
                        pelicula
                );

        assertEquals(
                "P1",
                pelicula.getIdPelicula()
        );

        assertEquals(
                "Avatar",
                pelicula.getNombre()
        );

        assertEquals(
                "James Cameron",
                pelicula.getDirector()
        );

        assertTrue(
                pelicula.getListFuncionesPelicula()
                        .isEmpty()
        );

        pelicula.agregarFuncion(funcion);
        pelicula.agregarFuncion(funcion);

        assertEquals(
                1,
                pelicula.getListFuncionesPelicula()
                        .size()
        );

        assertSame(
                funcion,
                pelicula.getListFuncionesPelicula()
                        .get(0)
        );

        pelicula.setNombre("Avatar 2");
        pelicula.setDirector("Otro Director");

        assertEquals(
                "Avatar 2",
                pelicula.getNombre()
        );

        assertEquals(
                "Otro Director",
                pelicula.getDirector()
        );
    }


    // SALA DE CINE


    @Test
    void debeCrearSalaYAgregarAsientosYFunciones() {

        SalaCine sala =
                new SalaCine("S1");

        Asiento asiento1 =
                new Asiento("A1", sala);

        Asiento asiento2 =
                new Asiento("A2", sala);

        Pelicula pelicula =
                new Pelicula(
                        "P1",
                        "Avatar",
                        "Director"
                );

        FuncionPelicula funcion =
                new FuncionPelicula(
                        "F1",
                        LocalDate.now(),
                        LocalTime.of(19, 0),
                        sala,
                        pelicula
                );

        sala.agregarAsiento(asiento1);
        sala.agregarAsiento(asiento1);
        sala.agregarAsiento(asiento2);

        sala.agregarFuncion(funcion);
        sala.agregarFuncion(funcion);

        assertEquals(
                "S1",
                sala.getIdSalaCine()
        );

        assertEquals(
                2,
                sala.getListAsientosSalaCine()
                        .size()
        );

        assertEquals(
                1,
                sala.getListFuncionesPelicula()
                        .size()
        );

        assertEquals(
                "Sala S1",
                sala.toString()
        );
    }


    // ASIENTO


    @Test
    void debeCompararAsientosCorrectamente() {

        SalaCine sala =
                new SalaCine("S1");

        Asiento asiento1 =
                new Asiento("A1", sala);

        Asiento asiento1Igual =
                new Asiento("A1", sala);

        Asiento asiento2 =
                new Asiento("A2", sala);

        assertEquals(
                asiento1,
                asiento1Igual
        );

        assertEquals(
                asiento1.hashCode(),
                asiento1Igual.hashCode()
        );

        assertNotEquals(
                asiento1,
                asiento2
        );

        assertEquals(
                "A1",
                asiento1.toString()
        );
    }


    // ENTRADA


    @Test
    void debeCrearEntradaYCalcularNombreYPrecio() {

        SalaCine sala =
                new SalaCine("S1");

        Asiento asiento =
                new Asiento("A1", sala);

        Pelicula pelicula =
                new Pelicula(
                        "P1",
                        "Avatar",
                        "Director"
                );

        FuncionPelicula funcion =
                new FuncionPelicula(
                        "F1",
                        LocalDate.now(),
                        LocalTime.of(20, 0),
                        sala,
                        pelicula
                );

        Entrada entrada =
                new Entrada(
                        "E1",
                        15000,
                        asiento,
                        funcion
                );

        assertEquals(
                "E1",
                entrada.getIdEntrada()
        );

        assertEquals(
                15000,
                entrada.getPrecio()
        );

        assertEquals(
                "Entrada - Avatar (Asiento A1)",
                entrada.getNombre()
        );

        assertTrue(
                entrada.toString()
                        .contains("$15000")
        );

        entrada.setIdEntrada("E2");
        entrada.setPrecio(18000);

        assertEquals(
                "E2",
                entrada.getIdEntrada()
        );

        assertEquals(
                18000,
                entrada.getPrecio()
        );
    }


    // FUNCION DE PELICULA


    @Test
    void debeControlarAsientosDeUnaFuncion() {

        SalaCine sala =
                new SalaCine("S1");

        Asiento asiento =
                new Asiento("A1", sala);

        Pelicula pelicula =
                new Pelicula(
                        "P1",
                        "Avatar",
                        "Director"
                );

        FuncionPelicula funcion =
                new FuncionPelicula(
                        "F1",
                        LocalDate.of(2026, 12, 1),
                        LocalTime.of(18, 30),
                        sala,
                        pelicula
                );

        assertFalse(
                funcion.isAsientoOcupado(asiento)
        );

        funcion.ocuparAsiento(asiento);

        assertTrue(
                funcion.isAsientoOcupado(asiento)
        );

        assertEquals(
                1,
                funcion.getAsientosOcupados()
                        .size()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> funcion.ocuparAsiento(asiento)
        );

        funcion.liberarAsiento(asiento);

        assertFalse(
                funcion.isAsientoOcupado(asiento)
        );
    }


    // OBSERVER


    @Test
    void debeNotificarObservadores() {

        SalaCine sala =
                new SalaCine("S1");

        Pelicula pelicula =
                new Pelicula(
                        "P1",
                        "Avatar",
                        "Director"
                );

        FuncionPelicula funcion =
                new FuncionPelicula(
                        "F1",
                        LocalDate.now(),
                        LocalTime.of(18, 0),
                        sala,
                        pelicula
                );

        MensajeObservador observador =
                new MensajeObservador();

        funcion.agregarObservador(observador);
        funcion.agregarObservador(observador);

        funcion.notificarObservadores(
                "La función fue cancelada"
        );

        assertEquals(
                1,
                observador.getCantidadMensajes()
        );

        assertEquals(
                "La función fue cancelada",
                observador.getUltimoMensaje()
        );

        funcion.eliminarObservador(observador);

        funcion.notificarObservadores(
                "Segundo mensaje"
        );

        assertEquals(
                1,
                observador.getCantidadMensajes()
        );
    }


    // CONFITERIA


    @Test
    void debeCrearConfiteriaYModificarDatos() {

        Palomita palomita =
                new Palomita(
                        "Palomitas",
                        5000
                );

        Chocolatina chocolatina =
                new Chocolatina(
                        "Chocolate",
                        3000,
                        TipoChocolatina.CONLECHE
                );

        Gasesosa gaseosa =
                new Gasesosa(
                        "Pepsi",
                        4500,
                        TipoGaseosa.PEPSI
                );

        assertEquals(
                "Palomitas",
                palomita.getNombre()
        );

        assertEquals(
                5000,
                palomita.getPrecio()
        );

        assertEquals(
                TipoChocolatina.CONLECHE,
                chocolatina.getTipoChocolatina()
        );

        assertEquals(
                TipoGaseosa.PEPSI,
                gaseosa.getTipoGaseosa()
        );

        chocolatina.setTipoChocolatina(
                TipoChocolatina.BLANCO
        );

        gaseosa.setTipoGaseosa(
                TipoGaseosa.COCACOLA
        );

        palomita.setNombre(
                "Palomitas grandes"
        );

        palomita.setPrecio(7000);

        assertEquals(
                TipoChocolatina.BLANCO,
                chocolatina.getTipoChocolatina()
        );

        assertEquals(
                TipoGaseosa.COCACOLA,
                gaseosa.getTipoGaseosa()
        );

        assertEquals(
                "Palomitas grandes",
                palomita.getNombre()
        );

        assertEquals(
                7000,
                palomita.getPrecio()
        );
    }


    // COMPOSITE


    @Test
    void debeCrearCarritoAgregarEliminarYCalcularPrecio() {

        Carrito carrito =
                new Carrito("C1");

        Palomita palomita =
                new Palomita(
                        "Palomitas",
                        5000
                );

        Gasesosa gaseosa =
                new Gasesosa(
                        "Pepsi",
                        4500,
                        TipoGaseosa.PEPSI
                );

        assertTrue(
                carrito.estaVacio()
        );

        assertEquals(
                "Carrito C1",
                carrito.getNombre()
        );

        assertEquals(
                0,
                carrito.getPrecio()
        );

        carrito.agregar(palomita);
        carrito.agregar(gaseosa);

        assertFalse(
                carrito.estaVacio()
        );

        assertEquals(
                2,
                carrito.getElementos().size()
        );

        assertEquals(
                9500,
                carrito.getPrecio()
        );

        assertTrue(
                carrito.eliminar(palomita)
        );

        assertEquals(
                4500,
                carrito.getPrecio()
        );

        carrito.vaciar();

        assertTrue(
                carrito.estaVacio()
        );
    }

    @Test
    void debeValidarCarritosAnidados() {

        Carrito principal =
                new Carrito("C1");

        Carrito secundario =
                new Carrito("C2");

        principal.agregar(secundario);

        secundario.agregar(
                new Palomita(
                        "Palomitas",
                        5000
                )
        );

        assertEquals(
                5000,
                principal.getPrecio()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> secundario.agregar(principal)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> principal.agregar(principal)
        );

        assertThrows(
                NullPointerException.class,
                () -> principal.agregar(null)
        );
    }


    // DECORATOR


    @Test
    void debeAplicarDecoradoresDeSalYDulce() {

        ComplementoPalomita producto =
                new Palomita(
                        "Palomitas",
                        5000
                );

        assertEquals(
                "Palomitas",
                producto.getNombre()
        );

        assertEquals(
                5000,
                producto.getPrecio()
        );

        producto =
                new ConSal(producto);

        assertEquals(
                "Palomitas + Sal",
                producto.getNombre()
        );

        assertEquals(
                6000,
                producto.getPrecio()
        );

        producto =
                new ConDulce(producto);

        assertEquals(
                "Palomitas + Sal + Dulce",
                producto.getNombre()
        );

        assertEquals(
                8000,
                producto.getPrecio()
        );

        assertThrows(
                NullPointerException.class,
                () -> new ConSal(null)
        );
    }


    // FACTORY METHOD


    @Test
    void debeCrearPalomitaConFactory() {

        DatosConfiteria datos =
                new DatosConfiteria(
                        "Palomitas",
                        5000
                );

        Confiteria producto =
                new PalomitaFactory()
                        .crearConfiteria(datos);

        assertInstanceOf(
                Palomita.class,
                producto
        );

        assertEquals(
                "Palomitas",
                producto.getNombre()
        );

        assertEquals(
                5000,
                producto.getPrecio()
        );
    }

    @Test
    void debeCrearChocolatinaConFactory() {

        DatosConfiteria datos =
                new DatosConfiteria(
                        "Chocolate",
                        3000,
                        TipoChocolatina.CONLECHE
                );

        Confiteria producto =
                new ChocolatinaFactory()
                        .crearConfiteria(datos);

        assertInstanceOf(
                Chocolatina.class,
                producto
        );

        assertEquals(
                TipoChocolatina.CONLECHE,
                ((Chocolatina) producto)
                        .getTipoChocolatina()
        );
    }

    @Test
    void debeCrearGaseosaConFactory() {

        DatosConfiteria datos =
                new DatosConfiteria(
                        "Pepsi",
                        4500,
                        TipoGaseosa.PEPSI
                );

        Confiteria producto =
                new GaseosaFactory()
                        .crearConfiteria(datos);

        assertInstanceOf(
                Gasesosa.class,
                producto
        );

        assertEquals(
                TipoGaseosa.PEPSI,
                ((Gasesosa) producto)
                        .getTipoGaseosa()
        );
    }

    @Test
    void debeValidarDatosDeConfiteria() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new DatosConfiteria(
                        null,
                        1000
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new DatosConfiteria(
                        "",
                        1000
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new DatosConfiteria(
                        "Producto",
                        -1
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new DatosConfiteria(
                        "Producto",
                        Double.NaN
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new DatosConfiteria(
                        "Producto",
                        Double.POSITIVE_INFINITY
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new PalomitaFactory()
                        .crearConfiteria(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ChocolatinaFactory()
                        .crearConfiteria(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ChocolatinaFactory()
                        .crearConfiteria(
                                new DatosConfiteria(
                                        "C",
                                        1000
                                )
                        )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new GaseosaFactory()
                        .crearConfiteria(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new GaseosaFactory()
                        .crearConfiteria(
                                new DatosConfiteria(
                                        "G",
                                        1000
                                )
                        )
        );
    }


    // ABSTRACT FACTORY


    @Test
    void debeCrearComboCompleto() {

        FabricaCombo fabrica =
                new Combo1();

        Gasesosa gaseosa =
                fabrica.crearGasesosa();

        ComplementoPalomita palomitas =
                fabrica.crearPalomita();

        Chocolatina chocolatina =
                fabrica.crearChocolatina();

        assertEquals(
                "Pepsi",
                gaseosa.getNombre()
        );

        assertEquals(
                4500,
                gaseosa.getPrecio()
        );

        assertEquals(
                TipoGaseosa.PEPSI,
                gaseosa.getTipoGaseosa()
        );

        assertEquals(
                "Palomitas medianas + Sal + Dulce",
                palomitas.getNombre()
        );

        assertEquals(
                8000,
                palomitas.getPrecio()
        );

        assertEquals(
                "Chocolatina con leche",
                chocolatina.getNombre()
        );

        assertEquals(
                3000,
                chocolatina.getPrecio()
        );

        assertEquals(
                TipoChocolatina.CONLECHE,
                chocolatina.getTipoChocolatina()
        );
    }


    // PROXY


    @Test
    void debeConsultarSaldoYRealizarPagoConTarjetaReal() {

        Cuenta cuenta =
                crearCuenta("pago1");

        TarjetaVirtual tarjeta =
                new TarjetaVirtual(
                        "T1",
                        100000,
                        cuenta
                );

        PagoTarjetaVirtual pago =
                new PagoTarjetaVirtual(tarjeta);

        assertEquals(
                100000,
                pago.consultarSaldo()
        );

        pago.pagar(30000);

        assertEquals(
                70000,
                pago.consultarSaldo()
        );

        assertEquals(
                70000,
                tarjeta.getSaldo()
        );
    }

    @Test
    void debeValidarPagoReal() {

        Cuenta cuenta =
                crearCuenta("pago2");

        TarjetaVirtual tarjeta =
                new TarjetaVirtual(
                        "T2",
                        10000,
                        cuenta
                );

        PagoTarjetaVirtual pago =
                new PagoTarjetaVirtual(tarjeta);

        assertThrows(
                IllegalArgumentException.class,
                () -> pago.pagar(0)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> pago.pagar(-100)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> pago.pagar(Double.NaN)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> pago.pagar(
                        Double.POSITIVE_INFINITY
                )
        );

        assertThrows(
                IllegalStateException.class,
                () -> pago.pagar(10001)
        );

        assertThrows(
                NullPointerException.class,
                () -> new PagoTarjetaVirtual(null)
        );
    }

    @Test
    void debeValidarCredencialesYSaldoConProxy() {

        Cuenta cuenta =
                crearCuenta("proxy1");

        TarjetaVirtual tarjeta =
                new TarjetaVirtual(
                        "TP",
                        50000,
                        cuenta
                );

        cuenta.setTheTarjetaVirtualCliente(
                tarjeta
        );

        MedioPago proxy =
                new PagoTarjetaProxy(
                        cuenta,
                        "123456"
                );

        assertEquals(
                50000,
                proxy.consultarSaldo()
        );

        proxy.pagar(20000);

        assertEquals(
                30000,
                proxy.consultarSaldo()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> proxy.pagar(0)
        );

        assertThrows(
                IllegalStateException.class,
                () -> proxy.pagar(40000)
        );

        MedioPago proxyClaveIncorrecta =
                new PagoTarjetaProxy(
                        cuenta,
                        "incorrecta"
                );

        assertThrows(
                SecurityException.class,
                proxyClaveIncorrecta::consultarSaldo
        );

        assertThrows(
                SecurityException.class,
                () -> proxyClaveIncorrecta.pagar(1000)
        );

        Cuenta sinTarjeta =
                crearCuenta("sinTarjeta");

        MedioPago proxySinTarjeta =
                new PagoTarjetaProxy(
                        sinTarjeta,
                        "123456"
                );

        assertThrows(
                IllegalStateException.class,
                proxySinTarjeta::consultarSaldo
        );
    }

    // =========================================================
    // CUENTA DE PUNTOS
    // =========================================================

    @Test
    void debeCalcularPuntosPorValor() {

        assertEquals(
                0,
                CuentaPuntos.puntosPorValor(999)
        );

        assertEquals(
                1,
                CuentaPuntos.puntosPorValor(1000)
        );

        assertEquals(
                5,
                CuentaPuntos.puntosPorValor(5999)
        );

        assertEquals(
                10,
                CuentaPuntos.puntosPorValor(10000)
        );
    }

    @Test
    void debeAcumularConsultarYEliminarPuntos() {

        Cuenta cuenta =
                crearCuenta("puntos1");

        LocalDate fecha =
                LocalDate.of(
                        2026,
                        1,
                        1
                );

        CuentaPuntos.acumular(
                cuenta,
                10,
                fecha,
                "COMPRA-1"
        );

        CuentaPuntos.acumular(
                cuenta,
                5,
                fecha.plusDays(1),
                "COMPRA-2"
        );

        CuentaPuntos.acumular(
                cuenta,
                0,
                fecha,
                "NO-DEBE-CREARSE"
        );

        assertEquals(
                2,
                cuenta.getListCuentaPuntos()
                        .size()
        );

        assertEquals(
                15,
                CuentaPuntos.puntosDisponibles(
                        cuenta,
                        fecha.plusMonths(1)
                )
        );

        int eliminados =
                CuentaPuntos.eliminarVencidos(
                        cuenta,
                        LocalDate.of(
                                2028,
                                1,
                                1
                        )
                );

        assertEquals(
                15,
                eliminados
        );

        assertEquals(
                0,
                cuenta.getListCuentaPuntos()
                        .size()
        );
    }

    @Test
    void debeRedimirPuntosYAplicarValorDeDescuento() {

        Cuenta cuenta =
                crearCuenta("puntos2");

        LocalDate fecha =
                LocalDate.of(
                        2026,
                        1,
                        1
                );

        CuentaPuntos.acumular(
                cuenta,
                20,
                fecha,
                "COMPRA-1"
        );

        CuentaPuntos.acumular(
                cuenta,
                10,
                fecha.plusDays(1),
                "COMPRA-2"
        );

        double descuento =
                CuentaPuntos.redimir(
                        cuenta,
                        15,
                        fecha.plusDays(2)
                );

        assertEquals(
                150,
                descuento
        );

        assertEquals(
                15,
                CuentaPuntos.puntosDisponibles(
                        cuenta,
                        fecha.plusDays(2)
                )
        );
    }

    @Test
    void debeValidarRedencionYVencimientoDePuntos() {

        Cuenta cuenta =
                crearCuenta("puntos3");

        LocalDate fecha =
                LocalDate.of(
                        2025,
                        1,
                        1
                );

        CuentaPuntos lote =
                new CuentaPuntos(
                        10,
                        fecha,
                        cuenta,
                        "COMPRA-X"
                );

        cuenta.getListCuentaPuntos()
                .add(lote);

        assertTrue(
                lote.estaVencido(
                        LocalDate.of(
                                2026,
                                1,
                                1
                        )
                )
        );

        assertFalse(
                lote.estaVencido(
                        LocalDate.of(
                                2025,
                                12,
                                31
                        )
                )
        );

        assertEquals(
                "COMPRA-X",
                lote.getIdCompraOrigen()
        );

        assertEquals(
                fecha.plusYears(1),
                lote.getFechaVencimiento()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> CuentaPuntos.redimir(
                        cuenta,
                        0,
                        LocalDate.of(
                                2025,
                                1,
                                1
                        )
                )
        );

        assertThrows(
                IllegalStateException.class,
                () -> CuentaPuntos.redimir(
                        cuenta,
                        20,
                        LocalDate.of(
                                2025,
                                1,
                                1
                        )
                )
        );

        assertEquals(
                10,
                CuentaPuntos.eliminarVencidos(
                        cuenta,
                        LocalDate.of(
                                2026,
                                1,
                                1
                        )
                )
        );

        assertTrue(
                cuenta.getListCuentaPuntos()
                        .isEmpty()
        );
    }

    @Test
    void debeRevertirPuntosDeUnaCompra() {

        Cuenta cuenta =
                crearCuenta("puntos4");

        LocalDate fecha =
                LocalDate.of(
                        2026,
                        1,
                        1
                );

        CuentaPuntos.acumular(
                cuenta,
                10,
                fecha,
                "COMPRA-1"
        );

        CuentaPuntos.acumular(
                cuenta,
                5,
                fecha,
                "COMPRA-2"
        );

        CuentaPuntos.revertir(
                cuenta,
                "COMPRA-1"
        );

        assertEquals(
                1,
                cuenta.getListCuentaPuntos()
                        .size()
        );

        assertEquals(
                "COMPRA-2",
                cuenta.getListCuentaPuntos()
                        .get(0)
                        .getIdCompraOrigen()
        );

        assertEquals(
                5,
                CuentaPuntos.puntosDisponibles(
                        cuenta,
                        fecha
                )
        );
    }

    // =========================================================
    // COMPRA
    // =========================================================

    @Test
    void debeCalcularValorPagadoYRegistrarRedencion() {

        Carrito carrito =
                new Carrito("C1");

        carrito.agregar(
                new Palomita(
                        "Palomitas",
                        5000
                )
        );

        Compra compra =
                new Compra(
                        "COMPRA-1",
                        5000,
                        LocalDate.of(
                                2026,
                                11,
                                17
                        ),
                        LocalTime.of(
                                15,
                                0
                        ),
                        carrito
                );

        assertEquals(
                5000,
                compra.getValorTotal()
        );

        assertEquals(
                5000,
                compra.getValorPagado()
        );

        compra.registrarRedencion(
                100,
                1000
        );

        assertEquals(
                100,
                compra.getPuntosRedimidos()
        );

        assertEquals(
                1000,
                compra.getDescuentoPuntos()
        );

        assertEquals(
                4000,
                compra.getValorPagado()
        );
    }

    // =========================================================
    // STRATEGY
    // =========================================================

    @Test
    void debeSeleccionarPoliticaDeReembolso() {

        LocalDateTime ahora =
                LocalDateTime.of(
                        2026,
                        11,
                        17,
                        10,
                        0
                );

        assertEquals(
                100,
                Compra.seleccionarPolitica(
                        ahora,
                        ahora.plusHours(24)
                ).getPorcentaje()
        );

        assertEquals(
                100,
                Compra.seleccionarPolitica(
                        ahora,
                        ahora.plusHours(30)
                ).getPorcentaje()
        );

        assertEquals(
                80,
                Compra.seleccionarPolitica(
                        ahora,
                        ahora.plusHours(5)
                ).getPorcentaje()
        );

        assertEquals(
                80,
                Compra.seleccionarPolitica(
                        ahora,
                        ahora.plusHours(2)
                ).getPorcentaje()
        );

        assertEquals(
                0,
                Compra.seleccionarPolitica(
                        ahora,
                        ahora.plusHours(1)
                ).getPorcentaje()
        );

        assertEquals(
                1000,
                Compra.REEMBOLSO_100
                        .calcular(1000)
        );

        assertEquals(
                800,
                Compra.REEMBOLSO_80
                        .calcular(1000)
        );

        assertEquals(
                0,
                Compra.REEMBOLSO_0
                        .calcular(1000)
        );
    }

    @Test
    void debeCancelarCompraYReembolsarSegunStrategy() {

        String usuario =
                unico("compra");

        Cuenta cuenta =
                crearCuenta(usuario);

        TarjetaVirtual tarjeta =
                new TarjetaVirtual(
                        "T-" + usuario,
                        50000,
                        cuenta
                );

        cuenta.setTheTarjetaVirtualCliente(
                tarjeta
        );

        SalaCine sala =
                new SalaCine("S1");

        Pelicula pelicula =
                new Pelicula(
                        "P1",
                        "Avatar",
                        "Director"
                );

        LocalDateTime inicio =
                LocalDateTime.of(
                        2026,
                        11,
                        18,
                        12,
                        0
                );

        FuncionPelicula funcion =
                new FuncionPelicula(
                        "F1",
                        inicio.toLocalDate(),
                        inicio.toLocalTime(),
                        sala,
                        pelicula
                );

        Carrito carrito =
                new Carrito("C1");

        Compra compra =
                new Compra(
                        "COMPRA-1",
                        20000,
                        LocalDate.of(
                                2026,
                                11,
                                17
                        ),
                        LocalTime.of(
                                10,
                                0
                        ),
                        carrito,
                        cuenta,
                        funcion
                );

        compra.registrarRedencion(
                10,
                100
        );

        CuentaPuntos.acumular(
                cuenta,
                20,
                LocalDate.of(
                        2026,
                        11,
                        17
                ),
                "COMPRA-1"
        );

        double reembolso =
                compra.cancelar(
                        LocalDateTime.of(
                                2026,
                                11,
                                17,
                                10,
                                0
                        )
                );

        assertEquals(
                19900,
                reembolso
        );

        assertTrue(
                compra.isCancelada()
        );

        assertEquals(
                100,
                compra.getPorcentajeReembolso()
        );

        assertEquals(
                19900,
                compra.getMontoReembolsado()
        );

        assertEquals(
                69900,
                tarjeta.getSaldo()
        );

        assertEquals(
                10,
                CuentaPuntos.puntosDisponibles(
                        cuenta,
                        LocalDate.of(
                                2026,
                                11,
                                17
                        )
                )
        );

        assertThrows(
                IllegalStateException.class,
                () -> compra.cancelar(
                        LocalDateTime.of(
                                2026,
                                11,
                                17,
                                10,
                                0
                        )
                )
        );
    }

    @Test
    void noDebeCancelarCompraCuandoLaFuncionYaInicio() {

        Cuenta cuenta =
                crearCuenta("cancelacion");

        TarjetaVirtual tarjeta =
                new TarjetaVirtual(
                        "T",
                        50000,
                        cuenta
                );

        cuenta.setTheTarjetaVirtualCliente(
                tarjeta
        );

        LocalDateTime inicio =
                LocalDateTime.of(
                        2026,
                        11,
                        17,
                        12,
                        0
                );

        FuncionPelicula funcion =
                new FuncionPelicula(
                        "F1",
                        inicio.toLocalDate(),
                        inicio.toLocalTime(),
                        new SalaCine("S1"),
                        new Pelicula(
                                "P1",
                                "Pelicula",
                                "Director"
                        )
                );

        Compra compra =
                new Compra(
                        "C1",
                        10000,
                        LocalDate.of(
                                2026,
                                11,
                                17
                        ),
                        LocalTime.of(
                                10,
                                0
                        ),
                        new Carrito("C"),
                        cuenta,
                        funcion
                );

        assertFalse(
                compra.puedeCancelarse(inicio)
        );

        assertThrows(
                IllegalStateException.class,
                () -> compra.cancelar(inicio)
        );
    }

    // =========================================================
    // SINGLETON CINE
    // =========================================================

    @Test
    void debeMantenerUnaSolaInstanciaDeCine() {

        assertSame(
                model.Cine.getInstancia(),
                model.Cine.getInstancia()
        );

        assertEquals(
                "CinemaUQ",
                model.Cine.getInstancia()
                        .getNombre()
        );

        assertEquals(
                "123456789",
                model.Cine.getInstancia()
                        .getNit()
        );
    }

    @Test
    void debeAgregarPeliculasYSalasAlCine() {

        model.Cine cine =
                model.Cine.getInstancia();

        Pelicula pelicula =
                new Pelicula(
                        "P1",
                        "Avatar",
                        "Director"
                );

        SalaCine sala =
                new SalaCine("S1");

        cine.agregarPelicula(pelicula);
        cine.agregarPelicula(pelicula);

        cine.agregarSala(sala);
        cine.agregarSala(sala);

        assertEquals(
                1,
                cine.getListPeliculas()
                        .size()
        );

        assertEquals(
                1,
                cine.getListSalasCine()
                        .size()
        );

        assertSame(
                pelicula,
                cine.obtenerCartelera()
                        .get(0)
        );
    }

    // =========================================================
    // ITERATOR / CARTELERA
    // =========================================================

    @Test
    void debeRecorrerCarteleraConIterator() {

        Cartelera cartelera =
                Cartelera.getInstancia();

        Pelicula p1 =
                new Pelicula(
                        "P1",
                        "Avatar",
                        "Director 1"
                );

        Pelicula p2 =
                new Pelicula(
                        "P2",
                        "Titanic",
                        "Director 2"
                );

        cartelera.agregarPelicula(p1);
        cartelera.agregarPelicula(p2);

        Iterator<Pelicula> iterator =
                cartelera.iterator();

        assertTrue(
                iterator.hasNext()
        );

        assertSame(
                p1,
                iterator.next()
        );

        assertTrue(
                iterator.hasNext()
        );

        assertSame(
                p2,
                iterator.next()
        );

        assertFalse(
                iterator.hasNext()
        );

        assertThrows(
                java.util.NoSuchElementException.class,
                iterator::next
        );
    }

    @Test
    void debeMantenerSingletonDeCartelera() {

        assertSame(
                Cartelera.getInstancia(),
                Cartelera.getInstancia()
        );
    }

    // =========================================================
    // GESTOR DE USUARIOS
    // =========================================================

    @Test
    void debeRegistrarYBuscarUsuarios() {

        GestorUsuarios gestor =
                GestorUsuarios.getInstancia();

        String usuario =
                unico("gestor");

        Cliente cliente =
                crearCliente(usuario);

        Administrador administrador =
                new Administrador(
                        "Admin Test",
                        "admin" + usuario,
                        "abc123"
                );

        gestor.registrarCliente(cliente);
        gestor.registrarAdministrador(administrador);

        assertSame(
                cliente,
                gestor.buscarCliente(usuario)
        );

        assertSame(
                administrador,
                gestor.buscarAdministrador(
                        "admin" + usuario
                )
        );

        assertTrue(
                gestor.existeUsuario(usuario)
        );

        assertTrue(
                gestor.existeUsuario(
                        "admin" + usuario
                )
        );

        assertTrue(
                gestor.existeDocumento(
                        "DOC-" + usuario
                )
        );

        assertTrue(
                gestor.existeCorreo(
                        usuario + "@correo.com"
                )
        );

        assertFalse(
                gestor.existeUsuario(
                        "no-existe-" + usuario
                )
        );

        assertFalse(
                gestor.existeDocumento(
                        "NO-DOC"
                )
        );

        assertFalse(
                gestor.existeCorreo(
                        "no@correo.com"
                )
        );
    }

    // =========================================================
    // AUTENTICACION
    // =========================================================

    @Test
    void debeAutenticarAdministrador() {

        AutenticacionService servicio =
                new AutenticacionService();

        Object resultado =
                servicio.autenticar(
                        "admin",
                        "admin123"
                );

        assertNotNull(
                resultado
        );

        assertInstanceOf(
                Administrador.class,
                resultado
        );

        assertEquals(
                "admin",
                ((Administrador) resultado)
                        .getUsuario()
        );
    }

    @Test
    void debeAutenticarClienteRegistrado() {

        String usuario =
                unico("auth");

        Cliente cliente =
                crearCliente(usuario);

        GestorUsuarios.getInstancia()
                .registrarCliente(cliente);

        AutenticacionService servicio =
                new AutenticacionService();

        Object resultado =
                servicio.autenticar(
                        usuario,
                        "123456"
                );

        assertSame(
                cliente,
                resultado
        );

        assertNull(
                servicio.autenticar(
                        usuario,
                        "incorrecta"
                )
        );

        assertNull(
                servicio.autenticar(
                        "usuario-inexistente",
                        "123456"
                )
        );
    }

    // =========================================================
    // REGISTRO SERVICE
    // =========================================================

    @Test
    void debeRegistrarClienteCompleto() {

        RegistroService servicio =
                new RegistroService();

        String sufijo =
                unico("registro");

        Cliente cliente =
                servicio.registrarCliente(
                        "Juan Perez",
                        "DOC-" + sufijo,
                        20,
                        "juan" + sufijo
                                + "@correo.com",
                        "usuario" + sufijo,
                        "123456"
                );

        assertNotNull(
                cliente
        );

        assertEquals(
                "Juan Perez",
                cliente.getNombreCompleto()
        );

        assertEquals(
                20,
                cliente.getEdad()
        );

        assertNotNull(
                cliente.getCuenta()
        );

        assertNotNull(
                cliente.getCuenta()
                        .getPuntos()
        );

        assertNotNull(
                cliente.getCuenta()
                        .getTheTarjetaVirtualCliente()
        );

        assertEquals(
                0,
                cliente.getCuenta()
                        .getPuntos()
                        .getPuntos()
        );

        assertEquals(
                0,
                cliente.getCuenta()
                        .getTheTarjetaVirtualCliente()
                        .getSaldo()
        );
    }

    @Test
    void debeValidarRegistroDeCliente() {

        RegistroService servicio =
                new RegistroService();

        String s =
                unico("validacion");

        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrarCliente(
                        "",
                        "D" + s,
                        20,
                        "a@b.com",
                        "u" + s,
                        "123456"
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrarCliente(
                        "Juan",
                        "",
                        20,
                        "a@b.com",
                        "u" + s,
                        "123456"
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrarCliente(
                        "Juan",
                        "D" + s,
                        0,
                        "a@b.com",
                        "u" + s,
                        "123456"
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrarCliente(
                        "Juan",
                        "D" + s,
                        121,
                        "a@b.com",
                        "u" + s,
                        "123456"
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrarCliente(
                        "Juan",
                        "D" + s,
                        20,
                        "correo-invalido",
                        "u" + s,
                        "123456"
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrarCliente(
                        "Juan",
                        "D" + s,
                        20,
                        "a@b.com",
                        "u" + s,
                        "123"
                )
        );

        String usuario =
                "duplicado" + s;

        String documento =
                "DOC" + s;

        String correo =
                "dup" + s + "@correo.com";

        servicio.registrarCliente(
                "Primero",
                documento,
                20,
                correo,
                usuario,
                "123456"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrarCliente(
                        "Segundo",
                        "DOC2" + s,
                        20,
                        "otro" + s
                                + "@correo.com",
                        usuario,
                        "123456"
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrarCliente(
                        "Tercero",
                        documento,
                        20,
                        "tercero" + s
                                + "@correo.com",
                        "usuario3" + s,
                        "123456"
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> servicio.registrarCliente(
                        "Cuarto",
                        "DOC4" + s,
                        20,
                        correo,
                        "usuario4" + s,
                        "123456"
                )
        );
    }

    // =========================================================
    // SESION
    // =========================================================

    @Test
    void debeManejarSesionComoSingleton() {

        Sesion sesion1 =
                Sesion.getInstancia();

        Sesion sesion2 =
                Sesion.getInstancia();

        assertSame(
                sesion1,
                sesion2
        );

        Administrador admin =
                new Administrador(
                        "Admin",
                        "adminTest",
                        "123456"
                );

        sesion1.iniciarSesion(admin);

        assertTrue(
                sesion2.haySesionActiva()
        );

        assertSame(
                admin,
                sesion2.getUsuarioActual()
        );

        sesion2.cerrarSesion();

        assertFalse(
                sesion1.haySesionActiva()
        );

        assertNull(
                sesion1.getUsuarioActual()
        );
    }

    // =========================================================
    // FACADE
    // =========================================================

    @Test
    void debeRealizarCompraConCompraFachada() {

        model.Cine cine =
                model.Cine.getInstancia();

        Cuenta cuenta =
                crearCuenta("fachada1");

        TarjetaVirtual tarjeta =
                new TarjetaVirtual(
                        "TF",
                        50000,
                        cuenta
                );

        cuenta.setTheTarjetaVirtualCliente(
                tarjeta
        );

        Carrito carrito =
                new Carrito("CF");

        carrito.agregar(
                new Palomita(
                        "Palomitas",
                        10000
                )
        );

        int comprasAntes =
                cine.getListCompras()
                        .size();

        CompraFachada fachada =
                new CompraFachada(
                        cuenta,
                        "123456",
                        carrito,
                        10000
                );

        fachada.hacerCompra();

        assertEquals(
                40000,
                tarjeta.getSaldo()
        );

        assertEquals(
                comprasAntes + 1,
                cine.getListCompras()
                        .size()
        );

        assertEquals(
                10000,
                cine.getListCompras()
                        .get(
                                cine.getListCompras()
                                        .size() - 1
                        )
                        .getValorTotal()
        );
    }

    @Test
    void debeValidarDatosDeCompraFachada() {

        Cuenta cuenta =
                crearCuenta("fachada2");

        TarjetaVirtual tarjeta =
                new TarjetaVirtual(
                        "TF2",
                        50000,
                        cuenta
                );

        cuenta.setTheTarjetaVirtualCliente(
                tarjeta
        );

        Carrito carrito =
                new Carrito("CF2");

        assertThrows(
                IllegalArgumentException.class,
                () -> new CompraFachada(
                        null,
                        "123456",
                        carrito,
                        1000
                ).hacerCompra()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new CompraFachada(
                        cuenta,
                        "123456",
                        null,
                        1000
                ).hacerCompra()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new CompraFachada(
                        cuenta,
                        "123456",
                        carrito,
                        0
                ).hacerCompra()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new CompraFachada(
                        cuenta,
                        "123456",
                        carrito,
                        -1
                ).hacerCompra()
        );
    }

    // =========================================================
    // OBSERVER DE CUENTA
    // =========================================================

    @Test
    void debeActualizarCuentaComoObservador() {

        Cuenta cuenta =
                crearCuenta("observer1");

        assertDoesNotThrow(
                () -> cuenta.actualizar(
                        "Mensaje de prueba"
                )
        );

        assertEquals(
                "observer1",
                cuenta.getUsuario()
        );
    }

    // =========================================================
    // CLASE AUXILIAR PARA OBSERVER
    // =========================================================

    private static class MensajeObservador
            implements Observador {

        private int cantidadMensajes;
        private String ultimoMensaje;

        @Override
        public void actualizar(
                String mensaje
        ) {
            cantidadMensajes++;
            ultimoMensaje = mensaje;
        }

        int getCantidadMensajes() {
            return cantidadMensajes;
        }

        String getUltimoMensaje() {
            return ultimoMensaje;
        }
    }

    // =========================================================
    // LIMPIEZA DEL SINGLETON CINE
    // =========================================================

    private static class CineHelper {

        static void limpiarCine() {

            model.Cine cine =
                    model.Cine.getInstancia();

            cine.setListClientes(
                    new ArrayList<>()
            );

            cine.setListCuentasClientes(
                    new ArrayList<>()
            );

            cine.setListCompras(
                    new ArrayList<>()
            );

            cine.setListAdministradores(
                    new ArrayList<>()
            );

            cine.setListConfiterias(
                    new ArrayList<>()
            );

            cine.setListSalasCine(
                    new ArrayList<>()
            );

            cine.setListPeliculas(
                    new ArrayList<>()
            );
        }
    }
}