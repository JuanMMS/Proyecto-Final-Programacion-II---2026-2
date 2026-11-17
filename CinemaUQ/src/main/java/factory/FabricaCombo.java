package factory;

import decorator.ComplementoPalomita;
import model.Chocolatina;
import model.Gasesosa;

/** Abstract Factory: crea una familia de productos que conforman un combo. */
public interface FabricaCombo {
    Gasesosa crearGasesosa();
    ComplementoPalomita crearPalomita();
    Chocolatina crearChocolatina();
}
