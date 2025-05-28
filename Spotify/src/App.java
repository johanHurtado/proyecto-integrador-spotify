import Database.Conexion;
import forms.FrameLogin;

public class App {

    public static void main(String[] args) throws Exception {
        Conexion conexiondb = new Conexion();
        conexiondb.getConnection();

        new FrameLogin();


        
    }
}
