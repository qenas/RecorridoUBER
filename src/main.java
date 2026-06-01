import grafos.grafoDirigido.GrafoDirigido;
import lector.Lector;

import java.util.HashMap;
import java.util.Map;

public class main {
    public static void main(String[] args) {
        Lector lector = new Lector();

        lector.setup();

        Map<String, Integer> aux = lector.getPuntosUnicos();

        System.out.println(aux.size());

        lector.mostrarCalles();


        /*for(String key : aux.keySet()) {
            System.out.println(aux.get(key));
        }*/

        GrafoDirigido mapaCoste = new GrafoDirigido(aux.size());
        mapaCoste.cargarGrafoVacio();


        lector.cargarGrafoCoste(mapaCoste);

        //mapaCoste.muestraGrafo();

        Map<Integer, String> calles = new HashMap<>();

    }
}
