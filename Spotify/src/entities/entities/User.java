package entities;


public class User {
    private int idUsuario;
    private String nombreUsuario;
    private String correo;
    private String telefono;
    private String claveHash;
    private int idRol;
    private int idSuscripcion;

    public User() { }

    public User(int idUsuario,
        String nombreUsuario,
        String correo,
        String telefono,
        String claveHash,
        int idRol,
        int idSuscripcion) {
        this.idUsuario      = idUsuario;
        this.nombreUsuario  = nombreUsuario;
        this.correo         = correo;
        this.telefono       = telefono;
        this.claveHash      = claveHash;
        this.idRol          = idRol;
        this.idSuscripcion  = idSuscripcion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getClaveHash() {
        return claveHash;
    }
    public void setClaveHash(String claveHash) {
        this.claveHash = claveHash;
    }

    public int getIdRol() {
        return idRol;
    }
    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

   
    public int getIdSuscripcion() {
        return idSuscripcion;
    }
    public void setIdSuscripcion(int idSuscripcion) {
        this.idSuscripcion = idSuscripcion;
    }

    @Override
    public String toString() {
        return "User{" +
               "idUsuario=" + idUsuario +
               ", nombreUsuario='" + nombreUsuario + '\'' +
               ", correo='" + correo + '\'' +
               ", telefono='" + telefono + '\'' +
               ", idRol=" + idRol +
               ", idSuscripcion=" + idSuscripcion +
               '}';
    }
}
