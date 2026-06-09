package servicio;

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



    public Chofer(int idChofer, boolean estaOcupado) {
        this.idChofer = idChofer;
        this.estaOcupado = estaOcupado;
    }

    public void setEstaOcupado(boolean estaOcupado) {
        this.estaOcupado = estaOcupado;
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

}
