package Factory;

import model.Confiteria;
import model.Palomita;

public class PalomitaFactory extends ConfiteriaFactory{

    @Override
    public Confiteria crearConfiteria(DatosConfiteria datosConfiteria) {
        return new Palomita(datosConfiteria.getNombre(), datosConfiteria.getPrecio());
    }
}
