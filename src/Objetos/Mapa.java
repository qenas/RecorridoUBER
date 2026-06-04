package Objetos;

import grafos.grafoDirigido.GrafoDirigido;
import org.json.JSONArray;
import org.json.JSONObject;


import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Mapa {
    private GrafoDirigido grafoIntersecciones, grafoPesos;
    private Map<String, Calle> callesPorNombre;
    private ArrayList<Calle> callesPorIndice;
    private Map<String, Interseccion> intersecciones;
    private int cantCalles;

    public Mapa(JSONObject jsonObject) {
        this.callesPorNombre = new HashMap<>();
        this.callesPorIndice = new ArrayList<>();
        cargarCalles(jsonObject);

        this.intersecciones = new HashMap<>();

        this.grafoIntersecciones = new GrafoDirigido(cantCalles);
        cargarGrafoIntersecciones();




        this.grafoPesos = new GrafoDirigido(this.intersecciones.size());
        cargarGrafoPesos();



    }

    private String normarlizarNombreCalle(String nombreCalle) {
        nombreCalle = Normalizer.normalize(nombreCalle, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return nombreCalle
                .toLowerCase()
                .replaceFirst("^(avenida|av\\.|pasaje|paseo|doctor|dr\\.)\\s+", "")
                .trim();
    }

    private ArrayList<String> getNodos(JSONObject jsonObject) {
        ArrayList<String> aux = new ArrayList<>();
        if(jsonObject.getString("type").equals("LineString")) {
            JSONArray coordinates = jsonObject.getJSONArray("coordinates");
            for(int i = 0; i < coordinates.length(); i++) {
                String nodo = coordinates.getJSONArray(i).toString();
                aux.add(nodo);
            }
        }
        return aux;
    }


    // metodo para cargar el  map de calles <NombreCalle, Calle>

    private void cargarCalles(JSONObject jsonObject) {
        JSONArray featuresArray = jsonObject.getJSONArray("features");
        int contadorCalles = 0;

        for(int i = 0; i < featuresArray.length(); i++) {
            JSONObject featureIndex = featuresArray.getJSONObject(i);

            JSONObject featureProperties = featureIndex.getJSONObject("properties");
            String nombreCalle = featureProperties.optString("name", "S/N");
            String tipoCalle = featureProperties.optString("highway", "residential");
            boolean esManoUnica = featureProperties.optString("oneway", "no").equals("yes");

            nombreCalle = normarlizarNombreCalle(nombreCalle);


            ArrayList<String> nodos = getNodos(featureIndex.getJSONObject("geometry"));

            if(!callesPorNombre.containsKey(nombreCalle)) { // si la calle todavia no se cargo.
                Calle calle = new Calle(contadorCalles, nombreCalle, tipoCalle, nodos, esManoUnica);
                this.callesPorNombre.put(nombreCalle, calle);
                this.callesPorIndice.add(calle);
                contadorCalles++;
            } else {
                this.callesPorNombre.get(nombreCalle).agregarNodos(nodos); // agrega los nuevos nodos a una calle ya existente
            }

            this.cantCalles = contadorCalles;
        }

    }



    public void mostrarCalles() {
        /*for(String key : this.callesPorNombre.keySet()) {
            Calle calle = this.callesPorNombre.get(key);
            System.out.println(calle.getId() + " Calle: " + calle.getNombre());
            //calle.mostrarNodos();

        }*/

        for(int i = 0; i < this.callesPorIndice.size(); i++) {
            Calle calle = this.callesPorIndice.get(i);
            System.out.println(calle.getId() + " Calle: " + calle.getNombre() + ". Tipo: " + calle.getTipo());
            calle.mostrarIntersecciones();
        }
    }


    // metodo para cargar el grafo de conexiones y obtener la matriz de adyacencia de 0 y 1;

    private void cargarGrafoIntersecciones() {
        this.grafoIntersecciones.cargarGrafoVacio();

        for(int i = 0; i < this.cantCalles; i++) {
            Calle calleA = this.callesPorIndice.get(i);
            ArrayList<String> calleNodosA = calleA.getNodos();

            for(int j = i + 1; j < this.cantCalles; j++) {
                Calle calleB = this.callesPorIndice.get(j);
                HashSet<String> calleNodosB = new HashSet<>(calleB.getNodos());
                boolean hayInterseccion = false;

                int indice = 0;

                while(indice < calleNodosA.size() && !hayInterseccion) {
                    String coordenada = calleNodosA.get(indice);
                    if(calleNodosB.contains(coordenada)) {

                        Interseccion interseccion;

                        if(!this.intersecciones.containsKey(coordenada)) { // si no existe un objeto interseccion asociado a esa coordenada, crea uno nuevo y lo pone en el Map
                            interseccion = new Interseccion(coordenada);

                            interseccion.addCalle(calleA);
                            interseccion.addCalle(calleB);

                            this.intersecciones.put(coordenada, interseccion);

                        } else {

                            interseccion = this.intersecciones.get(coordenada);
                        }


                        calleA.addInterseccion(interseccion);
                        calleB.addInterseccion(interseccion);


                        this.grafoIntersecciones.actualizarArista(1, i, j);
                        this.grafoIntersecciones.actualizarArista(1, j, i); // INTERSECCION SIMETRICA
                        hayInterseccion = true;
                    }
                    indice++;
                }

            }
        }
    }


    public void mostrarIntersecciones() {
        for(String key : this.intersecciones.keySet()) {
            System.out.println(this.intersecciones.get(key).toString());
        }
        System.out.println(this.intersecciones.size());
    }


    private void cargarGrafoPesos() {
        this.grafoPesos.cargarGrafoVacio();

        for(int i = 0; i < this.grafoPesos.getOrden(); i++) {

        }



    }

    public void mostrarMatrizDeIntersecciones() {
        for(int i = 0; i < this.cantCalles; i++) {
            for(int j = 0; j < this.cantCalles; j++) {
                if(i != j) {

                    if(this.grafoIntersecciones.getArista(i, j) == 1) {
                        System.out.println(this.callesPorIndice.get(i).getNombre() + " -> " + this.callesPorIndice.get(j).getNombre());
                    }
                }
            }
        }
    }

    public void mostrarMatrizDePesos() {

        for(int i = 0; i < this.cantCalles; i++) {
            for(int j = 0; j < this.cantCalles; j++) {
                if(i != j) {

                    if(this.grafoPesos.getArista(i, j) != this.grafoPesos.getInfinito()) {
                        System.out.println(this.callesPorIndice.get(i).getNombre() + " -> " + this.callesPorIndice.get(j).getNombre() + ": " + this.grafoPesos.getArista(i,j));

                    }
                }
            }
        }
       
    }
}
