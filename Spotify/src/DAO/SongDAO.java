package DAO;

import entities.entities.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Database.Conexion;

/**
 * Data-access object for table “canciones”.
 */
public class SongDAO {

    private static final String INSERT_SQL = "INSERT INTO canciones (titulo, descripcion, duracion, id_artista, id_genero, portada, archivo_mp3) "
            +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL = "UPDATE canciones SET titulo=?, descripcion=?, duracion=?, id_artista=?, id_genero=?, portada=?, archivo_mp3=? "
            +
            "WHERE id=?";

    private static final String DELETE_SQL = "DELETE FROM canciones WHERE id=?";

    private static final String SELECT_ALL = "SELECT id, titulo, descripcion, duracion, id_artista, id_genero FROM canciones";

    public void insert(Song s) throws SQLException {
        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            ps.setString(1, s.getTitle());
            ps.setString(2, s.getDescription());
            ps.setDouble(3, s.getDuration());
            ps.setInt(4, s.getArtistId());
            ps.setInt(5, s.getGenreId());
            ps.setBytes(6, s.getCoverArt());
            ps.setBytes(7, s.getMp3Bytes());
            ps.executeUpdate();
        }
    }

    public void update(Song s) throws SQLException {
        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, s.getTitle());
            ps.setString(2, s.getDescription());
            ps.setDouble(3, s.getDuration());
            ps.setInt(4, s.getArtistId());
            ps.setInt(5, s.getGenreId());
            ps.setBytes(6, s.getCoverArt());
            ps.setBytes(7, s.getMp3Bytes());
            ps.setInt(8, s.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Song> findAll() throws SQLException {
        List<Song> list = new ArrayList<>();
        try (Connection con = Conexion.getConnection();
                PreparedStatement ps = con.prepareStatement(SELECT_ALL);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Song s = new Song();
                s.setId(rs.getInt("id"));
                s.setTitle(rs.getString("titulo"));
                s.setDescription(rs.getString("descripcion"));
                s.setDuration(rs.getDouble("duracion"));
                s.setArtistId(rs.getInt("id_artista"));
                s.setGenreId(rs.getInt("id_genero"));
                list.add(s);
            }
        }
        return list;
    }
}
