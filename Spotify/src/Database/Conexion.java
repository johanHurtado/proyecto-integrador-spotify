package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
     private static final String URL = "jdbc:mysql://localhost/spotify";
    private static final String USER = "user_poo";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos");
        } catch (SQLException e) {
            System.err.println("Error en la conexión: " + e.getMessage());
        }
        return connection;
    }
}


