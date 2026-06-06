package mapa;

import java.util.ArrayList;
import java.util.Map;

public class Calle {
    private int id;
    private String nombre, tipo;

    //parche

    private boolean manoUnica;

    public Calle(int id, String nombre, String tipo, boolean manoUnica) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.manoUnica = manoUnica;

    }


    public String getTipo() {
        return this.tipo;
    }

    public boolean isManoUnica() {
        return this.manoUnica;
    }

    public int getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }


    public double getVelocidad() {
        double velocidad = 0;
        switch (this.tipo) {
            case "primary":
                velocidad = 45.0 / 3.6;   // 12.5 m/s
                break;
            case "secondary":
                velocidad = 35.0 / 3.6; // 9.7 m/s
                break;
            case "residential":
            case "tertiary":
                velocidad = 25.0 / 3.6;  // 6.9 m/s
                break;
            default:
                velocidad = 20.0 / 3.6;          // 5.5 m/s
        }
        return velocidad;
    }


    public boolean equals(Calle otra) {
        return (this.nombre == otra.getNombre());
    }


}
