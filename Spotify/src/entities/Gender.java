package entities;
public class Gender {
    private Integer idGender;
    private String nameGender;
    private String description;

    public Gender() {
    
    }

    public Gender(Integer idGender, String nameGender, String description) {
        this.idGender = idGender;
        this.nameGender = nameGender;
        this.description = description;
    }

    public Integer getIdGender() {
        return idGender;
    }

    public void setIdGender(Integer idGender) {
        this.idGender = idGender;
    }

    public String getNameGender() {
        return nameGender;
    }

    public void setNameGender(String nameGender) {
        this.nameGender = nameGender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Gender [idGender=" + idGender + ", nameGender=" + nameGender + ", description=" + description + "]";
    }

    

}
