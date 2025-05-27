public class Role {
    private Integer id;
    private String nameRol;
    private Integer levelRol;
    public Role() {
    
    }
    public Role(Integer id, String nameRol, Integer levelRol) {
        this.id = id;
        this.nameRol = nameRol;
        this.levelRol = levelRol;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNameRol() {
        return nameRol;
    }
    public void setNameRol(String nameRol) {
        this.nameRol = nameRol;
    }
    public Integer getLevelRol() {
        return levelRol;
    }
    public void setLevelRol(Integer levelRol) {
        this.levelRol = levelRol;
    }
    @Override
    public String toString() {
        return "Role [id=" + id + ", nameRol=" + nameRol + ", levelRol=" + levelRol + "]";
    }
    
}
