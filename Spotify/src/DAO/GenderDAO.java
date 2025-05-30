package DAO;                       // ← ajusta a tu paquete real

import Database.Conexion;          // ← tu clase de conexión
import entities.*;            // ← tu entidad (puede ser Genre)

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla "generos".
 */
public class GenderDAO {

    /* ======================  INSERTAR  ====================== */
    public boolean insertGender(Gender g) {
        String sql = "INSERT INTO generos (nombre_genero, descripcion_genero) VALUES (?, ?)";
        try (Connection c = Conexion.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, g.getNameGender());
            ps.setString(2, g.getDescription());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("No se pudo insertar el género: " + e.getMessage());
            return false;
        }
    }

    /* ======================  LISTAR TODOS  ====================== */
    public List<Gender> getAllGenders() {
        List<Gender> list = new ArrayList<>();
        String sql = "SELECT id_genero, nombre_genero, descripcion_genero FROM generos";
        try (Connection c = Conexion.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Gender g = new Gender(
                        rs.getInt   ("id_genero"),
                        rs.getString("nombre_genero"),
                        rs.getString("descripcion_genero"));
                list.add(g);
            }
        } catch (Exception e) {
            System.out.println("No se pudieron obtener los géneros: " + e.getMessage());
        }
        return list;
    }

    /* ======================  BUSCAR POR ID  ====================== */
    public Gender findById(int id) {
        String sql = "SELECT id_genero, nombre_genero, descripcion_genero FROM generos WHERE id_genero = ?";
        try (Connection c = Conexion.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Gender(
                            rs.getInt   ("id_genero"),
                            rs.getString("nombre_genero"),
                            rs.getString("descripcion_genero"));
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudo obtener el género: " + e.getMessage());
        }
        return null;
    }

    /* ======================  ACTUALIZAR  ====================== */
    public boolean updateGender(Gender g) {
        String sql = "UPDATE generos SET nombre_genero = ?, descripcion_genero = ? WHERE id_genero = ?";
        try (Connection c = Conexion.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, g.getNameGender());
            ps.setString(2, g.getDescription());
            ps.setInt   (3, g.getIdGender());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("No se pudo actualizar el género: " + e.getMessage());
            return false;
        }
    }

    /* ======================  ELIMINAR  ====================== */
    public boolean deleteGenderById(int id) {
        String sql = "DELETE FROM generos WHERE id_genero = ?";
        try (Connection c = Conexion.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("No se pudo eliminar el género: " + e.getMessage());
            return false;
        }
    }
}
