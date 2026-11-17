package factory;

import model.Confiteria;

/** Factory Method para crear productos concretos de confitería. */
public abstract class ConfiteriaFactory {
    public abstract Confiteria crearConfiteria(factory.DatosConfiteria datosConfiteria);
}
