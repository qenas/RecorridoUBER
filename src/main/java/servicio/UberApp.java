package servicio;

import interfaz.MapaView;
import mapa.Mapa;

import java.util.ArrayList;

public class UberApp {
    private ArrayList<Chofer> choferes;
    private ArrayList<Usuario> usuarios;
    private Mapa mapa;
    private ArrayList<Viaje> viajes;
    //---------------------------------------------
    private MapaView vistaMapa;
    //---------------------------------------------
    public UberApp (Mapa mapa) {
        this.mapa = mapa;
        this.choferes = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.viajes = new ArrayList<>();
        //-------------------------------------------
        this.vistaMapa = new MapaView(mapa);
        //-------------------------------------------
    }
    //---------------------------------------------
    public MapaView getVistaMapa(){ return this.vistaMapa; }
    //---------------------------------------------
    public Mapa getMapa() {
        return this.mapa;
    }

    public void generarUsuario() {
        Usuario nuevoUsuario = new Usuario(this.usuarios.size());
        nuevoUsuario.cargarPosiciones(this.mapa);
        this.usuarios.add(nuevoUsuario);
        //----------------------------------------
        vistaMapa.agrgarUsuario(nuevoUsuario);
        //----------------------------------------
    }

    public ArrayList<Chofer> getChoferesDisponibles() {
        ArrayList<Chofer> aux = new ArrayList<>();
        for(Chofer chofer : this.choferes) {
            if(!chofer.estaOcupado()) {
                aux.add(chofer);
            }
        }

        return aux;
    }

    public void generarChofer() {
        Chofer nuevoChofer = new Chofer(this.choferes.size());
        nuevoChofer.cargarPosicion(this.mapa);
        this.choferes.add(nuevoChofer);
        //----------------------------------------
        vistaMapa.agregarCoche(nuevoChofer);
        //----------------------------------------
    }


    public void addNuevoViaje(Viaje viaje) {
        this.viajes.add(viaje);
    }

    public Viaje getUltimoViaje() {
        return this.viajes.get(this.viajes.size() - 1);
    }

    public Usuario getUltimoUsuario() {
        return this.usuarios.get(this.usuarios.size() - 1);
    }

    public Chofer getUltimoChofer() {
        return this.choferes.get(this.choferes.size() - 1);
    }



}
