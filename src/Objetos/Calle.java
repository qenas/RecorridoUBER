package Objetos;

import java.util.ArrayList;

public class Calle {
    private int id;
    private String nombre, tipo;
    private ArrayList<String> nodos;

    public Calle(int id, String nombre, String tipo, ArrayList<String> nodos) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.nodos = nodos;
    }

    public int getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public ArrayList<String> getNodos() {
        return nodos;
    }

    public void setNodos(ArrayList<String> nodos) {
        this.nodos = nodos;
    }

    public void agregarNodos(ArrayList<String> nodos) {
        for(int i = 0; i < nodos.size(); i++) {
            String nodo = nodos.get(i);
            if(!this.nodos.contains(nodo)) {
                this.nodos.add(nodo);
            }
        }
    }

    public void mostrarNodos() {
        for(int i = 0; i < this.nodos.size(); i++) {
            System.out.println(this.nodos.get(i));
        }
    }

    public boolean tieneNodosRepetidos() {
        ArrayList<String> visitados = new ArrayList<>();
        for(int i = 0; i < this.nodos.size(); i++) {
            if(visitados.contains(this.nodos.get(i))) {
                return true;
            }
            visitados.add(this.nodos.get(i));
        }

        return false;

    }

}
