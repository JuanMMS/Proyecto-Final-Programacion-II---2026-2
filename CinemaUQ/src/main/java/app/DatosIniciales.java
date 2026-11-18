package app;

import model.Asiento;
import model.Cine;
import model.FuncionPelicula;
import model.Pelicula;
import model.SalaCine;
import model.Cliente;
import model.Cuenta;
import model.CuentaPuntos;
import model.TarjetaVirtual;
import service.GestorUsuarios;
import java.time.LocalDate;
import java.time.LocalTime;

/** Carga un catálogo mínimo para que la aplicación pueda probarse recién iniciada. */
public final class DatosIniciales {
    private static boolean cargados;
    private DatosIniciales() { }

    public static synchronized void cargar() {
        if (cargados) return;
        Cine cine = Cine.getInstancia();

        GestorUsuarios gestor = GestorUsuarios.getInstancia();

// Crear clientes precargados si todavía no existen
        if (gestor.obtenerClientes().isEmpty()) {

            // CLIENTE 1
            Cuenta cuenta1 = new Cuenta(
                    "CU-001", "ana", "123456", null, null
            );

            CuentaPuntos puntos1 = new CuentaPuntos(
                    100, LocalDate.now(), cuenta1
            );

            cuenta1.setPuntos(puntos1);
            cuenta1.getListCuentaPuntos().add(puntos1);

            TarjetaVirtual tarjeta1 = new TarjetaVirtual(
                    "TV-001", 100000, cuenta1
            );

            cuenta1.setTheTarjetaVirtualCliente(tarjeta1);

            Cliente cliente1 = new Cliente(
                    "ana velasquez",
                    "109490001",
                    "ana@gmail.com",
                    20,
                    cuenta1
            );

            gestor.registrarCliente(cliente1);
            cine.getListClientes().add(cliente1);
            cine.getListCuentasClientes().add(cuenta1);


            // CLIENTE 2
            Cuenta cuenta2 = new Cuenta(
                    "CU-002", "juan", "123456", null, null
            );

            CuentaPuntos puntos2 = new CuentaPuntos(
                    50, LocalDate.now(), cuenta2
            );

            cuenta2.setPuntos(puntos2);
            cuenta2.getListCuentaPuntos().add(puntos2);

            TarjetaVirtual tarjeta2 = new TarjetaVirtual(
                    "TV-002", 80000, cuenta2
            );

            cuenta2.setTheTarjetaVirtualCliente(tarjeta2);

            Cliente cliente2 = new Cliente(
                    "Juan Perez",
                    "109490002",
                    "juan@gmail.com",
                    22,
                    cuenta2
            );

            gestor.registrarCliente(cliente2);
            cine.getListClientes().add(cliente2);
            cine.getListCuentasClientes().add(cuenta2);
        }

        if (cine.getListPeliculas().isEmpty()) {
            Pelicula pelicula = new Pelicula("PEL-001", "Interstellar", "Christopher Nolan");
            SalaCine sala = new SalaCine("1");
            for (int fila = 0; fila < 5; fila++) {
                char letra = (char) ('A' + fila);
                for (int numero = 1; numero <= 8; numero++) {
                    sala.agregarAsiento(new Asiento(letra + String.valueOf(numero), sala));
                }
            }
            FuncionPelicula funcion = new FuncionPelicula("FUN-001", LocalDate.now().plusDays(1),
                    LocalTime.of(19, 0), sala, pelicula);
            pelicula.agregarFuncion(funcion);
            sala.agregarFuncion(funcion);
            cine.agregarSala(sala);
            cine.agregarPelicula(pelicula);
        }




        boolean existeOdisea = cine.getListPeliculas()
                .stream()
                .anyMatch(p -> p.getIdPelicula().equals("PEL-002"));

        if (!existeOdisea) {

            Pelicula pelicula2 = new Pelicula(
                    "PEL-002",
                    "La Odisea",
                    "Christopher Nolan"
            );

            // Crear sala 2
            SalaCine sala2 = new SalaCine("2");

            // Crear 40 asientos
            for (int fila = 0; fila < 5; fila++) {

                char letra = (char) ('A' + fila);

                for (int numero = 1; numero <= 8; numero++) {

                    sala2.agregarAsiento(
                            new Asiento(
                                    letra + String.valueOf(numero),
                                    sala2
                            )
                    );
                }
            }

            // Crear función
            FuncionPelicula funcion2 = new FuncionPelicula(
                    "FUN-002",
                    LocalDate.now().plusDays(1),
                    LocalTime.of(21, 0),
                    sala2,
                    pelicula2
            );

            pelicula2.agregarFuncion(funcion2);
            sala2.agregarFuncion(funcion2);

            // Registrar sala y película
            cine.agregarSala(sala2);
            cine.agregarPelicula(pelicula2);
        }

        cargados = true;
    }
}
