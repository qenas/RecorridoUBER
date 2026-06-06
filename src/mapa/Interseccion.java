package mapa;

import java.util.ArrayList;

public class Interseccion {
    private int ID;
    private ArrayList<Calle> calles;
    private Coordenada coordenada;

    public Interseccion(Coordenada coordenada) {
        this.calles = new ArrayList<>();
        this.coordenada = coordenada;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public int getID() {
        return this.ID;
    }

    public void setCalles(ArrayList<Calle> calles) {
        this.calles = calles;
    }

    public void addCalle(Calle calle) {
        if(!this.calles.contains(calle)) {
            this.calles.add(calle);
        }
    }

    public Coordenada getCoordenada() {
        return this.coordenada;
    }

    @Override
    public String toString() {
        String cad = "";

        cad += "Interseccion " + this.ID + " " + this.coordenada + " entre " + getStringCalles() + "cant " + this.calles.size();

        return cad;
    }

    public String getDescripcion() {

        StringBuilder sb = new StringBuilder("(");

        for(int i = 0; i < calles.size(); i++) {

            sb.append(calles.get(i).getNombre());

            if(i < calles.size()-1) {
                sb.append(", ");
            }
        }

        sb.append(")");

        return sb.toString();
    }

    private String getStringCalles() {
        String nombreCalles = "";
        for(int i = 0; i < this.calles.size(); i++) {
            nombreCalles += this.calles.get(i).getNombre() + " ";
        }
        return nombreCalles;


    }
}
