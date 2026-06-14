package gestion;

public class ResultadoSimulacion {
    private int orden;
    private String idChofer;
    private String ubicacion;
    private String eta;
    private String aceptoViaje;

    public ResultadoSimulacion(int orden, String idChofer, String ubicacion, String eta, String aceptoViaje) {
        this.orden = orden;
        this.idChofer = idChofer;
        this.ubicacion = ubicacion;
        this.eta = eta;
        this.aceptoViaje = aceptoViaje;
    }

    // Getters (JavaFX los necesita obligatoriamente para llenar la tabla)
    public int getOrden() { return orden; }
    public String getIdChofer() { return idChofer; }
    public String getUbicacion() { return ubicacion; }
    public String getEta() { return this.eta; }
    public String getAceptoViaje() { return aceptoViaje; }

}
