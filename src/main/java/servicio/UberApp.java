package servicio;

import mapa.Mapa;

import java.util.ArrayList;

public class UberApp {
    private ArrayList<Chofer> choferes;
    private ArrayList<Usuario> usuarios;
    private Mapa mapa;

    public UberApp (Mapa mapa) {
        this.mapa = mapa;
        this.choferes = new ArrayList<>();
        this.usuarios = new ArrayList<>();
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

    public Usuario getUltimoUsuario() {
        return this.usuarios.get(this.usuarios.size() - 1);
    }

    public Chofer getUltimoChofer() {
        return this.choferes.get(this.choferes.size() - 1);
    }

    public void simular() {
        /*for(int i = 0; i < 5; i++) {
            generarChofer();
        }*/

        //generarUsuario();
        Usuario usuario = this.usuarios.get(0);
        Viaje viaje = usuario.pedirUber();
        viaje.cargarCaminoUsuario(mapa);
        viaje.cargarCaminoDestino(mapa);

        if(viaje != null) {
            Chofer chofer = viaje.getChofer();
            chofer.trabajar(viaje, this.mapa);
            viaje.setEstado(true);
        } else {
            System.out.println("el usuario " + usuario.getIdUsuario() + " no logro encontrar chofer.");
        }

    }


}
