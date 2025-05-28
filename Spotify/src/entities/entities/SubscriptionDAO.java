package entities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Database.Conexion;

public class SubscriptionDAO {
    // !CRUD

    //metodo para agregar una nueva suscripcion
    public boolean addSubscription(Subscription sub){
        String sql = "INSERT INTO subscriptions (id_suscripcion, nombre_suscripcion, beneficios_suscripcion, descripcion_suscripcion) VALUES (?,?,?,?)";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sub.getId());
            pstmt.setString(2, sub.getSubName());
            pstmt.setString(3, sub.getSubBenefits());
            pstmt.setString(4, sub.getSubDescription());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("No se pudo agregar la suscripcion" + e.getMessage());
            return false;
        }
    }

    //metodo para obtener todas las suscripciones
    public List<Subscription> getAllSubscriptions(){
        List<Subscription> subs = new ArrayList<>();
        String sql = "SELECT * FROM suscripciones";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Subscription sub = new Subscription(rs.getInt("idSuscripcion"), rs.getString("nombreSuscripcion"), rs.getString("beneficionSuscripcion"), rs.getString("descripcionSuscripcion"));
                subs.add(sub);
            }
        } catch (Exception e) {
            System.out.println("no se pudo obtener las suscripciones" + e.getMessage());
        }
        return subs;
    }

    //metodo para actualizar una suscripcion
    public boolean updateSubscription(Subscription sub){
        String sql = "UPDATE suscripciones SET nombre_suscripcion = ?, beneficios_suscripcion =?, descripcion_suscripcion = ? WHERE id_suscripcion = ?";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sub.getSubName());
            pstmt.setString(2, sub.getSubBenefits());
            pstmt.setString(3, sub.getSubDescription());
            pstmt.setInt(4, sub.getId());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("no se pudo actualizar la suscripcion" + e.getMessage());
            return false;
        }
    }

    //metodo para eliminar una suscripcion
    public boolean deleteSubscription(Subscription sub){
        String sql = "DELETE FROM suscripciones WHERE id_suscripcion = ?";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(sub.getId()));
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("no se pudo eliminar la suscripcion" + e.getMessage());
        }
        return false;
    }

    //metodo para obtener una suscripcion
    public Subscription getSubscriptionById(int id){
        Subscription sub = null;
        String sql = "SELECT * FROM suscripciones WHERE id_suscripcion = ?";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                sub = new Subscription(rs.getInt("idSuscripcion"), rs.getString("nombreSuscripcion"), rs.getString("beneficionSuscripcion"), rs.getString("descripcionSuscripcion"));
            }
        } catch (Exception e) {
            System.out.println("no se pudo obtener la suscripcion" + e.getMessage());
        }
        return sub;
    }
}
