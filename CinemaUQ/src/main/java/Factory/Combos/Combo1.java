package Factory.Combos;

import Factory.DatosConfiteria;
import Factory.FabricaCombo;
import decorator.Complemento;
import decorator.ComplementoPalomita;
import decorator.ConDulce;
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
        return gaseosa;
    }

    @Override
    public Palomita crearPalomita() {
        //crear palomita de sal y caramelo
        Palomita palomita = new Palomita("Palomitas pro max", 5000);
        ComplementoPalomita dulce = new ConDulce(palomita);
        ComplementoPalomita mixtas = new ConDulce(dulce);
        return (Palomita) mixtas;
    }

    @Override
    public Chocolatina crearChocolatina() {
        return null;
    }
}
