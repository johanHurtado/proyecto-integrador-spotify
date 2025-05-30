package DAO;                       // ← pon tu paquete real

import Database.Conexion;          // ← tu clase de conexión
import entities.*;       // ← tu entidad

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistDAO {

    /* ------------------- INSERTAR ------------------- */
    public boolean insertArtist(Artist a) {
        String sql = "INSERT INTO artistas (nombre_artista, descripcion_artista) VALUES (?, ?)";
        try (Connection c = Conexion.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, a.getName());
            ps.setString(2, a.getDescription());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ------------------- LISTAR TODOS ------------------- */
    public List<Artist> getAllArtists() {
        List<Artist> list = new ArrayList<>();
        String sql = "SELECT id_artista, nombre_artista, descripcion_artista FROM artistas";
        try (Connection c = Conexion.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Artist a = new Artist(
                        rs.getInt   ("id_artista"),
                        rs.getString("nombre_artista"),
                        rs.getString("descripcion_artista"));
                list.add(a);
            }
        } catch (Exception e) {
            System.out.println("No se pudieron obtener los artistas: " + e.getMessage());
        }
        return list;
    }

    /* ------------------- BUSCAR POR ID ------------------- */
    public Artist findById(int id) {
        String sql = "SELECT id_artista, nombre_artista, descripcion_artista FROM artistas WHERE id_artista = ?";
        try (Connection c = Conexion.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Artist(
                            rs.getInt   ("id_artista"),
                            rs.getString("nombre_artista"),
                            rs.getString("descripcion_artista"));
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudo obtener el artista: " + e.getMessage());
        }
        return null;
    }

    /* ------------------- ACTUALIZAR ------------------- */
    public boolean updateArtist(Artist a) {
        String sql = "UPDATE artistas SET nombre_artista = ?, descripcion_artista = ? WHERE id_artista = ?";
        try (Connection c = Conexion.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, a.getName());
            ps.setString(2, a.getDescription());
            ps.setInt   (3, a.getId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("No se pudo actualizar el artista: " + e.getMessage());
            return false;
        }
    }

    /* ------------------- ELIMINAR ------------------- */
    public boolean deleteArtistById(int id) {
        String sql = "DELETE FROM artistas WHERE id_artista = ?";
        try (Connection c = Conexion.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("No se pudo eliminar el artista: " + e.getMessage());
            return false;
        }
    }
}
