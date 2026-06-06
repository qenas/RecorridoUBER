package mapa;

import java.util.ArrayList;

public class Segmento {
    private String inicio;
    private String fin;
    private ArrayList<String> nodos;

    public Segmento(ArrayList<String> nodos) {
        this.inicio = nodos.get(0);
        this.fin = nodos.get(nodos.size()-1);
        this.nodos = nodos;
    }

    public void mostrarNodos() {
        for(int i = 0; i < this.nodos.size(); i++) {
            System.out.println(nodos.get(i));
        }
    }

    public String getInicio() {
        return inicio;
    }

    public String getFin() {
        return fin;
    }
}
