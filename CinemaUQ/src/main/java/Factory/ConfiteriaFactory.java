package Factory;

import model.Confiteria;

public abstract class ConfiteriaFactory {
    //factory method
    public abstract Confiteria crearConfiteria(DatosConfiteria datosConfiteria);

//    public void crearconfiteria(String nombre, double precio) {
  //      Confiteria confiteria = crearConfiteria();
    //}
}
