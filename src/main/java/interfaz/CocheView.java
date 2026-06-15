package interfaz;

import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import mapa.ConversorCoordenadas;
import mapa.Coordenada;
import servicio.Chofer;

import java.util.ArrayList;

public class CocheView extends Rectangle{
    Chofer chofer;
    ConversorCoordenadas conversor;

    public CocheView(Chofer chofer){
        this.chofer = chofer;
        this.conversor = null;
        setFill(Color.GREEN);
    }
    public CocheView(Chofer chofer, ConversorCoordenadas conversor){
        super(conversor.toX(chofer.getPosicion().getCoordenada().getLongitud())-5, conversor.toY(chofer.getPosicion().getCoordenada().getLatitud())-4, 11, 9);
        this.chofer = chofer;
        this.conversor = conversor;
        setArcWidth(5);
        setArcHeight(5);
        setFill(Color.GREEN);
    }



    public Chofer getChofer() {
        return chofer;
    }
    /*
    public void mover(ColaSLinkedList recorrido){
        Path path = new Path();
        if(!recorrido.estaVacia()){
            Coordenada coord = (Coordenada)recorrido.sacar();
            path.getElements().add(new MoveTo(conversor.toX(coord.getLongitud()), conversor.toY(coord.getLatitud())));
            while(!recorrido.estaVacia()){
                coord = (Coordenada)recorrido.sacar();
                path.getElements().add(new LineTo(conversor.toX(coord.getLongitud()), conversor.toY(coord.getLatitud())));
            }
        }
        PathTransition movimiento = new PathTransition(Duration.millis(100), path, this);
        movimiento.play();
    }*/

    public void actualizar(){/*
        if(chofer.estaOcupado()){
            this.setFill(Color.RED);
        } else {
            this.setFill(Color.GREEN);
        }
        Coordenada coord = chofer.getPosicion().getCoordenada();
        double actX = conversor.toX(coord.getLongitud());
        double actY = conversor.toY(coord.getLatitud());
        if(actX!=this.getX() || actY!=this.getY()){
            setX(actX-5);
            setY(actY-4);
        }*/
        if (chofer.estaOcupado()) {
            setFill(Color.RED);
        } else {
            setFill(Color.GREEN);
        }

        Coordenada coord = chofer.getPosicion().getCoordenada();

        double nuevoX = conversor.toX(coord.getLongitud()) - 5;
        double nuevoY = conversor.toY(coord.getLatitud()) - 4;

        Timeline animacion = new Timeline(
                new KeyFrame(
                        Duration.millis(500),
                        new KeyValue(xProperty(), nuevoX),
                        new KeyValue(yProperty(), nuevoY)
                )
        );

        animacion.play();
    }

    public void verPosicion(){
        System.out.println("actX=" + getX());
        System.out.println("actY=" + getY());
    }

    public void mover(ArrayList<Coordenada> recorrido){
        Path path = new Path();
        if(recorrido.size()!=0){
            Coordenada coord = recorrido.get(0);
            path.getElements().add(new MoveTo(conversor.toX(coord.getLongitud()), conversor.toY(coord.getLatitud())));
            for(int i=1; i<recorrido.size(); i++){
                coord = recorrido.get(i);
                path.getElements().add(new LineTo(conversor.toX(coord.getLongitud()), conversor.toY(coord.getLatitud())));
            }
        }
        PathTransition movimiento = new PathTransition(Duration.seconds(recorrido.size()/2.0), path, this);
        movimiento.play();

    }

    public void mover(ArrayList<Coordenada> recorrido, Runnable alTerminar) {
        Path path = new Path();
        if(recorrido.size()!=0){
            Coordenada coord = recorrido.get(0);
            path.getElements().add(new MoveTo(conversor.toX(coord.getLongitud()), conversor.toY(coord.getLatitud())));
            for(int i=1; i<recorrido.size(); i++){
                coord = recorrido.get(i);
                path.getElements().add(new LineTo(conversor.toX(coord.getLongitud()), conversor.toY(coord.getLatitud())));
            }
        }
        PathTransition movimiento = new PathTransition(Duration.seconds(recorrido.size()/2.0), path, this);
        movimiento.setOnFinished(e -> {
            if (alTerminar != null) {
                alTerminar.run();
            }
        });
        movimiento.play();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CocheView){
            return this.chofer.equals(((CocheView)obj).getChofer());
        } else return false;
    }

    @Override
    public int hashCode() {
        return this.chofer.hashCode();
    }
}
