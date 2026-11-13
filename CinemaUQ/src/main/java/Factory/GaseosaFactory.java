package Factory;

import model.Confiteria;
import model.Gasesosa;

public class GaseosaFactory extends ConfiteriaFactory{


    /**
     * Metodo que permite crear una gaseosa utilizando información general como la especifica
     * @param datosConfiteria
     * @return
     */
    @Override
    public Confiteria crearConfiteria(DatosConfiteria datosConfiteria) { return new Gasesosa(datosConfiteria.getNombre(), datosConfiteria.getPrecio(), datosConfiteria.getTipoGaseosa());
    }
}

