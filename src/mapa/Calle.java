package mapa;

import java.util.ArrayList;
import java.util.Map;

public class Calle {
    private int id;
    private String nombre, tipo;

    //parche
    private ArrayList<Segmento> segmentos;
    private ArrayList<String> nodos;
    private ArrayList<Interseccion> intersecciones;
    private boolean manoUnica;

    public Calle(int id, String nombre, String tipo, ArrayList<String> nodos, boolean manoUnica) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.nodos = nodos;
        this.manoUnica = manoUnica;
        this.intersecciones = new ArrayList<>();
        this.segmentos = new ArrayList<>();
    }

    public void addSegmento(ArrayList<String> segmento) {
        Segmento nuevoSegmento = new Segmento(segmento);
        this.segmentos.add(nuevoSegmento);
    }

    public ArrayList<Interseccion> getIntersecciones() {
        return this.intersecciones;
    }


    public String getTipo() {
        return this.tipo;
    }

    public boolean isManoUnica() {
        return this.manoUnica;
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

    public double getVelocidad() {
        double velocidad = 0;
        switch (this.tipo) {
            case "primary":
                velocidad = 45.0 / 3.6;   // 12.5 m/s
                break;
            case "secondary":
                velocidad = 35.0 / 3.6; // 9.7 m/s
                break;
            case "residential":
            case "tertiary":
                velocidad = 25.0 / 3.6;  // 6.9 m/s
                break;
            default:
                velocidad = 20.0 / 3.6;          // 5.5 m/s
        }
        return velocidad;
    }


    public boolean equals(Calle otra) {
        return (this.nombre == otra.getNombre());
    }

    public void addInterseccion(Interseccion interseccion) {
        if(!intersecciones.contains(interseccion)) {
            this.intersecciones.add(interseccion);
        }
    }

    public void mostrarIntersecciones() {
        for(int i = 0; i < this.intersecciones.size(); i++) {
            System.out.println(this.intersecciones.get(i).toString());
        }
    }

    public void ordenarIntersecciones(Map<String, Interseccion> mapaIntersecciones) {
        ArrayList<Interseccion> interseccionesOrdenadas = new ArrayList<>();

        for(String coordenada : this.nodos) {

            if(mapaIntersecciones.containsKey(coordenada)) {

                Interseccion i = mapaIntersecciones.get(coordenada);



                if(!interseccionesOrdenadas.contains(i)) {
                    interseccionesOrdenadas.add(i);
                }
            }



        }

        this.intersecciones = interseccionesOrdenadas;

    }

    public void mostrarSegmentos() {
        for(Segmento segmento : this.segmentos) {
            segmento.mostrarNodos();
        }



    }

}
