package interfaz;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import servicio.Chofer;
import gestion.ResultadoSimulacion;
import servicio.UberApp;
import servicio.Usuario;

import java.util.ArrayList;

public class ControladorViaje {
    @FXML
    private Button btnSimular;
    @FXML private TableView<ResultadoSimulacion> tablaResultados;
    @FXML private TableColumn<ResultadoSimulacion, Integer> colOrden;
    @FXML private TableColumn<ResultadoSimulacion, String> colId;
    @FXML private TableColumn<ResultadoSimulacion, String> colUbicacion;
    @FXML private TableColumn<ResultadoSimulacion, String> colETA;
    @FXML private TableColumn<ResultadoSimulacion, String> colAcepto;

    private ObservableList<ResultadoSimulacion> listaResultados = FXCollections.observableArrayList();

    private UberApp sistemaUber;
    private Usuario usuarioActual;
    private ArrayList<Chofer> choferesDisponible = new ArrayList<>(10);
    private ArrayList<Usuario> usuariosDisponible = new ArrayList<>(10);

    public void setSistemaUber(UberApp uberApp) {
        this.sistemaUber = uberApp;
    }

    @FXML
    public void initialize() {

        colOrden.setCellValueFactory(new PropertyValueFactory<>("orden"));
        colId.setCellValueFactory(new PropertyValueFactory<>("idChofer"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colETA.setCellValueFactory(new PropertyValueFactory<>("ETA"));
        colAcepto.setCellValueFactory(new PropertyValueFactory<>("aceptoViaje"));


        tablaResultados.setItems(listaResultados);

        // Metodo que prepara todos los datos para poder funcionar
        prepararDatosSimulacion();
    }


    private void prepararDatosSimulacion() {
        //sistemaUber = new UberApp(Mapa);  //Intanciamos al objeto sistemaUber
        usuarioActual = new Usuario(3);

        //Agregar un usuario en la lista de usuario en la clase UberApp
        usuariosDisponible.add(usuarioActual);

        // Agregamos algunos conductores de prueba con diferentes distancias
        for (int i=1; i<15; i++){
            choferesDisponible.add(new Chofer(i));
        }

    }

    @FXML
    void handleSimularViaje() {
        // Limpiamos la tabla antes de una nueva simulación
        listaResultados.clear();

        //Creamos la cola de Prioridad
        usuarioActual.actualizarColaChoferes(choferesDisponible, sistemaUber.getMapa());

        int contadorOrden = 1;          // Nuestro índice autoincrementado
        boolean viajeTomado = false;    // viajeTomado : comienza falso

        while (!usuarioActual.getColaChoferes().estaVacia()) {   // Mientras la cola no este vacia
            Chofer conductorActual = (Chofer) usuarioActual.getColaChoferes().sacar();   // saco un conductor de la colaPrioridad
            String respuesta;                               // respuesta : es una cadena

            if (!viajeTomado && conductorActual.decidirAceptarViaje()) {    // .decidirAceptarViaje() me devuelve un booleano
                respuesta = "SÍ (Aceptado)";    //respuesta : SI
                viajeTomado = true;

                // Traspasamos el chofer a la lista de Ocupado

            } else if (viajeTomado) {
                respuesta = "No evaluado (Ya se asignó)";
            } else {
                respuesta = "NO (Rechazado)";
            }

            // Agregamos la fila a la tabla con el orden autoincrementado
            String idChofer = "Chofer " + conductorActual.getIdChofer();
            String etaMinuto = conductorActual.getETA() + " minutos";
            listaResultados.add(new ResultadoSimulacion(
                    contadorOrden,
                    idChofer,
                    conductorActual.getPosicion().getDescripcion(),
                    etaMinuto,
                    respuesta
            ));

            contadorOrden++; // Incrementa para el siguiente conductor de la cola
        }
    }
}
