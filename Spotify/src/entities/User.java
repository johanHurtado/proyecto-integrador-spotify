package entities;   // o tu paquete real

public class User {

    /* ---------- Campos (añade si no existen) ---------- */
    private int    id;
    private String username;
    private String email;
    private String phone;
    private String password;
    private int    roleId;
    private int    subscriptionId;

         
    public User() {
    }
    
    public User(int id, String username, String email, String phone, String password, int roleId, int subscriptionId) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.roleId = roleId;
        this.subscriptionId = subscriptionId;
    }

    /* ---------- Getters ---------- */
    public int    getId()             { return id; }
    public String getUsername()       { return username; }
    public String getEmail()          { return email; }
    public String getPhone()          { return phone; }
    public String getPassword()       { return password; }
    public int    getRoleId()         { return roleId; }
    public int    getSubscriptionId() { return subscriptionId; }



    /* ---------- Setters ---------- */
    public void setId(int id)                     { this.id = id; }
    public void setUsername(String username)      { this.username = username; }
    public void setEmail(String email)            { this.email = email; }
    public void setPhone(String phone)            { this.phone = phone; }
    public void setPassword(String password)      { this.password = password; }
    public void setRoleId(int roleId)             { this.roleId = roleId; }
    public void setSubscriptionId(int subId)      { this.subscriptionId = subId; }

    /* (Si ya tienes otros nombres en español,
       mantener ambos juegos de métodos no daña nada.) */
}
