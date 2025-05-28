public class MethodPayment {
    private Integer idMethodPayment;
    private String type;
    private String details;
    private Subscription subscription;

    public MethodPayment() {
    }

    public MethodPayment(Integer idMethodPayment, String type, String details, Subscription subscription) {
        this.idMethodPayment = idMethodPayment;
        this.type = type;
        this.details = details;
        this.subscription = subscription;
    }

    public Integer getIdMethodPayment() {
        return idMethodPayment;
    }

    public void setIdMethodPayment(Integer idMethodPayment) {
        this.idMethodPayment = idMethodPayment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public String toString() {
        return "MethodPayment [idMethodPayment=" + idMethodPayment + ", type=" + type + ", details=" + details
                + ", subscription=" + subscription + "]";
    }

}
