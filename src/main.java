import mapa.Mapa;
import gestion.JSONLector;
import org.json.JSONObject;

public class main {
    public static void main(String[] args) {

        JSONLector jsonLector = new JSONLector();


        JSONObject salta = jsonLector.getJSONObject("src/archivos/CentroyMacroSALTA.geojson");

        Mapa mapa = new Mapa(salta);

        //mapa.ordenarInterseccionesCalle();


        mapa.mostrarCalles();

        //mapa.mostrarMatrizDeIntersecciones();

        //mapa.mostrarIntersecciones();

        //mapa.mostrarMatrizDePesos();

        //mapa.simularRecorrido(72, 649);


    }
}
