package interfaz;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import mapa.ConversorCoordenadas;
import servicio.Usuario;

public class UsuarioView extends Circle {
    Usuario usuario;
    ConversorCoordenadas conversor;

    public UsuarioView(Usuario usuario, ConversorCoordenadas conversor){
        super(conversor.toX(usuario.getOrigen().getCoordenada().getLongitud()),
                conversor.toY(usuario.getOrigen().getCoordenada().getLatitud()), 5);
        this.conversor = conversor;
        inicializar(usuario);
    }
    public UsuarioView(Usuario usuario){
        this.conversor = null;
        inicializar(usuario);
    }

    private void inicializar(Usuario usuario){
        this.usuario = usuario;
        //this.id = new Text(getCenterX(), getCenterY(), String.valueOf(usuario.getIdUsuario()));
        setFill(Color.YELLOW);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UsuarioView){
            return this.usuario.equals(((UsuarioView)obj).getUsuario());
        } else return false;
    }

    @Override
    public int hashCode() {
        return this.usuario.hashCode();
    }

}
