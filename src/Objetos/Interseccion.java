package Objetos;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

public class Interseccion {
    private ArrayList<Calle> calles;
    private String coordenada;

    public Interseccion(String coordenada) {
        this.calles = new ArrayList<>();
        this.coordenada = coordenada;
    }

    public void addCalle(Calle calle) {
        this.calles.add(calle);
    }

    @Override
    public String toString() {
        String cad = "";

        cad += "Interseccion entre " + getStringCalles();

        return cad;
    }

    private String getStringCalles() {
        String nombreCalles = "";
        for(int i = 0; i < this.calles.size(); i++) {
            nombreCalles += this.calles.get(i).getNombre() + " ";
        }
        return nombreCalles;


    }
}
