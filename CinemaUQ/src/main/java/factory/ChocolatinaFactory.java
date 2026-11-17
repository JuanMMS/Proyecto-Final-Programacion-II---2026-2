package factory;

import model.Chocolatina;
import model.Confiteria;

public class ChocolatinaFactory extends factory.ConfiteriaFactory {
    @Override
    public Confiteria crearConfiteria(factory.DatosConfiteria datos) {
        if (datos == null || datos.getTipoChocolatina() == null) {
            throw new IllegalArgumentException("Debe indicar los datos y el tipo de chocolatina.");
        }
        return new Chocolatina(datos.getNombre(), datos.getPrecio(), datos.getTipoChocolatina());
    }
}
