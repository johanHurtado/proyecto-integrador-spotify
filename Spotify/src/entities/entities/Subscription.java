package entities;


public class Subscription {
    private Integer idSuscripcion;
    private String nombreSuscripcion;
    private String beneficiosSuscripcion;
    private String descripcionSuscripcion;

    public Subscription() { }

    public Subscription(Integer idSuscripcion,
                        String nombreSuscripcion,
                        String beneficiosSuscripcion,
                        String descripcionSuscripcion) {
        this.idSuscripcion         = idSuscripcion;
        this.nombreSuscripcion     = nombreSuscripcion;
        this.beneficiosSuscripcion = beneficiosSuscripcion;
        this.descripcionSuscripcion= descripcionSuscripcion;
    }

    public Integer getIdSuscripcion() {
        return idSuscripcion;
    }
    public void setIdSuscripcion(Integer idSuscripcion) {
        this.idSuscripcion = idSuscripcion;
    }

    public String getNombreSuscripcion() {
        return nombreSuscripcion;
    }
    public void setNombreSuscripcion(String nombreSuscripcion) {
        this.nombreSuscripcion = nombreSuscripcion;
    }

    public String getBeneficiosSuscripcion() {
        return beneficiosSuscripcion;
    }
    public void setBeneficiosSuscripcion(String beneficiosSuscripcion) {
        this.beneficiosSuscripcion = beneficiosSuscripcion;
    }

    public String getDescripcionSuscripcion() {
        return descripcionSuscripcion;
    }
    public void setDescripcionSuscripcion(String descripcionSuscripcion) {
        this.descripcionSuscripcion = descripcionSuscripcion;
    }

    @Override
    public String toString() {
        return "Subscription{" +
               "idSuscripcion=" + idSuscripcion +
               ", nombreSuscripcion='" + nombreSuscripcion + '\'' +
               ", beneficiosSuscripcion='" + beneficiosSuscripcion + '\'' +
               ", descripcionSuscripcion='" + descripcionSuscripcion + '\'' +
               '}';
    }
}
