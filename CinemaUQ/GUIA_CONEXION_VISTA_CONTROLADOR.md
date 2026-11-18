# CinemaUQ — conexión de vistas y controladores

Esta versión conecta el flujo de compra y las pantallas principales de cliente y administrador.

## Flujo de cliente

1. `Login.fxml` → `LoginViewController`: autentica con `AutenticacionService` y conserva la sesión en `Sesion`.
2. `Cartelera.fxml` → `CarteleraViewController`: consulta las películas del singleton `Cine`, muestra tarjetas y permite elegir una función.
3. `SeleccionAsientos.fxml` → `AsientosViewController`: genera botones de asientos, permite seleccionar asientos libres y añade las entradas al carrito.
4. `Confiteria.fxml` → `ConfiteriaViewController`: agrega combos, palomitas decoradas, gaseosas y chocolatinas al mismo carrito.
5. `Checkout.fxml` → `CheckoutViewController`: resume el carrito, permite pagar con tarjeta virtual (Proxy + Fachada) o redimir puntos, registra la compra y marca los asientos ocupados.
6. `PerfilCliente.fxml` → `PerfilClienteViewController`: muestra tarjeta, compras y puntos; permite solicitar cancelación/reembolso según la política de `Compra`.

`navegacion.FlujoCompra` mantiene la función, los asientos seleccionados y el carrito compartido durante el flujo.

## Flujo de administrador

- Usuario inicial de pruebas: **admin**
- Contraseña inicial: **admin123**
- `DashboardAdmin.fxml` muestra indicadores y gráficos con los datos en memoria.
- `GestionAdmin.fxml` permite registrar/eliminar películas, programar funciones en salas y cancelar funciones. Se crean tres salas de prueba (40 asientos cada una) al abrir Gestión si aún no existen.

## Ejecutar

Se requiere JDK 23 (el `pom.xml` configura source/target 23) y conexión a Maven Central la primera vez para descargar Maven/JavaFX.

Desde la carpeta `CinemaUQ`:

```bash
./mvnw clean javafx:run
```

En Windows:

```bat
mvnw.cmd clean javafx:run
```

## Alcance y verificaciones

- Se verificaron los 9 FXML como XML bien formado y la existencia de todos los controladores y métodos de evento declarados.
- Se compiló la lógica de negocio sin JavaFX y se ejecutó una prueba de humo de Composite, Decorator, Abstract Factory, Fachada y Proxy.
- No fue posible ejecutar una compilación completa ni abrir JavaFX en el entorno de revisión: no hay dependencias JavaFX instaladas y Maven Wrapper no pudo descargar Maven por falta de conexión a Maven Central.
- Las pestañas administrativas de salas/inventario/promociones/clientes/recargas/reportes que ya estaban marcadas como “Próximamente” siguen siendo marcadores visuales; la gestión operativa implementada aquí cubre películas, funciones, selección de asientos, compra, puntos y cancelaciones.
- Las solicitudes de recarga se validan y notifican en el perfil, pero no se persisten ni se aprueban desde administración porque el proyecto todavía no tiene un modelo/flujo de solicitudes de recarga.
- Los datos de `Cine`, usuarios, compras y salas viven en memoria; al cerrar la aplicación se reinician.
