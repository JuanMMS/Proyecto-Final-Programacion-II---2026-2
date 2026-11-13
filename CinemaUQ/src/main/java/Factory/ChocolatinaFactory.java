package Factory;

import model.Chocolatina;
import model.Confiteria;

public class ChocolatinaFactory extends ConfiteriaFactory {


    /**
     * Metodo que permite crear una chocolatina utilizanco la información general como la especifica para chocolatina
     * @param datosConfiteria
     * @return
     */
    @Override
    public Confiteria crearConfiteria(DatosConfiteria datosConfiteria) { return new Chocolatina(datosConfiteria.getNombre(), datosConfiteria.getPrecio(), datosConfiteria.getTipoChocolatina());}
}
