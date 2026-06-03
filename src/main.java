import Objetos.Mapa;
import gestion.JSONLector;
import grafos.grafoDirigido.GrafoDirigido;
import gestion.Lector;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class main {
    public static void main(String[] args) {
        /*Lector lector = new Lector();

        lector.setup();

        Map<String, Integer> aux = lector.getPuntosUnicos();

        System.out.println(aux.size());

        //lector.mostrarCalles();


        /*for(String key : aux.keySet()) {
            System.out.println(aux.get(key));
        }

        GrafoDirigido mapaCoste = new GrafoDirigido(aux.size());
        mapaCoste.cargarGrafoVacio();


        lector.cargarGrafoCoste(mapaCoste);

        //mapaCoste.muestraGrafo();

       // lector.mostrarCalles();




        Map<String, JSONArray> calles = lector.getCalles();

        GrafoDirigido callesPesos = new GrafoDirigido(calles.size());

        callesPesos.cargarGrafoVacio();

        lector.cargarGrafoCalles();



        */

        JSONLector jsonLector = new JSONLector();


        JSONObject salta = jsonLector.getJSONObject("src/archivos/CentroyMacroSALTA.geojson");

        Mapa mapa = new Mapa(salta);

        //mapa.mostrarCalles();
        //mapa.mostrarMatrizDeIntersecciones();
        mapa.mostrarMatrizDePesos();





    }
}
