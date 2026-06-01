package lector;


import grafos.grafoDirigido.GrafoDirigido;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

public class Lector {
    private String contenido;
    private JSONObject json;
    private JSONArray features;
    private Map<String, JSONArray> calles;
    private Map<String, Integer> puntosUnicos;


    private int contadorId;

    public void setup() {
        try {
            contenido = Files.readString(Path.of("src/archivos/CentroyMacroSALTA.geojson"));
            json = new JSONObject(contenido); // transforma el archivo a un objeto 'json'
            features = json.getJSONArray("features"); // lee el apartado "features" y crea un array a partir de el
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        cargarEsquinas();
        cargarCalles();

    }

    private static String normalizar(String calle) {
        calle = Normalizer.normalize(calle, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return calle
                .toLowerCase()
                .replaceFirst("^(avenida|av\\.|pasaje|paseo|doctor|dr\\.)\\s+", "")
                .trim();
    }

    private void cargarCalles() {
        this.calles = new HashMap<>();

        for(int i = 0; i < features.length(); i++) {
            JSONObject index = features.getJSONObject(i);
            JSONObject indexProperties = index.getJSONObject("properties");
            String nombreCalle = indexProperties.optString("name", "S/N");

            nombreCalle = normalizar(nombreCalle);


            JSONArray cuadras = index.getJSONObject("geometry").getJSONArray("coordinates");

            if(!calles.containsKey(nombreCalle)) { // si la calle todavia no se cargo.
                this.calles.put(nombreCalle, cuadras);
            } else {
                JSONArray aux = calles.get(nombreCalle); // si se detecto de nuevo una calle existente, carga de nuevo el array agregando los nuevos nodos
                for(int j = 0; j < cuadras.length(); j++) {
                    aux.put(cuadras.get(j));
                }
                this.calles.put(nombreCalle, aux);
            }
        }
    }

    private void cargarEsquinas() {
        this.puntosUnicos = new HashMap<>();

        this.contadorId = 0;

        for(int i = 0; i < features.length(); i++) {
            JSONObject geometry = features.getJSONObject(i).getJSONObject("geometry");
            if(geometry.getString("type").equals("LineString")) {
                JSONArray coords = geometry.getJSONArray("coordinates");
                for(int j = 0; j < coords.length(); j++) {
                    String punto = coords.getJSONArray(j).toString();

                    if(!puntosUnicos.containsKey(punto)) {
                        //System.out.println(punto + ", " + contadorId);
                        puntosUnicos.put(punto, contadorId);
                        contadorId++;

                    }
                }
            }
        }

    }

    public Map<String, Integer> getPuntosUnicos() {
        return puntosUnicos;
    }

    public void cargarGrafoCoste(GrafoDirigido grafo) {
        for(int i = 0; i < features.length(); i++) {
            JSONObject featuresIndex = features.getJSONObject(i);
            JSONObject properties = featuresIndex.getJSONObject("properties");
            JSONArray coords = featuresIndex.getJSONObject("geometry").getJSONArray("coordinates");

            boolean esManoUnica = properties.optString("oneway", "no").equals("yes");
            String nombreCalle = properties.optString("name", "S/N");


            for(int j = 0; j < coords.length() - 1; j++) {
                int u = puntosUnicos.get(coords.getJSONArray(j).toString());
                int v = puntosUnicos.get(coords.getJSONArray(j+1).toString());

                //System.out.println("Cargando calle: " + nombreCalle + "(" + u + ", " + v + ")");

                grafo.actualizarArista(1, u, v);

                if(!esManoUnica) {
                    grafo.actualizarArista(1, v, u);
                }
            }

        }
    }

    public void mostrarCalles() {
        for(String key: this.calles.keySet()) {
            System.out.println(key);
            JSONArray aux = this.calles.get(key);

            String cad = "";

            /*for(int i = 0; i < aux.length(); i++) {
                String punto = aux.getJSONArray(i).toString();
                cad += punto + " ";
            }*/
            //System.out.println(cad);
        }
        System.out.println(this.calles.size());
    }

    public Map<String, JSONArray> getCalles() {
        return this.calles;
    }


}
