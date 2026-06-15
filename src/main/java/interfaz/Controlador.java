package interfaz;

import gestion.ResultadoSimulacion;
import grafos.contenedores.PilaSLinkedList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import mapa.Interseccion;
import mapa.Mapa;
import servicio.*;

public class Controlador {

    private ObservableList<ResultadoSimulacion> listaResultados = FXCollections.observableArrayList();
    private UberApp uberApp;

    public void setUberApp(UberApp uberApp) {
        this.uberApp = uberApp;
        //-------------------------------------------------------
        mapContainer.getChildren().add(uberApp.getVistaMapa());
        //-------------------------------------------------------
    }
    //---------------------------------------------
    @FXML
    private Pane mapContainer;
    //---------------------------------------------
    @FXML
    private Button btnGenUsuario;
    @FXML
    private Button btnGenChofer;
    @FXML
    private Button btnSimular;
    @FXML
    private Label lblEventos;
    @FXML
    private TextArea txtEventos;

    private void log(String mensaje) { //metodo para ir actualizando la lista de eventos
        Platform.runLater(() -> {
            txtEventos.appendText(mensaje + "\n");
        });
    }

    @FXML
    private void generarUsuario() {
        uberApp.generarUsuario();
        System.out.println("usuario generado " + uberApp.getUltimoUsuario().getIdUsuario());
        log("Usuario " + uberApp.getUltimoUsuario().getIdUsuario() + " fue generado con exito.");

    }

    @FXML
    private void generarChofer() {
        uberApp.generarChofer();
        System.out.println("chofer generado " + uberApp.getUltimoChofer().getIdChofer());
        log("Chofer " + uberApp.getUltimoChofer().getIdChofer() + " fue generado con exito.");

    }


    @FXML
    private void simularViaje() {
        //uberApp.simular();
        System.out.println("Simular viaje");
        Usuario usuario = uberApp.getUltimoUsuario();
        Viaje viaje = usuario.pedirUber(uberApp.getChoferesDisponibles(), uberApp.getMapa());
        log("El usuario " + usuario.getIdUsuario() + " pidio un uber en " + usuario.getOrigen().getDescripcion() + ".");
        if(viaje != null) {
            uberApp.addNuevoViaje(viaje);
            cargarListaResultados(usuario, viaje);
            Chofer chofer = viaje.getChofer();
            viaje.cargarCaminoDestino(uberApp.getMapa());
            viaje.cargarCaminoUsuario(uberApp.getMapa());

            log("El chofer " + chofer.getIdChofer() + " acepto el viaje del usuario " + usuario.getIdUsuario() + ".");

            new Thread(() -> {
                comenzarRecogida(viaje, uberApp.getMapa(), chofer);
                comenzarViaje(viaje, uberApp.getMapa(), chofer);
                //-----------------------------------------------------------
                uberApp.getVistaMapa().getCocheView(chofer).actualizar();
            }).start();


        } else {
            log("Ninguna unidad acepto el viaje del usuario " + usuario.getIdUsuario() + ".");
        }
        //actualizarChoferes();
    }

    /* comenzarRecogida(viaje, mapa, chofer)
     * Parámetros: "viaje" de tipo Viaje, "mapa" de tipo Mapa y "chofer" de tipo Chofer.
     * A partir de "viaje" obtiene el camino más barato para ir a recoger al usuario y con "mapa" va actualizando su posición.
     * Lo que hace el metodo es que el chofer primero se mueva desde la posición en la que agarró el viaje
     * hasta la posición del usuario al que le aceptó el viaje.
     * */

