package interfaz;

import gestion.JSONLector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mapa.Mapa;
import org.json.JSONObject;
import servicio.UberApp;



public class Interfaz extends Application {

    @Override
    public void start(Stage stage) throws Exception {


        // 1) Leer y crear un nuevo objeto Mapa el cual contiene todas las calles e intersecciones de la ciudad, en este caso, Salta.
        JSONObject salta = JSONLector.getJSONObject("src/main/java/archivos/CentroyMacroSALTA.geojson");
        Mapa mapa = new Mapa(salta);
        UberApp uberApp = new UberApp(mapa);



        // 2) Crea un loader para cargar el archivo .FXML para crear la interfaz. El controlador es el encargado de los botones y eventos
        // de esta interfaz.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/Interfaz.fxml"));

        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/Vista.fxml"));
        Parent root = loader.load();
        Controlador controlador = loader.getController();
        controlador.setUberApp(uberApp);





        Scene scene = new Scene(root, 870, 600);

        stage.setTitle("TPI - AyED 2026 - Uber");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
