package dao;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
 
public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/Taller"; // Cambia "mysql" por "mariadb" si es necesario
    private static final String USUARIO = "juanjose"; // Cambia por tu usuario de MariaDB
    private static final String CONTRASEÑA = "1234"; // Cambia por tu contraseña de MariaDB

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
            return null;
        }
    }
}