    private void comenzarRecogida(Viaje viaje, Mapa mapa, Chofer chofer) {
        PilaSLinkedList caminoRecogida = viaje.getCaminoAlUsuario();
        log("El chofer " + chofer.getIdChofer() + " comenzo el recorrido para recoger al usuario " + viaje.getUsuario().getIdUsuario());
        System.out.println("comenzar recogida");

        while(!caminoRecogida.estaVacia()) {
            int interseccionID = (int) caminoRecogida.sacar();
            Interseccion nuevaPosicion = mapa.getInterseccion(interseccionID);
            chofer.mover(nuevaPosicion);
            System.out.println(nuevaPosicion.toString());

            //implementacion de mover el objeto en la interfaz
            uberApp.getVistaMapa().getCocheView(chofer).actualizar();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(chofer.getPosicion().equals(viaje.getOrigen())) {
            log("El chofer " + chofer.getIdChofer() + " ha llegado a recoger al usuario " + viaje.getUsuario().getIdUsuario() + ".");
        } else {
            System.out.println("no llego");
        }


    }

    /* comenzarViaje(viaje, mapa, chofer)
     * Parámetros: "viaje" de tipo Viaje, "mapa" de tipo Mapa y "chofer" de tipo Chofer
     * A partir de "viaje" obtiene el camino más barato para ir al destino del usuario y con "mapa" va actualizando su posición.
     * El metodo va actualizando la posición del chofer, es decir, mueve al chofer y al usuario hasta el destino de este último.
     * */

    private void comenzarViaje(Viaje viaje, Mapa mapa, Chofer chofer) {
        PilaSLinkedList caminoViaje = viaje.getCaminoAlDestino();
        log("El chofer " + chofer.getIdChofer() + " comenzo el recorrido para llevar al usuario " + viaje.getUsuario().getIdUsuario() + " a su destino " + viaje.getDestino().getDescripcion());

        //implementacion de mover el objeto en la interfaz
        uberApp.getVistaMapa().desaparecer(viaje.getUsuario());

        while(!caminoViaje.estaVacia()) {
            int interseccionID = (int) caminoViaje.sacar();
            Interseccion nuevaPosicion = mapa.getInterseccion(interseccionID);
            chofer.mover(nuevaPosicion);
            System.out.println(nuevaPosicion.toString());

            //implementacion de mover el objeto en la interfaz
            uberApp.getVistaMapa().getCocheView(chofer).actualizar();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(chofer.getPosicion().equals(viaje.getDestino())) {
            log("El chofer " + chofer.getIdChofer() + " llevo a su destino " + viaje.getDestino().getDescripcion() + " al usuario " + viaje.getUsuario().getIdUsuario() + ".");
            chofer.setEstaOcupado(false);
            viaje.setFinalizado(true);
        } else {
            System.out.println("no llego");
        }

    }


    @FXML private TableView<ResultadoSimulacion> tablaResultados;
    @FXML private TableColumn<ResultadoSimulacion, Integer> colOrden;
    @FXML private TableColumn<ResultadoSimulacion, String> colId;
    @FXML private TableColumn<ResultadoSimulacion, String> colUbicacion;
    @FXML private TableColumn<ResultadoSimulacion, String> coleta;
    @FXML private TableColumn<ResultadoSimulacion, String> colAcepto;


    //Metodo para setear los datos en el TableList de la interfaz
    @FXML
    public void initialize() {
        // Vincular columnas de la tabla
        colOrden.setCellValueFactory(new PropertyValueFactory<>("orden"));
        colId.setCellValueFactory(new PropertyValueFactory<>("idChofer"));
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        coleta.setCellValueFactory(new PropertyValueFactory<>("eta"));
        colAcepto.setCellValueFactory(new PropertyValueFactory<>("aceptoViaje"));

        tablaResultados.setItems(listaResultados);

    }

    private void cargarListaResultados(Usuario usuario, Viaje viaje) {
        ColaChoferes colaChoferesUsuario = usuario.getColaChoferes();

        int contadorOrden = 1;          // Nuestro índice autoincrementado
        boolean viajeTomado = false;

        while (!colaChoferesUsuario.estaVacia() && !viajeTomado) {   // Mientras la cola no este vacia
            Chofer conductorActual = (Chofer) colaChoferesUsuario.sacar();   // saco un conductor de la colaPrioridad
            String respuesta;                               // respuesta : es una cadena

            viajeTomado = conductorActual.decidirAceptarViaje();

            if (viajeTomado) {    // .decidirAceptarViaje() me devuelve un booleano
                respuesta = "SI (Aceptado)";   //respuesta : SI
                conductorActual.setEstaOcupado(true);  // Traspasamos el chofer a la lista de Ocupado
                viaje.setChofer(conductorActual);
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

        while(!colaChoferesUsuario.estaVacia()) { // para el resto que no llego a evaluarse
            Chofer conductorActual = (Chofer) colaChoferesUsuario.sacar();   // saco un conductor de la colaPrioridad
            // respuesta : es una cadena

            String idChofer = "Chofer " + conductorActual.getIdChofer();
            String etaMinuto = conductorActual.getETA() + " minutos";
            listaResultados.add(new ResultadoSimulacion(
                    contadorOrden,
                    idChofer,
                    conductorActual.getPosicion().getDescripcion(),
                    etaMinuto,
                    "No llego a evaluarse."
            ));

            contadorOrden++; // Incrementa para el siguiente conductor de la cola
        }

        tablaResultados.setItems(listaResultados);

    }







}
