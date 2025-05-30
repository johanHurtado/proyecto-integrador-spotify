package entities;

/**
 * Entidad que representa la tabla 'metodo_pago'.
 */
public class PaymentMethod {
    private int idPago;
    private int idUsuario;
    private String numeroTarjeta;
    private String fechaVencimiento; // formato MM/AA
    private String cvc;

    public PaymentMethod() { }

    public PaymentMethod(int idPago,
                         int idUsuario,
                         String numeroTarjeta,
                         String fechaVencimiento,
                         String cvc) {
        this.idPago           = idPago;
        this.idUsuario        = idUsuario;
        this.numeroTarjeta     = numeroTarjeta;
        this.fechaVencimiento  = fechaVencimiento;
        this.cvc               = cvc;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
               "idPago=" + idPago +
               ", idUsuario=" + idUsuario +
               ", numeroTarjeta='" + numeroTarjeta + '\'' +
               ", fechaVencimiento='" + fechaVencimiento + '\'' +
               ", cvc='" + cvc + '\'' +
               '}';
    }
}
