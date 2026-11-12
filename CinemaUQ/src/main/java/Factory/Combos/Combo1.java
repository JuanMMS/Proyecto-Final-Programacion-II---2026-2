package Factory.Combos;

import Factory.DatosConfiteria;
import Factory.FabricaCombo;
import model.Chocolatina;
import model.Gasesosa;
import model.Palomita;
import model.TipoGaseosa;
import model.Gasesosa;

public class Combo1 implements FabricaCombo {
    @Override
    public Gasesosa crearGasesosa() {
        //Crear gaseosa
        Gasesosa gaseosa = new Gasesosa("PepsiCola", 4500, TipoGaseosa.PEPSI);

    }

    @Override
    public Palomita crearPalomita() {
        return null;
    }

    @Override
    public Chocolatina crearChocolatina() {
        return null;
    }
}
