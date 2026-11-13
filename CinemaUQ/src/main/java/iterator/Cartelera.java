
package iterator;

import model.Pelicula;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para agrupar las peliculas disponibles. Utiliza Iterator.
 */
public class Cartelera implements Iterable<Pelicula> {

    // Lista para las peliculas
    private ArrayList<Pelicula> listPeliculas = new ArrayList<>();

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
