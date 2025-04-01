package view;

import java.sql.*;
import java.util.Scanner;

import dao.ConexionDB;

public class Taller {

    Scanner sc = new Scanner(System.in);
    int opcion;

    public void mostrarInventario() {
        do {
            System.out.println("1. Herramientas");
            System.out.println("2. Piezas");
            System.out.println("3. Todo");
            System.out.println("4. Salir");
            opcion = sc.nextInt();

            try (Connection conexion = ConexionDB.conectar();
                 PreparedStatement sentencia = conexion.prepareStatement(obtenerConsultaInventario(opcion));
                 ResultSet resultados = sentencia.executeQuery()) {

                while (resultados.next()) {
                    System.out.println("ID: " + resultados.getInt("ID"));
                    System.out.println("Nombre: " + resultados.getString("Nombre"));
                    System.out.println("Categoría: " + resultados.getInt("Categoria"));
                    System.out.println("Precio: " + resultados.getDouble("Precio"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } while (opcion != 4);
    }

    private String obtenerConsultaInventario(int opcion) {
        switch (opcion) {
            case 1:
                return "SELECT * FROM Inventario WHERE Categoria = 1";
            case 2:
                return "SELECT * FROM Inventario WHERE Categoria = 2";
            case 3:
                return "SELECT * FROM Inventario";
            default:
                return null;
        }
    }

    public void vehiculos() {
        System.out.println("1. Añadir vehiculo al taller");
        System.out.println("2. Sacar vehiculo del taller");
        System.out.println("3. Ver vehiculos");
        opcion = sc.nextInt();
        sc.nextLine();

        switch (opcion) {
            case 1:
                añadirVehiculo();
                break;
            case 2:
                sacarVehiculo();
                break;
            case 3:
                verVehiculos();
                break;
            case 4:
                System.out.println("Atras..");
                break;
        }
    }

    private void añadirVehiculo() {
        System.out.println("Introduce la matricula del vehiculo: ");
        String matricula = sc.nextLine();

        System.out.println("Introduce el id del cliente propietario del vehiculo: ");
        String id_cliente = sc.nextLine();

        System.out.println("Introduce la marca del vehiculo: ");
        String marca = sc.nextLine();

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO Vehiculos (Matricula, DNI_Cliente, Marca) VALUES (?, ?, ?)")) {
            sentencia.setString(1, matricula);
            sentencia.setString(2, id_cliente);
            sentencia.setString(3, marca);
            sentencia.executeUpdate();
            System.out.println("Vehiculo añadido con exito.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sacarVehiculo() {
        System.out.println("Introduce la matricula del vehiculo a borrar: ");
        String matriculaABorrar = sc.nextLine();
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement sentencia = conexion.prepareStatement("DELETE FROM Vehiculos WHERE Matricula = ?")) {
            sentencia.setString(1, matriculaABorrar);
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Vehiculo borrado con exito");
            } else {
                System.out.println("No se encontro esa matricula");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void verVehiculos() {
        try (Connection conexion = ConexionDB.conectar();
             Statement sentencia = conexion.createStatement();
             ResultSet resultados = sentencia.executeQuery("SELECT * FROM Vehiculos")) {
            while (resultados.next()) {
                System.out.println("Matricula: " + resultados.getString("Matricula") + ", Marca: " + resultados.getString("Marca") + ", id cliente: " + resultados.getString("DNI_Cliente"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void tienda() {
        // Implementa la lógica para la tienda (consultas SQL para mostrar y comprar productos)
    }
}
