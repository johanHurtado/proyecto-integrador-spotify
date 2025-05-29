package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Database.Conexion;
import entities.Subscription;

/**
 * DAO para la tabla 'suscripciones'.
 */
public class SubscriptionDAO {

    /**
     * Inserta una nueva suscripción.
     * El ID se genera automáticamente y se setea en el objeto.
     */
    public boolean addSubscription(Subscription sub) {
        String sql = """
            INSERT INTO suscripciones
              (nombre_suscripcion, beneficios_suscripcion, descripcion_suscripcion)
            VALUES (?,?,?)
        """;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, sub.getNombreSuscripcion());
            ps.setString(2, sub.getBeneficiosSuscripcion());
            ps.setString(3, sub.getDescripcionSuscripcion());

            int affected = ps.executeUpdate();
            if (affected == 0) return false;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    sub.setIdSuscripcion(keys.getInt(1));
                }
            }
            return true;

        } catch (SQLException e) {
            System.err.println("Error al agregar suscripción: " + e.getMessage());
            return false;
        }
    }

    /**
     * Devuelve todas las suscripciones disponibles.
     */
    public List<Subscription> getAllSubscriptions() {
        List<Subscription> list = new ArrayList<>();
        String sql = """
            SELECT
              id_suscripcion,
              nombre_suscripcion,
              beneficios_suscripcion,
              descripcion_suscripcion
            FROM suscripciones
        """;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Subscription s = new Subscription(
                    rs.getInt("id_suscripcion"),
                    rs.getString("nombre_suscripcion"),
                    rs.getString("beneficios_suscripcion"),
                    rs.getString("descripcion_suscripcion")
                );
                list.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener suscripciones: " + e.getMessage());
        }
        return list;
    }

    /**
     * Actualiza una suscripción existente.
     */
    public boolean updateSubscription(Subscription sub) {
        String sql = """
            UPDATE suscripciones
               SET nombre_suscripcion       = ?,
                   beneficios_suscripcion   = ?,
                   descripcion_suscripcion  = ?
             WHERE id_suscripcion           = ?
        """;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sub.getNombreSuscripcion());
            ps.setString(2, sub.getBeneficiosSuscripcion());
            ps.setString(3, sub.getDescripcionSuscripcion());
            ps.setInt(4, sub.getIdSuscripcion());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar suscripción: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina la suscripción con el ID dado.
     */
    public boolean deleteSubscription(int idSuscripcion) {
        String sql = "DELETE FROM suscripciones WHERE id_suscripcion = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSuscripcion);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar suscripción: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca y devuelve la suscripción por su ID, o null si no existe.
     */
    public Subscription getSubscriptionById(int idSuscripcion) {
        String sql = """
            SELECT
              id_suscripcion,
              nombre_suscripcion,
              beneficios_suscripcion,
              descripcion_suscripcion
            FROM suscripciones
            WHERE id_suscripcion = ?
        """;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSuscripcion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Subscription(
                        rs.getInt("id_suscripcion"),
                        rs.getString("nombre_suscripcion"),
                        rs.getString("beneficios_suscripcion"),
                        rs.getString("descripcion_suscripcion")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener suscripción por ID: " + e.getMessage());
        }
        return null;
    }
}
