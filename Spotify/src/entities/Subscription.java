public class Subscription {
    private Integer id;
    private String subName;
    private String subBenefits;
    private String subDescription;
    public Subscription() {
    
    }
    public Subscription(Integer id, String subName, String subBenefits, String subDescription) {
        this.id = id;
        this.subName = subName;
        this.subBenefits = subBenefits;
        this.subDescription = subDescription;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getSubName() {
        return subName;
    }
    public void setSubName(String subName) {
        this.subName = subName;
    }
    public String getSubBenefits() {
        return subBenefits;
    }
    public void setSubBenefits(String subBenefits) {
        this.subBenefits = subBenefits;
    }
    public String getSubDescription() {
        return subDescription;
    }
    public void setSubDescription(String subDescription) {
        this.subDescription = subDescription;
    }
    @Override
    public String toString() {
        return "Subscription [id=" + id + ", subName=" + subName + ", subBenefits=" + subBenefits + ", subDescription="
                + subDescription + "]";
    }
    
}
