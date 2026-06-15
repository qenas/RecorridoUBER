package interfaz;

import grafos.contenedores.PilaSLinkedList;
import javafx.application.Platform;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import mapa.*;
import servicio.Chofer;
import servicio.Usuario;
import servicio.Viaje;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class MapaView extends Pane {
    private final Mapa mapa;
    private final ConversorCoordenadas sistemaCoord;
    public static final double WIDTH_MAP = 450;
    public static final double HEIGHT_MAP = 383;
    private ArrayList<CocheView> coches;
    private ArrayList<UsuarioView> usuarios;

    public MapaView(Mapa mapa){
        this.mapa = mapa;
        this.sistemaCoord = new ConversorCoordenadas(mapa.getIntersecciones());
        this.coches = new ArrayList<CocheView>();
        this.usuarios = new ArrayList<UsuarioView>();

        setPrefSize(WIDTH_MAP, HEIGHT_MAP);
        setBorder(
                new Border(
                        new BorderStroke(
                                Color.rgb(73, 152, 204),
                                BorderStrokeStyle.SOLID,
                                CornerRadii.EMPTY,
                                new BorderWidths(1)
                        )
                )
        );
        cargarVistaMapa();
        setClip(new Rectangle(WIDTH_MAP, HEIGHT_MAP));
    }

    private void cargarVistaMapa(){
        cargarNodosMapa(mapa.getIntersecciones());
        cargarCallesMapa(mapa.getCalles().values());
    }

    private void cargarNodosMapa(Map<Coordenada, Interseccion> datos){
        //Colocacion de intersecciones
        for(Coordenada coord: datos.keySet()){
            double posicionX = sistemaCoord.toX(coord.getLongitud());
            double posicionY = sistemaCoord.toY(coord.getLatitud());
            Circle c =new Circle(posicionX, posicionY, 2);
            getChildren().add(c);
        }
    }

    public void agrgarUsuario(Usuario usuario){
        UsuarioView u = new UsuarioView(usuario, sistemaCoord);
        usuarios.add(u);
        getChildren().add(u);
    }

    public void agregarCoche(Chofer chofer){
        CocheView c = new CocheView(chofer, sistemaCoord);
        coches.add(c);
        getChildren().add(c);
    }

    public void actualizarVista(){
        for(CocheView coche: coches){
            coche.actualizar();
        }
    }

    public UsuarioView getUsuarioView(Usuario usuario){
        int index = usuarios.indexOf(new UsuarioView(usuario));
        if(index!=-1){
            return usuarios.get(index);
        } else return null;
    }
    public CocheView getCocheView(Chofer chofer){
        int index = coches.indexOf(new CocheView(chofer));
        if(index!=-1){
            return coches.get(index);
        } else return null;
    }

    private void cargarCallesMapa(Collection<Calle> datos){
        for(Calle calle: datos){
            ArrayList<ArrayList<Coordenada>> segmentos = calle.getCoordenadas();
            for(ArrayList<Coordenada> segmento: segmentos) {
                for(int i=0; i<segmento.size()-1; i++){
                    Line ruta = new Line(
                            sistemaCoord.toX(segmento.get(i).getLongitud()),
                            sistemaCoord.toY(segmento.get(i).getLatitud()),
                            sistemaCoord.toX(segmento.get(i+1).getLongitud()),
                            sistemaCoord.toY(segmento.get(i+1).getLatitud())
                    );
                    getChildren().add(ruta);
                }
            }
        }
    }

    public void desaparecer(Usuario usuario) {

        UsuarioView view = null;

        for (UsuarioView u : usuarios) {
            if (u.getUsuario().equals(usuario)) {
                view = u;
                break;
            }
        }

        if (view != null) {
            usuarios.remove(view);
            final UsuarioView viewFinal = view;
            Platform.runLater(() -> {
                getChildren().remove(viewFinal);
            });
        }
    }

    public void animarViaje(CocheView coche, UsuarioView usuario, Viaje viaje){
        PilaSLinkedList aux = viaje.getCaminoAlUsuario();
        ArrayList<Integer> camino = new ArrayList<Integer>();
        ArrayList<Coordenada> caminoCompletoIda; ArrayList<Coordenada> caminoCompletoVuelta;
        Polyline ruta;
        while (!aux.estaVacia()){
            camino.add((Integer)aux.sacar());
        }
        for (int i=camino.size()-1; i>=0; i--){
            aux.meter(camino.get(i));
        }
        caminoCompletoIda = mapa.recorridoNodoANodoB(camino);
        ruta = crearCamino(caminoCompletoIda);
        getChildren().add(ruta);
        //coche.mover(caminoCompletoIda);
        coche.setFill(Color.RED);

        getChildren().remove(usuario);
        camino.clear();
        getChildren().remove(ruta);

        aux = viaje.getCaminoAlDestino();
        while (!aux.estaVacia()){
            camino.add((Integer)aux.sacar());
        }
        for (int i=camino.size()-1; i>=0; i--){
            aux.meter(camino.get(i));
        }
        caminoCompletoVuelta = mapa.recorridoNodoANodoB(camino);
        ruta = crearCamino(caminoCompletoVuelta);
        getChildren().add(ruta);
        coche.mover(caminoCompletoIda, () -> coche.mover(caminoCompletoVuelta, null));
        coche.setFill(Color.GREEN);
        getChildren().remove(ruta);
    }

    public Polyline crearCamino(ArrayList<Coordenada> camino){
        Polyline ruta = new Polyline();
        for(int i=0; i< camino.size(); i++){
            ruta.getPoints().add(sistemaCoord.toX(camino.get(i).getLongitud()));
            ruta.getPoints().add(sistemaCoord.toY(camino.get(i).getLatitud()));
        }
        ruta.setStroke(Color.BLUE);
        ruta.setStrokeWidth(2);
        return  ruta;
    }

}
