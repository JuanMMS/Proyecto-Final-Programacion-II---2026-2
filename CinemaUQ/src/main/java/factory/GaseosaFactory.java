package factory;

import model.Confiteria;
import model.Gasesosa;

public class GaseosaFactory extends factory.ConfiteriaFactory {
    @Override
    public Confiteria crearConfiteria(factory.DatosConfiteria datos) {
        if (datos == null || datos.getTipoGaseosa() == null) {
            throw new IllegalArgumentException("Debe indicar los datos y el tipo de gaseosa.");
        }
        return new Gasesosa(datos.getNombre(), datos.getPrecio(), datos.getTipoGaseosa());
    }
}
