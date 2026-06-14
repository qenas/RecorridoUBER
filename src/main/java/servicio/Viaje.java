package servicio;

import grafos.contenedores.PilaSLinkedList;
import grafos.grafoDirigido.GrafoDirigido;
import mapa.Interseccion;
import mapa.Mapa;

public class Viaje {
    private Usuario usuario;
    private Chofer chofer;
    private Interseccion origen, destino;
    private boolean finalizado;
    private PilaSLinkedList caminoAlUsuario, caminoAlDestino;


    public Viaje(Usuario usuario, Chofer chofer) {
        this.usuario = usuario;
        this.chofer = chofer;
        this.caminoAlDestino = this.caminoAlUsuario = new PilaSLinkedList();
    }

    public Viaje(Usuario usuario, Chofer chofer, Interseccion origen, Interseccion destino) {
        this(usuario, chofer);
        this.origen = origen;
        this.destino = destino;
        this.finalizado = false;
    }

    public void setChofer(Chofer chofer) {
        this.chofer = chofer;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public Chofer getChofer() {
        return this.chofer;
    }

    public PilaSLinkedList getCaminoAlUsuario() {
        return caminoAlUsuario;
    }

    public PilaSLinkedList getCaminoAlDestino() {
        return caminoAlDestino;
    }

    public Interseccion getOrigen() {
        return this.origen;
    }

    public Interseccion getDestino() {
        return this.destino;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void cargarCaminoUsuario(Mapa mapa) {
        /* Primero se carga el camino desde la posición del usuario a su destino.
        * Después se obtiene el grafo del mapa y se realiza Dijkstra desde la posición del chofer
        * al resto de nodos. Una vez realizado el algoritmo, se carga el camino óptimo desde la
        * posición del chofer hasta la posición del usuario.
        * */

        GrafoDirigido grafoMapa = mapa.getGrafoPesos();
        grafoMapa.realizarDijkstra(this.chofer.getPosicion().getID());

        this.caminoAlUsuario = grafoMapa.retornaCaminoDijkstra(this.chofer.getIdChofer(), this.usuario.getOrigen().getID());
    }

    public void cargarCaminoDestino(Mapa mapa) {
        GrafoDirigido grafoMapa = mapa.getGrafoPesos();

        /* Como el mapa ya hizo el el alg. de Dijkstra desde la posición del usuario cuando
        * este pidió el uber, entonces no hace falta volver a realizarlo y se aprovecha que desde
        * esa posición ya se conocen los caminos a el resto de nodos del mapa y se cargan en el
        * camino al destino.
        * */

        this.caminoAlDestino = grafoMapa.retornaCaminoDijkstra(this.origen.getID(), this.destino.getID());
    }



}
