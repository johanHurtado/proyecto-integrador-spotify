import Database.Conexion;

public class App {

    public static void main(String[] args) throws Exception {
        Conexion conexiondb = new Conexion();
        conexiondb.getConnection();

        new FrameLogin();

    }
}
