
package iterator;

import model.Cine;
import model.Pelicula;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para agrupar las peliculas disponibles. Utiliza Iterator y Singleton
 */
public class Cartelera implements Iterable<Pelicula> {
    // Instancia unica
    private static Cartelera instancia;

    // Lista para las peliculas
    private ArrayList<Pelicula> listPeliculas;

    //Constructor privado para el singleton
    private Cartelera() {
        listPeliculas = new ArrayList<>();
    }

    /**
     * Funcion para obtener una instancia unica de la cartelera
     * @return instantica unica de la cartelera
     */
    public static Cartelera getInstancia() {
        if (instancia == null) {
            instancia = new Cartelera();
        }
        return instancia;
    }

    /**
     * Funcion para agregar nuevas peliculas a la lista.
     * @param pelicula pelicula a agregar
     */
    public void agregarPelicula(Pelicula pelicula) {
        listPeliculas.add(pelicula);
    }

    /**
     * Funcion para obtener las peliculas utilizando Iterator.
     * @return Iterator de peliculas
     */
    @Override
    public Iterator<Pelicula> iterator() {
        return new Iterator<Pelicula>() {

            private int indice = 0;
            private Pelicula siguiente = buscarSiguiente();

            private Pelicula buscarSiguiente() {
                while (indice < listPeliculas.size()) {
                    Pelicula pelicula = listPeliculas.get(indice);
                    indice++;

                    return pelicula;
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return siguiente != null;
            }

            @Override
            public Pelicula next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                Pelicula actual = siguiente;
                siguiente = buscarSiguiente();

                return actual;
            }
        };
    }

    //Getters y setters
    public ArrayList<Pelicula> getListPeliculas() {
        return listPeliculas;
    }

    public void setListPeliculas(ArrayList<Pelicula> listPeliculas) {
        this.listPeliculas = listPeliculas;
    }
}
