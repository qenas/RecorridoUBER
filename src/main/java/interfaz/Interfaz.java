package interfaz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Interfaz extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        System.out.println(getClass().getResource("/interfaz/Interfaz.fxml"));

        Parent root = FXMLLoader.load(getClass().getResource("/interfaz/Interfaz.fxml"));

        System.out.println(getClass().getResource("/interfaz/Interfaz.fxml"));

        Scene scene = new Scene(root, 400, 200);

        stage.setTitle("Prueba");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
