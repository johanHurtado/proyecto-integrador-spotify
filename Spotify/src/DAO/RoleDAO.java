package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Database.Conexion;
import entities.Role;

public class RoleDAO {
    // ! CRUD

    //metodo para crear un roll
    public boolean addRole(Role role){
        String sql = "INSERT INTO roles (nombre_rol, estado_rol) VALUES (?, ?, ?)";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role.getNameRol());
            pstmt.setBoolean(2, role.getLevelRol());
            pstmt.setInt(3, role.getId());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("No se pudo agregar el rol" + e.getMessage());
            return false;
        }
    }

    //metodo para obtener todos los roles
    public List<Role> getAllRoles(){
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM roles";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Role role = new Role(rs.getInt("idRol"), rs.getString("nombreRol"), rs.getBoolean("estadoRol"));
                roles.add(role);
            }
        } catch (Exception e) {
            System.out.println("no se pudo obtener los roles" + e.getMessage());
        }
        return roles;
    }

    //metodo para actualizar un rol por id
    public boolean updateRole(Role role){
        String sql = "UPDATE roles SET nombre_rol = ?, estado_rol = ? WHERE idRol = ?";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role.getNameRol());
            pstmt.setBoolean(2, role.getLevelRol());
            pstmt.setInt(3, role.getId());
            pstmt.executeUpdate();
            return true;    
        } catch (Exception e) {
            System.out.println("no se pudo actualizar el rol" + e.getMessage());
            return false;
        }
    }

    //metodo para eliminar un rol por id
    public boolean deleteRole(Role role){
        String sql = "DELETE FROM roles WHERE idRol = ?";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, role.getId());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("no se pudo eliminar el rol" + e.getMessage());
        }
        return false;
    }

    //metodo para obtener un rol por id
    public Role getRoleById(int id){
        Role role = null;
        String sql = "SELECT * FROM roles WHERE idRol = ?";
        try {
            Connection conn = Conexion.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                role = new Role(rs.getInt("idRol"), rs.getString("nombreRol"), rs.getBoolean("estadoRol"));
            }
        } catch (Exception e) {
            System.out.println("no se pudo obtener el rol" + e.getMessage());
        }
        return role;
    }
}
