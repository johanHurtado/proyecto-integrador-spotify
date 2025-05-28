package entities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Database.Conexion;

public class GenderDAO {
    //! CRUD

    //metodo para obtener todos los generos 
    public List<Gender> getAllGenders() {
        List<Gender> genders = new ArrayList<>();
        String sql = "SELECT * FROM generos";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                Gender gender = new Gender(rs.getInt("idGenero"), rs.getString("nombreGenero"), rs.getString("descripcion"));
                genders.add(gender);
            }
        } catch (Exception e) {
            System.out.println("no se pudo obtener los generos" + e.getMessage());
        }
        return genders;
    }
    //metodo para actualizar los generos
    public boolean updateGender(Gender gender){
        String sql = "UPDATE genero SET nombreGenero = ?, descripcion = ? WHERE idGenero = ?";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gender.getNameGender());
            pstmt.setString(2, gender.getDescription());
            pstmt.setInt(3, gender.getIdGender());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("no se pudo actualizar la lista de generos" + e.getMessage());
            return false;
        }
    }
    //metodo para eliminar un genero
    public boolean deletGenderbyID(int idGender){
        String sql = "DELETE FROM genero WHERE idGenero = ?";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idGender);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("No se pudo eliminar el genero" + e.getMessage());
            return false;
        }
    }
    //metodo para obtener un genero por id
    public Gender getGenderByID(int idGender){
        Gender gender = null;
        String sql = "SELECT * FROM genero WHERE idGenero = ?";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idGender);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                gender = new Gender(rs.getInt("idGender"), rs.getString("nombreGenero"), rs.getString("descripcion"));
            }
        } catch (Exception e) {
            System.out.println("no se pudo obtener el genero" + e.getMessage());
        }
        return gender;
    }
    
}
