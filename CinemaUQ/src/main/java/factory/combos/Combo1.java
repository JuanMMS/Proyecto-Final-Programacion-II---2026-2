package Factory.combos;

import composite.Elemento;
import decorator.ComplementoPalomita;
import decorator.ConDulce;
import decorator.ConSal;
import Factory.FabricaCombo;
import model.Chocolatina;
import model.Gasesosa;
import model.Palomita;
import model.TipoChocolatina;
import model.TipoGaseosa;

/** Combo clásico: gaseosa, palomitas con sal y dulce, y chocolatina. */
public class Combo1 implements FabricaCombo {
    @Override
    public Gasesosa crearGasesosa() {
        return new Gasesosa("Pepsi", 4500, TipoGaseosa.PEPSI);
    }

    @Override
    public ComplementoPalomita crearPalomita() {
        ComplementoPalomita palomitas = new Palomita("Palomitas medianas", 5000);
        palomitas = new ConSal(palomitas);
        palomitas = new ConDulce(palomitas);
        return palomitas;
    }

    @Override
    public Chocolatina crearChocolatina() {
        return new Chocolatina("Chocolatina con leche", 3000, TipoChocolatina.CONLECHE);
    }

}
