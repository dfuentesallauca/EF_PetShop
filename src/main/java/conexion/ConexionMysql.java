package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMysql {

    private static final String url    = "jdbc:mysql://localhost:3306/db_petshop?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String usuario = "root";
    private static final String clave   = "mySql123#";
    private static final String driver  = "com.mysql.cj.jdbc.Driver";

    public static Connection getConexion() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(url, usuario, clave);
    }
}
