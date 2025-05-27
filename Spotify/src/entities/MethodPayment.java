public class MethodPayment {
    private Integer id;
    private String type;
    private String details;
    private Subscription subscription;

    public MethodPayment() {
    }

    public MethodPayment(Integer id, String type, String details, Subscription subscription) {
        this.id = id;
        this.type = type;
        this.details = details;
        this.subscription = subscription;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return "MethodPayment [id=" + id + ", type=" + type + ", details=" + details + ", subscription=" + subscription
                + "]";
    }
}
