package DAO;

import java.sql.*;
import Database.Conexion;
import entities.User;

/**
 * DAO para la tabla 'usuarios'.
 */
public class UserDAO {

    /**
     * Busca un usuario por su correo. Devuelve null si no existe.
     */
    public User findByEmail(String correo) {
        String sql = "SELECT id_usuario, nombre_usuario, correo, telefono, clave, id_rol, id_suscripcion "
                   + "FROM usuarios WHERE correo = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("clave"),
                        rs.getInt("id_rol"),
                        rs.getInt("id_suscripcion")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por email: " + e.getMessage());
        }
        return null;
    }

    /**
     * Inserta un nuevo usuario. El ID se genera automáticamente y se asigna al objeto.
     * Devuelve true si la inserción fue exitosa.
     */
    public boolean insert(User user) {
        String sql = "INSERT INTO usuarios "
                   + "(nombre_usuario, correo, telefono, clave, id_rol, id_suscripcion) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getNombreUsuario());
            ps.setString(2, user.getCorreo());
            ps.setString(3, user.getTelefono());
            ps.setString(4, user.getClaveHash());
            ps.setInt(5, user.getIdRol());
            ps.setInt(6, user.getIdSuscripcion());

            int affected = ps.executeUpdate();
            if (affected == 0) return false;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setIdUsuario(keys.getInt(1));
                }
            }
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza la contraseña (campo 'clave') de un usuario dado su ID.
     */
    public boolean updatePassword(int idUsuario, String nuevaClaveHash) {
        String sql = "UPDATE usuarios SET clave = ? WHERE id_usuario = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevaClaveHash);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza nombre y teléfono de perfil de usuario.
     */
    public boolean updateProfile(User user) {
        String sql = "UPDATE usuarios SET nombre_usuario = ?, telefono = ? WHERE id_usuario = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getNombreUsuario());
            ps.setString(2, user.getTelefono());
            ps.setInt(3, user.getIdUsuario());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar perfil: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cambia la suscripción del usuario.
     */
    public boolean updateSubscription(int idUsuario, int idSuscripcion) {
        String sql = "UPDATE usuarios SET id_suscripcion = ? WHERE id_usuario = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSuscripcion);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar suscripción: " + e.getMessage());
            return false;
        }
    }
}
