package mapa;

public class Coordenada {
    private double latitud;
    private double longitud;

    public Coordenada(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }



    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public double haversine(Coordenada cord2) {
        final double R = 6371000; // Radio de la Tierra en metros

        double dLat = Math.toRadians(cord2.getLatitud() - this.latitud);
        double dLon = Math.toRadians(cord2.getLongitud() - this.longitud);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(this.latitud))
                * Math.cos(Math.toRadians(cord2.getLatitud()))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    @Override
    public String toString() {
        return "[" + longitud + "," + latitud + "]";
        //return "[" + latitud + "," + longitud + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coordenada)) return false;
        else {
            Coordenada cor2 = (Coordenada) obj;
            return (this.latitud==cor2.getLatitud()) && (this.longitud==cor2.getLongitud());
        }
    }

}
