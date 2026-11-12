package Factory;

import model.Chocolatina;
import model.Confiteria;
import model.Gasesosa;
import model.Palomita;

public interface FabricaCombo {

    Gasesosa crearGasesosa();
    Palomita crearPalomita();
    Chocolatina crearChocolatina();
}
