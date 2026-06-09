import mapa.Mapa;
import gestion.JSONLector;
import org.json.JSONObject;

import java.util.ArrayList;

public class main {
    public static void main(String[] args) {

        JSONLector jsonLector = new JSONLector();

        JSONObject salta = jsonLector.getJSONObject("src/archivos/CentroyMacroSALTA.geojson");

        Mapa mapa = new Mapa(salta);

        UberApp uberApp = new UberApp(mapa);





        //mapa.mostrarCalles();

        mapa.mostrarIntersecciones();

        //mapa.mostrarMatrizDePesos();

        //mapa.simularRecorrido(415, 511);

    }
}
