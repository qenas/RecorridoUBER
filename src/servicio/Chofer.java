package servicio;

import grafos.contenedores.PilaSLinkedList;
import grafos.grafoDirigido.GrafoDirigido;
import mapa.Interseccion;
import mapa.Mapa;

import java.util.ArrayList;
import java.util.Random;

//2do parcial
//entra arbol AVL, rotaciones
//codigo: busquedas por profundidad y amplitud, codigo (grafos conexos)
//grafos, arboles
//KRUSKAL Y PRIM -> GRAFOS


public class Chofer {

    private int idChofer;
    private Interseccion posicion;
    private boolean estaOcupado;
    private double ETA;


    public Chofer(int idChofer, boolean estaOcupado) {
        this.idChofer = idChofer;
        this.estaOcupado = estaOcupado;
        this.ETA = -1;
    }



    public int getIdChofer() {
        return this.idChofer;
    }

    public double getETA() {
        return ETA;
    }

    public void setETA(double ETA) {
        this.ETA = ETA;
    }

    public void setEstaOcupado(boolean estaOcupado) {
        this.estaOcupado = estaOcupado;
    }

    public void setPosicion(Interseccion posicion) {
        this.posicion = posicion;
    }

    public boolean estaOcupado() {
        return this.estaOcupado;
    }

    public Interseccion getPosicion() {
        return this.posicion;
    }

    //pasarle como atribulo solo la lista de intersecciones del mapa
    public void cargarPosicion(Mapa mapa) {
        GrafoDirigido grafoPesos = mapa.getGrafoPesos();

        Random random = new Random();

        int u = random.nextInt(grafoPesos.getOrden() + 1);

        this.posicion = mapa.getInterseccion(u);
    }

    public void trabajar(Viaje viaje, Mapa mapa) {
        System.out.println("comienza recogida");
        comenzarRecogida(viaje, mapa);
        System.out.println("comienza viaje del usuario");
        comenzarViaje(viaje, mapa);
    }

    /* comenzarRecogida(viaje, mapa)
    * Parámetros: "viaje" de tipo Viaje, "mapa" de tipo Mapa.
    * A partir de "viaje" obtiene el camino más barato para ir a recoger al usuario y con "mapa" va actualizando su posición.
    * Lo que hace el metodo es que el chofer primero se mueva desde la posición en la que agarró el viaje
    * hasta la posición del usuario al que le aceptó el viaje.
    * */

    private void comenzarRecogida(Viaje viaje, Mapa mapa) {
        PilaSLinkedList caminoRecogida = viaje.getCaminoAlUsuario();
        while(!caminoRecogida.estaVacia()) {
            int interseccionID = (int) caminoRecogida.sacar();
            this.posicion = mapa.getInterseccion(interseccionID);


            System.out.println(this.posicion.toString());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(posicion.equals(viaje.getOrigen())) {
            System.out.println("El chofer llego para recoger al usuario " + viaje.getUsuario().getIdUsuario());
        }
    }

    /* comenzarViaje(viaje, mapa)
     * Parámetros: "viaje" de tipo Viaje, "mapa" de tipo Mapa.
     * A partir de "viaje" obtiene el camino más barato para ir al destino del usuario y con "mapa" va actualizando su posición.
     * Lo que hace el metodo es que el chofer primero se mueva desde la posición en la que agarró el viaje
     * hasta la posición del usuario al que le aceptó el viaje.
     * */

    private void comenzarViaje(Viaje viaje, Mapa mapa) {
        PilaSLinkedList caminoViaje = viaje.getCaminoAlDestino();
        while(!caminoViaje.estaVacia()) {
            int interseccionID = (int) caminoViaje.sacar();
            this.posicion = mapa.getInterseccion(interseccionID);


            System.out.println(this.posicion.toString());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(posicion.equals(viaje.getDestino())) {
            System.out.println("El chofer llevo al usuario " + viaje.getUsuario().getIdUsuario() + " a su destino");
        }
    }

}
