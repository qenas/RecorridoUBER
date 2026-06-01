import grafos.grafoDirigido.GrafoDirigido;
import lector.Lector;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class main {
    public static void main(String[] args) {
        Lector lector = new Lector();

        lector.setup();

        Map<String, Integer> aux = lector.getPuntosUnicos();

        System.out.println(aux.size());

        //lector.mostrarCalles();


        /*for(String key : aux.keySet()) {
            System.out.println(aux.get(key));
        }*/

        GrafoDirigido mapaCoste = new GrafoDirigido(aux.size());
        mapaCoste.cargarGrafoVacio();


        lector.cargarGrafoCoste(mapaCoste);

        //mapaCoste.muestraGrafo();

        lector.mostrarCalles();




        Map<String, JSONArray> calles = lector.getCalles();

        GrafoDirigido callesPesos = new GrafoDirigido(calles.size());

        callesPesos.cargarGrafoVacio();

        for(int i = 0; i < mapaCoste.getOrden(); i++) {
            for(int j = 0; j < mapaCoste.getOrden(); j++) {

            }
        }



    }
}
