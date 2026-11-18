package app;

import model.Asiento;
import model.Cine;
import model.FuncionPelicula;
import model.Pelicula;
import model.SalaCine;

import java.time.LocalDate;
import java.time.LocalTime;

/** Carga un catálogo mínimo para que la aplicación pueda probarse recién iniciada. */
public final class DatosIniciales {
    private static boolean cargados;
    private DatosIniciales() { }

    public static synchronized void cargar() {
        if (cargados) return;
        Cine cine = Cine.getInstancia();
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
        cargados = true;
    }
}
