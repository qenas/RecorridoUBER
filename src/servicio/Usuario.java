package servicio;

import grafos.contenedores.PilaSLinkedList;
import grafos.grafoDirigido.GrafoDirigido;
import mapa.Interseccion;
import mapa.Mapa;

import java.util.ArrayList;
import java.util.Random;

public class Usuario {
    private int idUsuario;
    private ColaChoferes colaChoferes;
    private Interseccion origen, destino;

    private ArrayList<Double> listaETA;


    public Usuario(int idUsuario) {
        this.idUsuario = idUsuario;
        this.colaChoferes = new ColaChoferes();
        this.listaETA = new ArrayList<>();

    }

    /*public void setAtendido(boolean atendido) {
        this.atendido = atendido;
    }*/

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

            chofer.setETA(costo);

            this.colaChoferes.meter(chofer);
        }
    }

    public void cargarPosiciones(Mapa mapa) {
        GrafoDirigido grafoPesos = mapa.getGrafoPesos();

        Random random = new Random();

        int origen = random.nextInt(grafoPesos.getOrden() + 1);
        int destino = random.nextInt(grafoPesos.getOrden() + 1);
        this.origen = mapa.getInterseccion(origen);

        if(origen != destino) {
            this.destino = mapa.getInterseccion(destino);
        } else {
            while(origen == destino) {
                destino = random.nextInt(grafoPesos.getOrden() + 1);
            }
            this.destino = mapa.getInterseccion(destino);
        }
    }

    public Viaje pedirUber() {
        Viaje viaje = null;
        Random random = new Random();
        boolean atendido = false;

        Chofer chofer = null;

        if(this.colaChoferes.estaVacia()) {
            System.out.println("Error: cola de choferes vacia");
        } else {
            while(!this.colaChoferes.estaVacia() && !atendido) {
                chofer = (Chofer) this.colaChoferes.sacar();

                // true: acepta / false: no acepto
                boolean acepta = random.nextBoolean();
                if(acepta && !chofer.estaOcupado()) {
                    chofer.setEstaOcupado(acepta);
                    atendido = true;
                }
            }
            if(!atendido && this.colaChoferes.estaVacia()) { // se recorrio la cola de choferes y no acepto ninguno
                System.out.println("no fuiste atendido");
            } else if (atendido && !this.colaChoferes.estaVacia()) { // fue atendido por un chofer y quedaron choferes en la cola
                System.out.println("el chofer " + chofer.getIdChofer() + " ha aceptado tu viaje, esta en camino a recogerte.");

                /* Cómo funciona el sistema luego de que un chofer acepte el viaje del usuario?
                * -El chofer que aceptó queda guardado en la variable "chofer".
                *
                * -Se crea un objeto de tipo Viaje el cual almacena: el usuario que pidió el uber,
                * el chofer que le aceptó el viaje y las posiciones de origen y destino de su viaje.
                *
                * */


                viaje = new Viaje(this, chofer, origen, destino); // el destino podria ser generado aleatoriamente desde aca

            }
        }

        borrarColaChoferes();

        return  viaje;
    }

    private void borrarColaChoferes() {
        while(!this.colaChoferes.estaVacia()) {
            Chofer chofer = (Chofer) this.colaChoferes.sacar();
            chofer.setETA(-1);
        }
        this.colaChoferes.limpiar();
    }







}
