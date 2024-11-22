package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Establece la conexión con la base de datos.
 * @author Samuel Vega
 */
public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/escuela_padres?autoReconnect=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection conexion = null;
    
    /**
     * Crea la conexión en caso de que no haya, de lo contrario devuelve la conexión actual.
     * @return Conexión con la base de datos.
     */
    public static Connection getConexion() {
        if(conexion == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            }catch(ClassNotFoundException | SQLException e) {
                e.getStackTrace();
            }
        }
        return conexion;
    }
    
    /**
     * Cierra la conexión si está abieta
     */
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
