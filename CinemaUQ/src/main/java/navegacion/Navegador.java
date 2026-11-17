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
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/uniquindio/cinemauq/" + archivoFXML
                    )
            );

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
