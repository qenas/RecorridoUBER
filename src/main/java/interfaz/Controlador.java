package interfaz;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import servicio.UberApp;

public class Controlador {

    private UberApp uberApp;

    public void setUberApp(UberApp uberApp) {
        this.uberApp = uberApp;
    }


    @FXML
    private Button btnGenUsuario;

    @FXML
    private Button btnGenChofer;

    @FXML
    private Button btnSimular;

    @FXML
    private void generarUsuario() {
        uberApp.generarUsuario();
        System.out.println("usuario generado " + uberApp.getUltimoUsuario().getIdUsuario());
    }

    @FXML
    private void generarChofer() {
        uberApp.generarChofer();
        System.out.println("chofer generado " + uberApp.getUltimoChofer().getIdChofer());
    }


    @FXML
    private void simularViaje() {
        uberApp.simular();
    }



}
