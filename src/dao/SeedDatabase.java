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
        try (BufferedReader lector = new BufferedReader(new FileReader(rutaArchivoSQL));
             Statement sentencia = conexion.createStatement()) {

            String linea;
            StringBuilder sql = new StringBuilder();

            while ((linea = lector.readLine()) != null) {
                sql.append(linea);
                if (linea.endsWith(";")) {
                    sentencia.executeUpdate(sql.toString());
                    sql.setLength(0); // Limpiar el StringBuilder
                }
            }
        }
    }

    public static void inicializarSeed(Connection conexion, String rutaArchivoSQL) {
        try (Statement sentencia = conexion.createStatement()) {

            // Verificar si el seed ya se ejecutó
            ResultSet resultado = sentencia.executeQuery("SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'Taller' AND TABLE_NAME = 'seed_ejecutado'");

            resultado.next();
            int contador = resultado.getInt(1);

            if (contador == 0) {
                // El seed no se ha ejecutado, ejecutarlo
                ejecutarSeed(conexion, rutaArchivoSQL);

                // Crear la tabla para registrar que el seed se ejecutó
                sentencia.executeUpdate("CREATE TABLE seed_ejecutado (ejecutado INT)");
                sentencia.executeUpdate("INSERT INTO seed_ejecutado VALUES (1)");

                System.out.println("Datos de seed insertados con éxito.");
            } else {
                System.out.println("El seed ya se ha ejecutado.");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
    