package entities;
public class Role {
    private Integer id;
    private String nameRol;
    private boolean levelRol;
    
    public Role() {
    
    }
    public Role(Integer id, String nameRol, boolean levelRol) {
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
    public boolean getLevelRol() {
        return levelRol;
    }
    public void setLevelRol(boolean levelRol) {
        this.levelRol = levelRol;
    }
    @Override
    public String toString() {
        return "Role [id=" + id + ", nameRol=" + nameRol + ", levelRol=" + levelRol + "]";
    }
    
}
