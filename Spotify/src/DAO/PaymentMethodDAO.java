package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Database.Conexion;
import entities.PaymentMethod;

/**
 * DAO para la tabla 'metodo_pago'.
 */
public class PaymentMethodDAO {

    /**
     * Inserta un nuevo registro de método de pago para un usuario.
     * El ID se genera automáticamente y se asigna al objeto.
     */
    public boolean addPayment(PaymentMethod pm) {
        String sql = """
                    INSERT INTO metodo_pago
                      (id_usuario, numero_tarjeta, fecha_vencimiento, cvc)
                    VALUES (?,?,?,?)
                """;
        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, pm.getIdUsuario());
            ps.setString(2, pm.getNumeroTarjeta());
            ps.setString(3, pm.getFechaVencimiento());
            ps.setString(4, pm.getCvc());

            int affected = ps.executeUpdate();
            if (affected == 0)
                return false;

            // Recuperar el ID generado
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    pm.setIdPago(keys.getInt(1));
                }
            }
            return true;

        } catch (SQLException e) {
            System.err.println("Error al agregar método de pago: " + e.getMessage());
            return false;
        }
    }

    /**
     * Devuelve todos los métodos de pago de un usuario.
     */
    public List<PaymentMethod> getPaymentsByUser(int idUsuario) {
        List<PaymentMethod> list = new ArrayList<>();
        String sql = """
                    SELECT
                      id_pago,
                      id_usuario,
                      numero_tarjeta,
                      fecha_vencimiento,
                      cvc
                    FROM metodo_pago
                    WHERE id_usuario = ?
                """;
        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PaymentMethod pm = new PaymentMethod(
                            rs.getInt("id_pago"),
                            rs.getInt("id_usuario"),
                            rs.getString("numero_tarjeta"),
                            rs.getString("fecha_vencimiento"),
                            rs.getString("cvc"));
                    list.add(pm);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener métodos de pago: " + e.getMessage());
        }
        return list;
    }

    /**
     * Elimina un método de pago por su ID.
     */
    public boolean deletePayment(int idPago) {
        String sql = "DELETE FROM metodo_pago WHERE id_pago = ?";
        try (Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPago);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar método de pago: " + e.getMessage());
            return false;
        }
    }
}
