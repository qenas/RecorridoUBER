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
    private Map<Coordenada, Interseccion> intersecciones;
     // coordendas en string

    public Mapa(JSONObject jsonObject) {
        this.intersecciones = new HashMap<>();
        this.calles = new HashMap<>();

        cargar(jsonObject);

        this.grafoPesos = new GrafoDirigido(this.intersecciones.size());
        cargarGrafoPesos(jsonObject);
        realizarAlgoritmosGrafo();
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

        Map<Integer, Interseccion> aux = new HashMap<>();


        int contadorCalles = 0;
        int contadorCoordenadas = 0;

        for (int i = 0; i < features.length(); i++) {
            feature = features.getJSONObject(i);
            JSONObject geom = feature.getJSONObject("geometry");
            JSONObject properties = feature.getJSONObject("properties");
            String nombreCalle = properties.optString("name", "s/n");

            nombreCalle = normarlizarNombreCalle(nombreCalle);

            if (geom.getString("type").equals("LineString") && nombreCalle != null) { //Verificacion de que el feature sea lineString y la calle tenga nombre
                JSONArray coords = geom.getJSONArray("coordinates");
                for (int j = 0; j < coords.length(); j++) {
                    Coordenada coord = new Coordenada((coords.getJSONArray(j)).getDouble(0), (coords.getJSONArray(j)).getDouble(1));
                    String tipoCalle = properties.optString("highway", "residential");

                    // Lógica de DOBLE MANO:
                    // Si 'oneway' es 'yes', es mano única.
                    // Si es 'no' o no existe el tag, es doble mano.

                    boolean esManoUnica = properties.optString("oneway", "no").equals("yes");

                    Calle calle;

                    Interseccion interseccion = new Interseccion(coord);

                    if (!this.calles.containsKey(nombreCalle)) { // carga la cantidad de calles unicas
                        calle = new Calle(contadorCalles, nombreCalle, tipoCalle, esManoUnica); // si el mapa de calles no contiene una calle asociada a 'nombreCalle', crea una nueva
                        this.calles.put(nombreCalle, calle);
                        contadorCalles++;
                    } else {
                        calle = this.calles.get(nombreCalle); // si el mapa de calles  contiene una calle asociada a 'nombreCalle', la obtiene a partir de este ultimo
                    }

                    int pos = buscarCoordenada(aux, interseccion);


                    if (pos == -1) { // no existe una coordenada
                        interseccion.setID(contadorCoordenadas);
                        interseccion.addCalle(calle);
                        aux.put(contadorCoordenadas, interseccion);
                        contadorCoordenadas++;
                    } else {
                        aux.get(pos).addCalle(calle);
                    }


                }
            }
        }

        depurarIntersecciones(aux); // depura las intesercciones con menos de 2 calles asociadas y las carga en el mapa 'intersecciones'

    }

    private void depurarIntersecciones(Map<Integer, Interseccion> mapa) {
        // - A partir de un mapa de puntos unicos, depura las intersecciones como aquellas que poseen mas de una calle asociada
        // - Codigo de Matias.
        int indiceIntersecciones = 0;

        for(Integer i : mapa.keySet()) {
            Interseccion interseccion = mapa.get(i);
            if(interseccion.getCantCalles() > 1) {
                //System.out.println(indiceIntersecciones);
                interseccion.setID(indiceIntersecciones);
                this.intersecciones.put(interseccion.getCoordenada(), interseccion);
                indiceIntersecciones++;
            }
        }

    }

    private int buscarCoordenada(Map<Integer, Interseccion> mapa, Interseccion busc){
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

    private void cargarGrafoPesos(JSONObject jsonObject) {
        this.grafoPesos.cargarGrafoVacio();

        JSONArray features = jsonObject.getJSONArray("features");

        for(int i = 0; i < features.length(); i++) {
            JSONObject f = features.getJSONObject(i); // obtiene la calle
            JSONObject properties = f.getJSONObject("properties");
            JSONArray coords = f.getJSONObject("geometry").getJSONArray("coordinates"); // obtiene las coordenadas de la calle

            String nombreCalle = properties.optString("name", "s/n");

            nombreCalle = normarlizarNombreCalle(nombreCalle);

            if(this.calles.containsKey(nombreCalle)) {
                Calle calle = this.calles.get(nombreCalle);
                boolean esManoUnica = calle.isManoUnica();

                Interseccion ultimaInterseccion = null;

                Coordenada coordAnterior = null;

                for(int j = 0; j < coords.length(); j++) {
                    JSONArray punto = coords.getJSONArray(j);

                    Coordenada coordActual = new Coordenada(punto.getDouble(0), punto.getDouble(1));

                    if(this.intersecciones.containsKey(coordActual)) {
                        //System.out.println("hay interseccion asociada a esa coordenada");
                        Interseccion interseccionActual = this.intersecciones.get(coordActual);
                        if(interseccionActual != null) {
                            //System.out.println("entra al if");
                            if(ultimaInterseccion == null) {
                                ultimaInterseccion = interseccionActual;
                            } else {
                               // System.out.println("entra al else");
                                int u = ultimaInterseccion.getID();
                                int v = interseccionActual.getID();
                                //System.out.println("u: " + u + " v: " + v);

                                if(u != v) {
                                    double costo = calle.getVelocidad();

                                    this.grafoPesos.actualizarArista(costo, u, v);

                                    if(!esManoUnica) {
                                        this.grafoPesos.actualizarArista(costo, v, u);
                                    }
                                }
                                ultimaInterseccion = interseccionActual;
                            }
                        }
                    } else {
                        //System.out.println("no hay interseccion asociada a esa coordenada");
                    }
                    coordAnterior = coordActual;

                }
            }
        }
    }

    //metodo auxiliar para mostrar la matriz de pesos

    private Map<Integer, Interseccion> armarMapaPorId() {
        Map<Integer, Interseccion> aux = new HashMap<>();

        for(Coordenada c : this.intersecciones.keySet()) {
            Interseccion interseccion = this.intersecciones.get(c);
            int idInterseccion = interseccion.getID();
            aux.put(idInterseccion, interseccion);
        }

        return aux;
    }

    //metodo privado que realiza el Dijkstra y Floyd en el grafo
    private void realizarAlgoritmosGrafo() {
        this.grafoPesos.realizarFloyd();
    }

    public void mostrarMatrizDePesos() {
        //this.grafoPesos.muestraGrafo();

        Map<Integer, Interseccion> interseccionesPorID = armarMapaPorId();

        for(int i = 0; i < this.grafoPesos.getOrden(); i++) {
            for(int j = 0; j < this.grafoPesos.getOrden(); j++) {
                double costo = this.grafoPesos.getArista(i,j);
                if(i != j) {
                    if(costo != GrafoDirigido.getInfinito()) {

                        Interseccion origen = interseccionesPorID.get(i);
                        Interseccion destino = interseccionesPorID.get(j);


                        System.out.println(origen.getDescripcion() + " -> " + destino.getDescripcion() + " costo: " + costo);
                    }
                }
            }
        }
    }

    public GrafoDirigido getGrafoPesos() {
        return this.grafoPesos;
    }

    public Interseccion getInterseccion(int index) {
        Interseccion aux = null;
        for(Coordenada c : this.intersecciones.keySet()) {
            Interseccion interseccion = this.intersecciones.get(c);
            if(interseccion.getID() == index) {
                aux = interseccion;
            }
        }

        if(aux == null) {
            System.out.println("no existe interseccion asociada a esa id");
        }
        return aux;
    }

    public void simularRecorrido(int origen, int destino) { // simulacion por IDs de intersecciones


        this.grafoPesos.muestraCaminoFloyd(origen, destino);

        Map<Integer, Interseccion> interseccionesPorID = armarMapaPorId();

        ArrayList<Integer> camino = this.grafoPesos.getCaminoFloyd(origen, destino);

        for(int i = 0; i < camino.size(); i++) {
            int idInterseccion = camino.get(i);
            System.out.println(interseccionesPorID.get(idInterseccion).toString());
        }

    }

    public void mostrarCalles() {
        for(String nombreCalle : this.calles.keySet()) {
            Calle calle = this.calles.get(nombreCalle);
            System.out.println(calle.getId() + " Calle: " + calle.getNombre() + ". Tipo: " + calle.getTipo());
        }

        System.out.println(this.calles.size());
    }

    public void mostrarIntersecciones() {

        for(Coordenada coord : this.intersecciones.keySet()) {
            Interseccion interseccion = this.intersecciones.get(coord);
            System.out.println(interseccion.toString());
        }
        System.out.println(this.intersecciones.size());

    }
}
