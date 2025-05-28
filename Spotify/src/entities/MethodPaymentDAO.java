import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Database.Conexion;

public class MethodPaymentDAO {
    // ! CRUD

    // Metodo para obtener todos los métodos de pago
    public List<MethodPayment> getAllMethods() {
        List<MethodPayment> methods = new ArrayList<>();
        String sql = "SELECT * FROM metodopago";

        try {
            Connection conn = Conexion.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                MethodPayment method = new MethodPayment(rs.getInt("idMetodoPago"), rs.getString("tipo"),
                        rs.getString("detalles"), new Subscription());
                methods.add(method);

            }

        } catch (Exception e) {
            System.out.println("No se pudo obtener el metodo de pago" + e.getMessage());
        }
        return methods;
    }

    // Metodo para actualizar un método de pago
    public boolean updateMethod(MethodPayment method) {
        String sql = "UPDATE metodopago SET tipo =?, detalles =? WHERE idMetodoPago = ?";

        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, method.getType());
            ps.setString(2, method.getDetails());
            ps.setInt(3, method.getIdMethodPayment());
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("No se pudo actualizar el metodo de pago" + e.getMessage());
            return false;
        }
    }

    // Metodo para eliminar un método de pago por ID
    public boolean deletPaymentMethodbyID(int idMethodPayment) {
        String sql = "DELETE FROM metodopago WHERE idMetodoPago = ?";

        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idMethodPayment);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("No se pudo eliminar el metodo de pago" + e.getMessage());
            return false;
        }
    }

    // Metodo para obtener un metodo de pago por ID
    public MethodPayment getMethodByID(int idMethodPayment) {
        MethodPayment method = null;
        String sql = "SELECT * FROM metodopago WHERE idMetodoPago = ?";

        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idMethodPayment);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                method = new MethodPayment(rs.getInt("idMetodoPago"), rs.getString("tipo"),
                        rs.getString("detalles"), new Subscription());
            }

        } catch (Exception e) {
            System.out.println("No se pudo obtener el metodo de pago" + e.getMessage());
        }
        return method;
    }
}
