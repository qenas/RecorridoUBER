package mapa;

import interfaz.MapaView;

import java.util.Map;

public class ConversorCoordenadas {
    private final Coordenada min;
    private final Coordenada max;

    public ConversorCoordenadas(Map<Coordenada, Interseccion> datos){
        double latMax=-9999, lonMax=-9999, latMin=9999, lonMin=9999;;
        //Busqueda de los limites geograficos
        for(Coordenada coord: datos.keySet()){
            if(coord.getLatitud()>latMax) latMax = coord.getLatitud();
            if(coord.getLongitud()>lonMax) lonMax = coord.getLongitud();
            if(coord.getLatitud()<latMin) latMin = coord.getLatitud();
            if(coord.getLongitud()<lonMin) lonMin = coord.getLongitud();
        }
        max = new Coordenada(lonMax, latMax);
        min = new Coordenada(lonMin, latMin);
    }

    public double toX(double longitud){
        return (longitud-min.getLongitud())/(max.getLongitud()-min.getLongitud())*MapaView.WIDTH_MAP;
    }

    public double toY(double latitud){
        return (max.getLatitud()-latitud)/(max.getLatitud()-min.getLatitud())*MapaView.HEIGHT_MAP;
    }

    public double toLongitud(double x) {
        return (x / MapaView.WIDTH_MAP) * (max.getLongitud() - min.getLongitud()) + min.getLongitud();
    }

    public double toLatitud(double y) {
        return max.getLatitud() - (y / MapaView.HEIGHT_MAP) * (max.getLatitud() - min.getLatitud());
    }

}
