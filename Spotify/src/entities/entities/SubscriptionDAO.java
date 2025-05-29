package entities;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Database.Conexion;

public class SubscriptionDAO {

    /** Inserta una nueva suscripción (el id se genera automáticamente). */
    public boolean addSubscription(Subscription sub) {
        String sql = """
            INSERT INTO suscripciones
              (nombre_suscripcion, beneficios_suscripcion, descripcion_suscripcion)
            VALUES (?,?,?)
        """;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, sub.getNombreSuscripcion());
            stmt.setString(2, sub.getBeneficiosSuscripcion());
            stmt.setString(3, sub.getDescripcionSuscripcion());

            int filas = stmt.executeUpdate();
            if (filas == 0) return false;

            // obtenemos el id generado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    sub.setIdSuscripcion(rs.getInt(1));
                }
            }
            return true;

        } catch (SQLException e) {
            System.err.println("No se pudo agregar la suscripción: " + e.getMessage());
            return false;
        }
    }

    /** Recupera todas las suscripciones de la tabla. */
    public List<Subscription> getAllSubscriptions() {
        List<Subscription> subs = new ArrayList<>();
        String sql = """
            SELECT
              id_suscripcion,
              nombre_suscripcion,
              beneficios_suscripcion,
              descripcion_suscripcion
            FROM suscripciones
        """;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                subs.add(new Subscription(
                    rs.getInt("id_suscripcion"),
                    rs.getString("nombre_suscripcion"),
                    rs.getString("beneficios_suscripcion"),
                    rs.getString("descripcion_suscripcion")
                ));
            }

        } catch (SQLException e) {
            System.err.println("No se pudieron obtener las suscripciones: " + e.getMessage());
        }
        return subs;
    }

    /** Actualiza el registro de una suscripción existente. */
    public boolean updateSubscription(Subscription sub) {
        String sql = """
            UPDATE suscripciones
               SET nombre_suscripcion     = ?,
                   beneficios_suscripcion = ?,
                   descripcion_suscripcion = ?
             WHERE id_suscripcion         = ?
        """;
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sub.getNombreSuscripcion());
            stmt.setString(2, sub.getBeneficiosSuscripcion());
            stmt.setString(3, sub.getDescripcionSuscripcion());
            stmt.setInt   (4, sub.getIdSuscripcion());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("No se pudo actualizar la suscripción: " + e.getMessage());
            return false;
        }
    }

    /** Elimina la suscripción con el id dado. */
    public boolean deleteSubscription(int idSuscripcion) {
        String sql = "DELETE FROM suscripciones WHERE id_suscripcion = ?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSuscripcion);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("No se pudo eliminar la suscripción: " + e.getMessage());
            return false;
        }
    }

    /** Recupera una suscripción por su id, o devuelve null si no existe. */
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
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSuscripcion);
            try (ResultSet rs = stmt.executeQuery()) {
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
            System.err.println("No se pudo obtener la suscripción: " + e.getMessage());
        }
        return null;
    }
}
