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

    public ArrayList<Calle> getCalles() {
        return this.calles;
    }



    public void setID(int id) {
        this.ID = id;
    }

    public int getID() {
        return this.ID;
    }

    public int getCantCalles() {
        return this.calles.size();
    }

    public void setCalles(ArrayList<Calle> calles) {
        this.calles = calles;
    }

    public void addCalle(Calle calle) {
        if(!this.calles.contains(calle)) {
            this.calles.add(calle);
        }
    }

    public Calle calleCompartida(Interseccion inter2) {
        Calle c=null;
        for(int i=0; i < this.calles.size(); i++) {
            Calle calle1 = this.calles.get(i);
            ArrayList<Calle> inter2Calles = inter2.getCalles();
            for(int j=0; j < inter2Calles.size(); j++) {
                Calle calle2 = inter2Calles.get(j);
                if(calle1.equals(calle2) ) {
                    c=calle1;
                }
            }
        }
        return c;
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
            nombreCalles += this.calles.get(i).getNombre() + " / ";
        }
        return nombreCalles;


    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Interseccion)) return false;
        else {
            Interseccion int2 = (Interseccion) obj;
            return (this.coordenada.equals(int2.getCoordenada()));
        }
    }
}
