package navegacion;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Navegador {

    private final Stage stage;

    public Navegador(Stage stage) {
        this.stage = stage;
    }

    public void navegar(String archivoFXML) {

        try {
            java.net.URL recurso = getClass().getResource("/View/" + archivoFXML);
            if (recurso == null) {
                throw new IOException("No existe el recurso /View/" + archivoFXML
                        + ". Verifica que esté dentro de src/main/resources/View.");
            }
            FXMLLoader loader = new FXMLLoader(recurso);
            Parent root = loader.load();

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(
                    "No se pudo cargar la pantalla: " + archivoFXML,
                    e
            );
        }
    }
}
