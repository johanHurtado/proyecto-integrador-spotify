package DAO; // ← cámbialo si usas otro paquete

import entities.*; // ← paquete de la entidad
import Database.Conexion; // ← tu clase de conexión

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para la tabla "canciones".
 * Incluye: insertar, actualizar, eliminar, buscar uno, listar todos.
 */
public class SongDAO {

    /* ---------------- SQL ---------------- */
    private static final String INSERT = "INSERT INTO canciones " +
            "(titulo, descripcion, duracion, id_artista, id_genero, portada, archivo_mp3) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE = "UPDATE canciones SET " +
            "titulo=?, descripcion=?, duracion=?, id_artista=?, id_genero=?, portada=?, archivo_mp3=? " +
            "WHERE id=?";

    private static final String DELETE = "DELETE FROM canciones WHERE id=?";

    private static final String SELECT_BY_ID = "SELECT * FROM canciones WHERE id=?";

    private static final String SELECT_ALL = "SELECT id, titulo, descripcion, duracion, id_artista, id_genero " +
            "FROM canciones ORDER BY id";

    /* -------------- Insertar -------------- */
    public void insert(Song s) throws SQLException {
        try (Connection c = Conexion.getConnection();
                PreparedStatement ps = c.prepareStatement(INSERT)) {

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

    /* -------------- Actualizar -------------- */
    public void update(Song s) throws SQLException {
        try (Connection c = Conexion.getConnection();
                PreparedStatement ps = c.prepareStatement(UPDATE)) {

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

    /* -------------- Eliminar -------------- */
    public void delete(int id) throws SQLException {
        try (Connection c = Conexion.getConnection();
                PreparedStatement ps = c.prepareStatement(DELETE)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /* -------------- Buscar uno -------------- */
    public Song findById(int id) throws SQLException {
        try (Connection c = Conexion.getConnection();
                PreparedStatement ps = c.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return map(rs);
            }
        }
        return null; // no encontrado
    }

    /* -------------- Listar todos -------------- */
    public List<Song> findAll() throws SQLException {
        List<Song> list = new ArrayList<>();
        try (Connection c = Conexion.getConnection();
                PreparedStatement ps = c.prepareStatement(SELECT_ALL);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    /* -------------- Utilidad de mapeo -------------- */
    private Song map(ResultSet rs) throws SQLException {
        Song s = new Song();
        s.setId(rs.getInt("id"));
        s.setTitle(rs.getString("titulo"));
        s.setDescription(rs.getString("descripcion"));
        s.setDuration(rs.getDouble("duracion"));
        s.setArtistId(rs.getInt("id_artista"));
        s.setGenreId(rs.getInt("id_genero"));
        // Para ahorrar memoria no traemos portada ni MP3 en findAll().
        // Si los necesitas, usa SELECT_BY_ID o añade campos aquí.
        return s;
    }
}
