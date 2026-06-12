package gestion;

import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;

public class JSONLector {

    public JSONObject getJSONObject(String path) {
        JSONObject json = null;
        try {
            String pathAux = Files.readString(Path.of(path));
            json = new JSONObject(pathAux);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if(json == null) {
            System.out.println("Error al cargar el archivo .JSON.");
        }

        return json;
    }




}
