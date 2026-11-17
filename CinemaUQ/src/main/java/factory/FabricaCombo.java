package factory;

import model.Chocolatina;
import model.Gasesosa;
import model.Palomita;

public interface FabricaCombo {

    Gasesosa crearGasesosa();
    Palomita crearPalomita();
    Chocolatina crearChocolatina();
}
