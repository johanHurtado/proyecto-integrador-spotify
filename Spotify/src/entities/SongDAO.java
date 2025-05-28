import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Database.Conexion;

public class SongDAO {
    // ! CRUD

    //metodo para convertir de imagen a byte
    public byte[] imageToByteArray(File file) {
    try {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int readNum;
        while ((readNum = fis.read(buf)) != -1) {
            bos.write(buf, 0, readNum);
        }
        fis.close();
        return bos.toByteArray();
    } catch (IOException ex) {
        ex.printStackTrace();
    }
    return null;
}

    // Metodo para obtener todas las canciones
    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM canciones";
        try {
            Connection conn = Conexion.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Song song = new Song();
                songs.add(song);
            }

        } catch (Exception e) {
            System.out.println("No se pudo obtener las canciones: " + e.getMessage());
        }
        return songs;
    }

    // Metodo para agregar una canción
    public boolean addSong(Song song) {
        String sql = "INSERT INTO canciones (titulo, descripcion, duracion, idartista, idgenero) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, song.getTitle());
            ps.setString(2, song.getDescription());
            ps.setDouble(3, song.getTimeLength());
            ps.setInt(4, song.getArtist().getId());
            ps.setInt(5, song.getGender().getIdGender());
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("No se pudo agregar la canción: " + e.getMessage());
            return false;
        }
    }

    // Metodo para actualizar una canción
    public boolean updateSong(Song song) {
        String sql = "UPDATE canciones SET titulo = ?, descripcion = ?, duracion = ?, idartista = ?, idgenero = ? WHERE idcancion = ?";

        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, song.getTitle());
            ps.setString(2, song.getDescription());
            ps.setDouble(3, song.getTimeLength());
            ps.setInt(4, song.getArtist().getId());
            ps.setInt(5, song.getGender().getIdGender());
            ps.setInt(6, song.getIdSong());
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("No se pudo actualizar la canción: " + e.getMessage());
            return false;
        }
    }

    // Metodo para eliminar una canción por ID
    public boolean deleteSongById(int idSong) {
        String sql = "DELETE FROM canciones WHERE idcancion = ?";

        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idSong);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("No se pudo eliminar la canción: " + e.getMessage());
            return false;
        }
    }

    // Metodo para obtener una canción por ID
    public Song getSongById(int idSong) {
        Song song = null;
        String sql = "SELECT * FROM canciones WHERE idcancion = ?";

        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idSong);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                song = new Song();
            }
        } catch (Exception e) {
            System.out.println("No se pudo obtener la canción: " + e.getMessage());
        }
        return song;
    }
}
