import mapa.Interseccion;
import mapa.Mapa;
import servicio.Chofer;
import servicio.ColaChoferes;
import servicio.Usuario;

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
        nuevoUsuario.actualizarColaChoferes(this.choferes, this.mapa);
        this.usuarios.add(nuevoUsuario);
    }

    public void generarChofer() {
        Chofer nuevoChofer = new Chofer(this.choferes.size(), false);
        nuevoChofer.cargarPosicion(this.mapa);
        this.choferes.add(nuevoChofer);
    }


}
