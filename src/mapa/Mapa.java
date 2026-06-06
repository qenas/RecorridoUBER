package mapa;

import grafos.grafoDirigido.GrafoDirigido;
import org.json.JSONArray;
import org.json.JSONObject;


import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Mapa {
    private GrafoDirigido grafoPesos;
    private Map<String, Calle> calles;
    private Map<Integer, Interseccion> intersecciones;

    public Mapa(JSONObject jsonObject) {
        this.intersecciones = new HashMap<>();
        this.calles = new HashMap<>();

        cargar(jsonObject);

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


    // metodo para cargar el  map de calles <NombreCalle, Calle> y el map de intersecciones

    private void cargar(JSONObject jsonObject) {
        JSONArray features = jsonObject.getJSONArray("features");
        JSONObject feature;

        int contadorCalles = 0;
        int contadorIntersecciones = 0;

        for(int i = 0; i < features.length(); i++) {
            feature = features.getJSONObject(i);
            JSONObject geom = feature.getJSONObject("geometry");
            JSONObject properties = feature.getJSONObject("properties");
            String nombreCalle = properties.optString("name", "s/n");

            nombreCalle = normarlizarNombreCalle(nombreCalle);

            if (geom.getString("type").equals("LineString") && nombreCalle!=null) { //Verificacion de que el feature sea lineString y la calle tenga nombre
                JSONArray coords = geom.getJSONArray("coordinates");
                for (int j = 0; j < coords.length(); j++) {
                    Coordenada coord = new Coordenada((coords.getJSONArray(j)).getDouble(0), (coords.getJSONArray(j)).getDouble(1));
                    String tipoCalle = properties.optString("highway", "residential");

                    // Lógica de DOBLE MANO:
                    // Si 'oneway' es 'yes', es mano única.
                    // Si es 'no' o no existe el tag, es doble mano.

                    boolean esManoUnica = properties.optString("oneway", "no").equals("yes");
                    ArrayList<Calle> callesInterseccion = new ArrayList<>();

                    Calle calle = new Calle(contadorCalles, nombreCalle, tipoCalle, esManoUnica);
                    callesInterseccion.add(calle);

                    Interseccion interseccion = new Interseccion(coord);
                    interseccion.setCalles(callesInterseccion);

                    if(!this.calles.containsKey(nombreCalle)) {
                        this.calles.put(nombreCalle, calle);
                        contadorCalles++;
                    }

                    int pos = buscarInterseccion(this.intersecciones, interseccion);


                    if(pos==-1) {
                        interseccion.setID(contadorIntersecciones);
                        this.intersecciones.put(contadorIntersecciones, interseccion);
                        contadorIntersecciones++;
                    } else {
                        this.intersecciones.get(pos).addCalle(calle);
                    }

                }
            }
        }
    }


    private int buscarInterseccion(Map<Integer, Interseccion> mapa, Interseccion busc){
        boolean detectado = false;
        int pos=0;

        for(Integer i : mapa.keySet()) {
            Interseccion interseccion = mapa.get(i);
            double dlon = interseccion.getCoordenada().getLongitud() - busc.getCoordenada().getLongitud();
            double dlat = interseccion.getCoordenada().getLatitud() - busc.getCoordenada().getLatitud();

            if(dlon == 0 && dlat == 0) {
                detectado=true;
                pos = i;
            }
        }

        if(detectado) {
            return pos;
        }

        return -1;

    }


    public void mostrarCalles() {
        for(String nombreCalle : this.calles.keySet()) {
            Calle calle = this.calles.get(nombreCalle);
            System.out.println(calle.getId() + " Calle: " + calle.getNombre() + ". Tipo: " + calle.getTipo());
        }

        System.out.println(this.calles.size());
    }







    private void cargarGrafoPesos() {
        this.grafoPesos.cargarGrafoVacio();


        // una vez ordenadas las intersecciones de las calles, se carga el grafo a partir de las calles



    }


    public void mostrarMatrizDePesos() {
        //this.grafoPesos.muestraGrafo();

        for(int i = 0; i < this.grafoPesos.getOrden(); i++) {
            for(int j = 0; j < this.grafoPesos.getOrden(); j++) {

                double costo = this.grafoPesos.getArista(i,j);

                if(i != j) {

                    if(costo != GrafoDirigido.getInfinito()) {

                        Interseccion origen = this.intersecciones.get(i);
                        Interseccion destino = this.intersecciones.get(j);


                        System.out.println(origen.getDescripcion() + " -> " + destino.getDescripcion() + " costo: " + costo);

                    }

                }

            }
        }

    }


    public void simularRecorrido(int origen, int destino) { // simulacion por IDs de intersecciones
        this.grafoPesos.muestraFloyd();

        this.grafoPesos.muestraCaminoFloyd(origen, destino);

        ArrayList<Integer> camino = this.grafoPesos.getCaminoFloyd(origen, destino);

        for(int i = 0; i < camino.size(); i++) {
            int idInterseccion = camino.get(i);
            System.out.println(this.intersecciones.get(idInterseccion).toString());
        }



    }
}
