package DAO;                   // ← pon tu paquete real

import entities.User;          // ← pon tu paquete real
import Database.Conexion;      // ← tu clase de conexión

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** DAO para la tabla «usuarios». */
public class UserDAO {

    /* ------------------------------------------------ insertar ------------------------------------------------ */
    private static final String INSERT =
            "INSERT INTO usuarios (nombre_usuario, correo, telefono, clave, id_rol, id_suscripcion) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    /** Guarda un usuario. Devuelve true si la inserción fue exitosa. */
    public boolean insert(User u) {
        try (Connection c = Conexion.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPhone());
            ps.setString(4, u.getPassword());      // pásalo hasheado si corresponde
            ps.setInt   (5, u.getRoleId());
            ps.setInt   (6, u.getSubscriptionId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ------------------------------------------------ listar -------------------------------------------------- */
    private static final String SELECT_ALL =
            "SELECT id_usuario, nombre_usuario, correo, telefono, id_rol, id_suscripcion " +
            "FROM usuarios ORDER BY id_usuario";

    public List<User> findAll() {
        List<User> lista = new ArrayList<>();
        try (Connection c = Conexion.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setId            (rs.getInt   ("id_usuario"));
                u.setUsername      (rs.getString("nombre_usuario"));
                u.setEmail         (rs.getString("correo"));
                u.setPhone         (rs.getString("telefono"));
                u.setRoleId        (rs.getInt   ("id_rol"));
                u.setSubscriptionId(rs.getInt   ("id_suscripcion"));
                lista.add(u);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    /* ------------------------------------------------ login --------------------------------------------------- */
    private static final String AUTH =
            "SELECT id_usuario, nombre_usuario, correo, telefono, id_rol, id_suscripcion " +
            "FROM usuarios WHERE correo = ? AND clave = ?";

    /** Devuelve el usuario si la combinación correo/clave es correcta, o null si falla. */
    public User authenticate(String correo, String clavePlain) {
        try (Connection c = Conexion.getConnection();
             PreparedStatement ps = c.prepareStatement(AUTH)) {

            ps.setString(1, correo);
            ps.setString(2, clavePlain);      // pon aquí el hash si almacenaste hash
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setId            (rs.getInt   ("id_usuario"));
                u.setUsername      (rs.getString("nombre_usuario"));
                u.setEmail         (rs.getString("correo"));
                u.setPhone         (rs.getString("telefono"));
                u.setRoleId        (rs.getInt   ("id_rol"));
                u.setSubscriptionId(rs.getInt   ("id_suscripcion"));
                return u;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    /* ---------------------------------------------- emailExists ----------------------------------------------- */
    /** Devuelve true si ya existe un usuario con ese correo. */
    public boolean emailExists(String correo) {
        String sql = "SELECT 1 FROM usuarios WHERE correo = ?";
        try (Connection c = Conexion.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, correo);
            return ps.executeQuery().next();

        } catch (SQLException e) {
            e.printStackTrace();
            return true;                      // «true» para impedir registro ante error
        }
    }
}
