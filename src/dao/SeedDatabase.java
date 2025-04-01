package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SeedDatabase {

    public static void ejecutarSeed(Connection conexion, String rutaArchivoSQL) throws SQLException, IOException {
        System.out.println("Intentando leer el archivo SQL desde: " + rutaArchivoSQL);
        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivoSQL));
             Statement sentencia = conexion.createStatement()) {
    
            String linea;
            StringBuilder sql = new StringBuilder();
    
            while ((linea = lector.readLine()) != null) {
                sql.append(linea).append(" ");
                if (linea.trim().endsWith(";")) {
                    String instruccionSQL = sql.toString().trim();
                    System.out.println("Ejecutando SQL: " + instruccionSQL);
                    try {
                        sentencia.execute(instruccionSQL);
                    } catch (SQLException e) {
                        System.err.println("Error ejecutando SQL: " + instruccionSQL);
                        e.printStackTrace();
                    }
                    sql.setLength(0); // Limpiar el StringBuilder para la siguiente instrucción
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo SQL: " + e.getMessage());
            throw e;
        }
    }

    public static void inicializarSeed(Connection conexion, String rutaArchivoSQL) {
        try (Statement sentencia = conexion.createStatement()) {

            // Verificar si el seed ya se ejecutó
            ResultSet resultado = sentencia.executeQuery("SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'Taller' AND TABLE_NAME = 'seed_ejecutado'");

            resultado.next();
            int contador = resultado.getInt(1);

            // if (contador == 0) {
                // El seed no se ha ejecutado, ejecutarlo
                ejecutarSeed(conexion, rutaArchivoSQL);

                // Crear la tabla para registrar que el seed se ejecutó
                sentencia.executeUpdate("CREATE TABLE seed_ejecutado (ejecutado INT)");
                sentencia.executeUpdate("INSERT INTO seed_ejecutado VALUES (1)");

                System.out.println("Datos de seed insertados con éxito.");
            // } else {
                //System.out.println("El seed ya se ha ejecutado.");
            // }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
    