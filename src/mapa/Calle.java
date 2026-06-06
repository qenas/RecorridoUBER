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

    public Calle(int id, String nombre, String tipo, boolean manoUnica) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.manoUnica = manoUnica;
        this.nodos = new ArrayList<>();
        this.intersecciones = new ArrayList<>();
        this.segmentos = new ArrayList<>();
    }

    public void addSegmento(ArrayList<String> segmento) {
        Segmento nuevoSegmento = new Segmento(segmento);
        this.segmentos.add(nuevoSegmento);
    }

    public void setNodos(ArrayList<String> nodos) {
        this.nodos = nodos;
    }

    public void addNodos(ArrayList<String> nodos) {
        for(String nodo : nodos) {
            if(!this.nodos.contains(nodo)) {
                this.nodos.add(nodo);
            }
        }
    }

    public ArrayList<String> getNodos() {
        return this.nodos;
    }

    public ArrayList<Segmento> getSegmentos() {
        return this.segmentos;
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
        //ArrayList<Interseccion> interseccionesOrdenadas = new ArrayList<>();

        /*ordenarSegmentos();

        for(Segmento segmento : segmentos) {

            for(String coord : segmento.getNodos()) {

                System.out.println(coord);


                Interseccion i = mapaIntersecciones.get(coord);


                if(i != null && !interseccionesOrdenadas.contains(i)) {
                    interseccionesOrdenadas.add(i);
                }
            }
        }

        /*System.out.println("Intersecciones ordenadas de " + this.nombre);

        for(Interseccion i : this.intersecciones) {
            System.out.println(i.getID() + " " + i.getCoordenada());
        }*/

        ArrayList<Interseccion> ordenadas = new ArrayList<>();

        for(Segmento segmento : segmentos) {

            for(String coord : segmento.getNodos()) {

                Interseccion i = mapaIntersecciones.get(coord);

                if(i != null &&
                        !ordenadas.contains(i)) {

                    ordenadas.add(i);

                }
            }
        }

        this.intersecciones = ordenadas;

        //this.intersecciones = interseccionesOrdenadas;

    }

    public void ordenarSegmentos() {

        ArrayList<Segmento> ordenados = new ArrayList<>();

        Segmento actual = encontrarPrimerSegmento();

        if(actual == null) {
            System.out.println("error al encontrar el primer segmento");
            return;
        }

        System.out.println(
                "PRIMER SEGMENTO: "
                        + actual.getInicio()
                        + " -> "
                        + actual.getFin()
        );

        ordenados.add(actual);

        while(ordenados.size() < segmentos.size()) {

            Segmento siguiente = null;

            for(Segmento s : segmentos) {

                if(!ordenados.contains(s) &&
                        actual.getFin().equals(s.getInicio())) {

                    siguiente = s;
                    break;
                }
            }

            if(siguiente == null) {
                break;
            }

            ordenados.add(siguiente);
            actual = siguiente;

        }

        this.segmentos = ordenados;

    }

    public void mostrarSegmentos() {
        Segmento primero = encontrarPrimerSegmento();

        System.out.println(
                "PRIMER SEGMENTO: "
                        + primero.getInicio()
                        + " -> "
                        + primero.getFin()
        );

        for(Segmento segmento : this.segmentos) {
            System.out.println("inicio: " + segmento.getInicio() + " -> final: " + segmento.getFin());
            segmento.mostrarNodos();
        }
    }

    private Segmento encontrarPrimerSegmento() {
        for (Segmento segmento : segmentos) {

            boolean tieneAnterior = false;

            int index = 0;

            while (index < segmentos.size() && !tieneAnterior) {
                Segmento otro = segmentos.get(index);
                if(segmento != otro) {

                    if(segmento.getInicio().equals(otro.getFin())) {

                        tieneAnterior = true;

                    }
                }
                index++;
            }

            if(!tieneAnterior) {
                return segmento;
            }


        }
        return null;
    }

}
