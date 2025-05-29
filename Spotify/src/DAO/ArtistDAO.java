package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Database.Conexion;
import entities.entities.*;

public class ArtistDAO {
    // ! CRUD

    //metodo para crear un artista
   public boolean insertArtist(Artist artist) {
    String sql = "INSERT INTO artistas (nombre, descripcion) VALUES (?, ?)";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, artist.getName());
        stmt.setString(2, artist.getDescription());

        return stmt.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}


    //metodo para obtener todos los artistas
    public List<Artist> getAllArtists(){
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM artistas";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Artist artist = new Artist(rs.getInt("idArtista"), rs.getString("nombreArtista"), rs.getString("descripcion"));
                artists.add(artist);
            }
        } catch (Exception e) {
            System.out.println("no se pudo obtener los artistas" + e.getMessage());
        }
        return artists;
    }

    //metodo para actualizar un artista
    public boolean updateArtist(Artist artist){
        String sql = "UPDATE artist SET nombre_artista = ?, descripcion_artista = ? WHERE idArtista = ?";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, artist.getName());
            pstmt.setString(2, artist.getDescription());
            pstmt.setInt(3, artist.getId());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("no se pudo actualizar el artista" + e.getMessage());
            return false;
        }
    }

    //metodo para eliminar un artista
    public boolean deletArtistbyID(Artist artist){
        String sql = "DELETE FROM artist WHERE idArtista = ?";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, artist.getId());
            pstmt.executeUpdate();
            return true;        } catch (Exception e) {
            System.out.println("no se pudo eliminar el artista" + e.getMessage());
        }
        return false;
    }

    //metodo para obtener un artista por id
    public Artist geArtistByID(int idArtist){
        Artist artist = null;
        String sql = "SELECT * FROM artistas WHERE idArtista = ?";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idArtist);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                artist = new Artist(rs.getInt("idArtist"), rs.getString("nombreArtista"), rs.getString("descripcion"));
            }
        } catch (Exception e) {
            System.out.println("no se pudo obtener el artista" + e.getMessage());
        }
        return artist;
    }
}
