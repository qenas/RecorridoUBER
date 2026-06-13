package servicio;

import mapa.Mapa;

import java.util.ArrayList;

public class UberApp {
    private ArrayList<Chofer> choferes;
    private ArrayList<Usuario> usuarios;
    private Mapa mapa;
    private ArrayList<Viaje> viajes;

    public UberApp (Mapa mapa) {
        this.mapa = mapa;
        this.choferes = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.viajes = new ArrayList<>();
    }

    public Mapa getMapa() {
        return this.mapa;
    }

    public void generarUsuario() {
        Usuario nuevoUsuario = new Usuario(this.usuarios.size());
        nuevoUsuario.cargarPosiciones(this.mapa);
        nuevoUsuario.actualizarColaChoferes(this.choferes, this.mapa);
        this.usuarios.add(nuevoUsuario);
    }

    public void generarChofer() {
        Chofer nuevoChofer = new Chofer(this.choferes.size(), false);
        nuevoChofer.cargarPosicion(this.mapa);
        this.choferes.add(nuevoChofer);
        actualizarUsuarios();
    }

    private void actualizarUsuarios() {
        for(Usuario usuario : this.usuarios) {
            usuario.actualizarColaChoferes(this.choferes, this.mapa);
        }
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
