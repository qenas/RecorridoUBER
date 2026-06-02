package Objetos;

import grafos.grafoDirigido.GrafoDirigido;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.image.AreaAveragingScaleFilter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Mapa {
    private GrafoDirigido conexiones, pesos;
    private Map<String, Calle> calles;
    private int cantCalles;

    public Mapa(JSONObject jsonObject) {
        this.calles = new HashMap<>();
        cargarCalles(jsonObject);
        this.conexiones = new GrafoDirigido(cantCalles);
        this.pesos = new GrafoDirigido(cantCalles);


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

    private void cargarCalles(JSONObject jsonObject) {
        JSONArray featuresArray = jsonObject.getJSONArray("features");
        int contadorCalles = 0;
        for(int i = 0; i < featuresArray.length(); i++) {
            JSONObject featureIndex = featuresArray.getJSONObject(i);

            JSONObject featureProperties = featureIndex.getJSONObject("properties");
            String nombreCalle = featureProperties.optString("name", "S/N");
            String tipoCalle = featureProperties.optString("highway", "residential");

            nombreCalle = normarlizarNombreCalle(nombreCalle);


            ArrayList<String> nodos = getNodos(featureIndex.getJSONObject("geometry"));

            if(!calles.containsKey(nombreCalle)) { // si la calle todavia no se cargo.
                this.calles.put(nombreCalle, new Calle(contadorCalles, nombreCalle, tipoCalle, nodos));
                contadorCalles++;
            } else {
                this.calles.get(nombreCalle).agregarNodos(nodos); // agrega los nuevos nodos a una calle ya existente
            }

            this.cantCalles = contadorCalles;
        }

    }

    public void mostrarCalles() {
        for(String key : this.calles.keySet()) {
            Calle calle = this.calles.get(key);
            System.out.println(calle.getId() + " Calle: " + calle.getNombre());
        }
    }



}
