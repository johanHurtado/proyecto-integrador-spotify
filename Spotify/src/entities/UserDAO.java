import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Database.Conexion;

public class UserDAO {
    // ! CRUD

    // Metodo para obtener todos los usuarios
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM usuario";

        try {
            Connection conn = Conexion.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                User user = new User(rs.getInt("idUsuario"), rs.getString("nombre_usuario"), rs.getString("correo"),
                        rs.getString("telefono"), rs.getInt("clave"), new Role(), new Subscription());
                users.add(user);
            }

        } catch (Exception e) {
            System.out.println("No se pudo obtener el usuario: " + e.getMessage());
        }
        return users;
    }

    // Metodo para agregar un usuario
    public boolean addUser(User user) {
        String sql = "INSERT INTO usuario (nombre_usuario, correo, telefono, clave, idRol, idSuscripcion) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setInt(3, user.getPhoneNumber());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getRoleId());
            ps.setInt(6, user.getSubscriptionId());
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("No se pudo agregar el usuario: " + e.getMessage());
            return false;
        }
    }

}
