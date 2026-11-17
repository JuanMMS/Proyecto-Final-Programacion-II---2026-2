package factory;

import model.Confiteria;
import model.Palomita;

public class PalomitaFactory extends factory.ConfiteriaFactory {
    @Override
    public Confiteria crearConfiteria(factory.DatosConfiteria datos) {
        validar(datos);
        return new Palomita(datos.getNombre(), datos.getPrecio());
    }

    private void validar(factory.DatosConfiteria datos) {
        if (datos == null) throw new IllegalArgumentException("Los datos del producto son obligatorios.");
    }
}
