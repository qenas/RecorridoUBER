package servicio;

import grafos.grafoDirigido.GrafoDirigido;
import mapa.Interseccion;
import mapa.Mapa;

import java.util.ArrayList;
import java.util.Random;

public class Usuario {
    private int idUsuario;
    private ColaChoferes colaChoferes;
    private Interseccion origen;// destino
    private boolean abordado;
    private ArrayList<Double> listaETA;


    public Usuario(int idUsuario) {
        this.idUsuario = idUsuario;
        this.colaChoferes = new ColaChoferes();
        this.listaETA = new ArrayList<>();
    }

    public void setAbordado(boolean abordado) {
        this.abordado = abordado;
    }

    public Interseccion getOrigen() {
        return origen;
    }

    public void setOrigen(Interseccion origen) {
        this.origen = origen;
    }


    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void actualizarColaChoferes(ArrayList<Chofer> choferes, Mapa mapa) {
        GrafoDirigido grafoMapa = mapa.getGrafoPesos();

        grafoMapa.realizarDijkstra(this.origen.getID());

        for(Chofer chofer : choferes) {
            Interseccion posicionChofer = chofer.getPosicion();
            double costo = grafoMapa.getDistanciaDijkstra(posicionChofer.getID()); //para cada chofer, le calcula el ETA

            this.listaETA.add(costo);

            this.colaChoferes.meter(chofer);
        }
    }

    public void cargarPosicion(Mapa mapa) {
        GrafoDirigido grafoPesos = mapa.getGrafoPesos();

        Random random = new Random();

        int u = random.nextInt(grafoPesos.getOrden() + 1);

        this.origen = mapa.getInterseccion(u);
    }

    public void pedirUber() {
        int indiceLista = 0;

        Random random = new Random();

        boolean band

        while(!this.colaChoferes.estaVacia()) {
            Chofer chofer = (Chofer) this.colaChoferes.sacar();


            // true: acepta / false: no acepto
            boolean acepta = random.nextBoolean();

            if(acepta && !chofer.estaOcupado()) {
                chofer.setEstaOcupado(acepta);

            }

        }


    }




}
