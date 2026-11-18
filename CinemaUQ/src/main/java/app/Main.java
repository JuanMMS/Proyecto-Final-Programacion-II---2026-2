package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    /**
     * Funcion para poder cargar lo necesario para correr javaFX
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
            DatosIniciales.cargar();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/uniquindio/cinemauq/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("CinemaUQ");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Funcion que ejecuta la aplicacion
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
