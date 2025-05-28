import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Database.Conexion;
import entities.Role;
import entities.Subscription;
import entities.User;

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
        String sql = "INSERT INTO usuarios (nombre_usuario, correo, telefono, clave, id_rol, id_Suscripcion) VALUES (?, ?, ?, ?, ?, ?)";

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

    //Metodo para actualizar un usuario
    public boolean updateUser(User user) {
        String sql = "UPDATE usuario SET nombre_usuario = ?, correo = ?, telefono = ?, clave = ?, id_rol = ?, id_Suscripcion = ? WHERE idUsuario = ?";

        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setInt(3, user.getPhoneNumber());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getRoleId());
            ps.setInt(6, user.getSubscriptionId());
            ps.setInt(7, user.getId());
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("No se pudo actualizar el usuario: " + e.getMessage());
            return false;
        }
    }

    // Metodo para eliminar un usuario por ID
    public boolean deleteUserById(int id) {
        String sql = "DELETE FROM usuario WHERE idUsuario = ?";

        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("No se pudo eliminar el usuario: " + e.getMessage());
            return false;
        }
    }

    // Metodo para obtener un usuario por ID
    public User getUserById(int id) {
        User user = null;
        String sql = "SELECT * FROM usuario WHERE idUsuario = ?";

        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User(rs.getInt("idUsuario"), rs.getString("nombre_usuario"), rs.getString("correo"),
                        rs.getString("telefono"), rs.getInt("clave"), new Role(), new Subscription());
            }

        } catch (Exception e) {
            System.out.println("No se pudo obtener el usuario: " + e.getMessage());
        }
        return user;
    }

}
