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
                System.out.println("ID: " + resultados.getString("ID") + "Matricula: " + resultados.getString("Matricula") + ", Marca: " + resultados.getString("Marca") + ", id cliente: " + resultados.getString("DNI_Cliente"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void tienda() {
        try (Connection conexion = ConexionDB.conectar();
                Statement sentencia = conexion.createStatement();
                ResultSet resultados = sentencia.executeQuery("SELECT * FROM Tienda")) {
            while (resultados.next()) {
                System.out.println("ID: " + resultados.getString("ID") + ", Nombre: " + resultados.getString("Nombre") + ", Categoria: " + resultados.getString("Categoria") + ", Precio: " + resultados.getString("Precio"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Elija articulo: ( 1, 2, 3, 4, 5, 6 ) Salir(7)");
        String articuloElejido = sc.nextLine();
        
        switch (articuloElejido) {
            case "1", "2", "3", "4", "5", "6" -> {
                try (Connection conexion = ConexionDB.conectar()) {
                    // Obtener los datos del artículo antes de eliminarlo
                    String consultaArticulo = "SELECT ID, Nombre, Categoria, Precio FROM Tienda WHERE ID = ?";
                    try (PreparedStatement obtenerArticulo = conexion.prepareStatement(consultaArticulo)) {
                        obtenerArticulo.setString(1, articuloElejido);
                        ResultSet resultado = obtenerArticulo.executeQuery();
    
                        if (resultado.next()) {
                            // Insertar el artículo en la tabla de artículos comprados
                            String insertarComprado = "INSERT INTO Inventario (ID, Nombre, Categoria, Precio) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement insertar = conexion.prepareStatement(insertarComprado)) {
                                insertar.setInt(1, resultado.getInt("ID"));
                                insertar.setString(2, resultado.getString("Nombre"));
                                insertar.setInt(3, resultado.getInt("Categoria"));
                                insertar.setDouble(4, resultado.getDouble("Precio"));
                                insertar.executeUpdate();
                            }
    
                            // Eliminar el artículo de la tabla Tienda
                            String eliminarArticulo = "DELETE FROM Tienda WHERE ID = ?";
                            try (PreparedStatement eliminar = conexion.prepareStatement(eliminarArticulo)) {
                                eliminar.setString(1, articuloElejido);
                                int filasAfectadas = eliminar.executeUpdate();
                                if (filasAfectadas > 0) {
                                    System.out.println("Articulo comprado con exito.");
                                } else {
                                    System.out.println("No se encontro ese articulo.");
                                }
                            }
                        } else {
                            System.out.println("No se encontro ese articulo.");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            case "7" -> System.out.println("Atras..");
            default -> System.out.println("Opción no válida.");
        }
    }

    public void clientes() {
        System.out.println("1. Añadir cliente");
        System.out.println("2. Borrar cliente");
        System.out.println("3. Ver clientes");
        System.out.println("3. Ver solicitudes de cita, rechazar o aceptar");
        opcion = sc.nextInt();
        sc.nextLine();

        switch (opcion) {
            case 1 -> {
                System.out.println("Introduce DNI: ");
                String DNI = sc.nextLine();
        
                System.out.println("Introduce nombre: ");
                String Nombre = sc.nextLine();
                
                System.out.println("Introduce apellidos: ");
                String Apellido = sc.nextLine();

                System.out.println("Telefono: ");
                int Telefono = sc.nextInt();
                sc.nextLine();
        
                try (Connection conexion = ConexionDB.conectar();
                    PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO Vehiculos (Matricula, DNI_Cliente, Marca) VALUES (?, ?, ?)")) {
                    sentencia.setString(1, DNI);
                    sentencia.setString(2, Nombre);
                    sentencia.setString(3, Apellido);
                    sentencia.setInt(4, Telefono);
                    sentencia.setInt(5, 0);
                    sentencia.executeUpdate();
                    System.out.println("Cliente añadido con exito");
        
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            case 2 -> {
                System.out.println("Introduce el DNI del cliente a borrar: ");
                String DNIBorrar = sc.nextLine();
                try (Connection conexion = ConexionDB.conectar();
                    PreparedStatement sentencia = conexion.prepareStatement("DELETE FROM Clientes WHERE DNI = ?")) {
                    sentencia.setString(1, DNIBorrar);
                    int filasAfectadas = sentencia.executeUpdate();
                    if (filasAfectadas > 0) {
                        System.out.println("Cliente borrado con exito");
                    } else {
                        System.out.println("No se encontro ese cliente");
                    }
        
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            case 3 -> {
                try (Connection conexion = ConexionDB.conectar();
                    Statement sentencia = conexion.createStatement();
                    ResultSet resultados = sentencia.executeQuery("SELECT * FROM Clientes")) {
                    while (resultados.next()) {
                        System.out.println("ID: " + resultados.getString("ID") + ", DNI: " + resultados.getString("DNI") + ", Nombre: " + resultados.getString("Nombre") + ", Apellido: " + resultados.getString("Apellido") + ", Telefono: " + resultados.getString("Telefono"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            case 4 -> {
                try (Connection conexion = ConexionDB.conectar();
                     Statement sentencia = conexion.createStatement();
                     ResultSet resultados = sentencia.executeQuery("SELECT * FROM solicitudesCitas")) {
   
                    // Mostrar todas las solicitudes de citas
                    while (resultados.next()) {
                        System.out.println("ID: " + resultados.getString("ID") +
                                  ", DNI Cliente: " + resultados.getString("DNI_Cliente") +
                                  ", Fecha Cita: " + resultados.getString("Fecha_Cita") +
                                  ", Hora: " + resultados.getString("Hora") +
                                  ", Descripcion: " + resultados.getString("descripcionProblema"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                
   
                System.out.println("Elija cita por ID: ");
                String citaElejida = sc.nextLine();
            
                System.out.println("¿Qué desea hacer con la cita seleccionada? Aceptar(1) Rechazar(2)");
                int accion = sc.nextInt();
                sc.nextLine(); // Limpiar el buffer del scanner
            
                switch (accion) {
                    case 1 -> { // Aceptar la cita
                        try (Connection conexion = ConexionDB.conectar()) {
                            // Obtener los datos de la cita antes de eliminarla
                            String consultaCita = "SELECT DNI_Cliente, Fecha_Cita, Hora, descripcionProblema FROM solicitudesCitas WHERE ID = ?";
                            try (PreparedStatement obtenerCita = conexion.prepareStatement(consultaCita)) {
                                obtenerCita.setString(1, citaElejida);
                                ResultSet resultado = obtenerCita.executeQuery();
                    
                                if (resultado.next()) {
                                    // Insertar la cita en la tabla Citas
                                    String insertarCita = "INSERT INTO Citas (DNI_Cliente, Fecha_Cita, Hora, descripcionProblema) VALUES (?, ?, ?, ?)";
                                    try (PreparedStatement insertar = conexion.prepareStatement(insertarCita)) {
                                        insertar.setString(1, resultado.getString("DNI_Cliente"));
                                        insertar.setString(2, resultado.getString("Fecha_Cita"));
                                        insertar.setString(3, resultado.getString("Hora"));
                                        insertar.setString(4, resultado.getString("descripcionProblema"));
                                        insertar.executeUpdate();
                                    }
                    
                                    // Eliminar la cita de la tabla solicitudesCitas
                                    String eliminarCita = "DELETE FROM solicitudesCitas WHERE ID = ?";
                                    try (PreparedStatement eliminar = conexion.prepareStatement(eliminarCita)) {
                                        eliminar.setString(1, citaElejida);
                                        int filasAfectadas = eliminar.executeUpdate();
                                        if (filasAfectadas > 0) {
                                            System.out.println("Cita aceptada con éxito y añadida a la tabla Citas.");
                                        } else {
                                            System.out.println("No se encontró una cita con ese ID.");
                                        }
                                    }
                                } else {
                                    System.out.println("No se encontró una cita con ese ID.");
                                }
                            }
                        } catch (SQLException e2) {
                            e2.printStackTrace();
                        }
                        break;
                    }
            
                    case 2 -> { // Rechazar la cita
                        try (Connection conexion = ConexionDB.conectar();
                            PreparedStatement sentencia = conexion.prepareStatement("DELETE FROM solicitudesCitas WHERE ID = ?")) {
                            sentencia.setString(1, citaElejida);
                            int filasAfectadas = sentencia.executeUpdate();
                            if (filasAfectadas > 0) {
                                System.out.println("Cita rechazada y eliminada con éxito.");
                            } else {
                                System.out.println("No se encontró una cita con ese ID.");
                            }
                        } catch (SQLException e2) {
                            e.printStackTrace();
                        }
                        break;
                    }
        
                    case 3 -> {
                        System.out.println("Atras..");
                        break;
                    }
                }
            }
        }
    }

    public void asignacionesMecanicos() {
        System.out.println("1. Asignar mecanico a vehiculo");
        System.out.println("2. Borrar asignacion");
        System.out.println("3. Ver asignaciones");
        opcion = sc.nextInt();
        sc.nextLine();

        switch (opcion) {
            case 1:
                System.out.println("Introduce el id del mecanico: ");
                String idMecanico = sc.nextLine();

                System.out.println("Introduce el id del vehiculo: ");
                String idVehiculo = sc.nextLine();

                try (Connection conexion = ConexionDB.conectar();
                     PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO Asignaciones (ID_Mecanico, ID_Vehiculo) VALUES (?, ?)")) {
                    sentencia.setString(1, idMecanico);
                    sentencia.setString(2, idVehiculo);
                    sentencia.executeUpdate();
                    System.out.println("Asignacion realizada con exito.");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                System.out.println("Introduce el id de la asignacion a borrar: ");
                String idAsignacionBorrar = sc.nextLine();
                try (Connection conexion = ConexionDB.conectar();
                    PreparedStatement sentencia = conexion.prepareStatement("DELETE FROM Asignaciones WHERE ID = ?")) {
                    sentencia.setString(1, idAsignacionBorrar);
                    int filasAfectadas = sentencia.executeUpdate();
                    if (filasAfectadas > 0) {
                        System.out.println("Asignacion borrada con exito");
                    } else {
                        System.out.println("No se encontro esa asignacion");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case 3:
                try (Connection conexion = ConexionDB.conectar();
                    Statement sentencia = conexion.createStatement();
                    ResultSet resultados = sentencia.executeQuery("SELECT * FROM Asignaciones")) {
                    while (resultados.next()) {
                        System.out.println("ID: " + resultados.getString("ID") + ", ID Mecanico: " + resultados.getString("ID_Mecanico") + ", ID Vehiculo: " + resultados.getString("ID_Vehiculo"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                System.out.println("Atras..");
                break;
        }
    }

    public String buscarClientePorDNI() {
        System.out.println("Introduce el DNI del cliente que deseas buscar: ");
        String dniClienteBuscado = sc.nextLine();
    
        String dniClienteExtraido = null; // Variable para almacenar el DNI extraído
    
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement sentencia = conexion.prepareStatement("SELECT DNI FROM Clientes WHERE DNI = ?")) {
            sentencia.setString(1, dniClienteBuscado); // Establece el parámetro de la consulta
            ResultSet resultado = sentencia.executeQuery();
    
            if (resultado.next()) {
                dniClienteExtraido = resultado.getString("DNI"); // Extrae el valor del DNI
                System.out.println("Cliente encontrado. DNI: " + dniClienteExtraido);
            } else {
                System.out.println("No se encontró un cliente con ese DNI.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dniClienteExtraido; // Devuelve el DNI extraído o null si no se encontró
    }

    public void solicitarCita() {
        System.out.println("Introduzca su DNI: ");
        String dniCliente = sc.nextLine();

        if (dniCliente != null && dniCliente.equals(buscarClientePorDNI())) {
            System.out.println("Introduce la fecha solicitada (DD-MM-YYYY): ");
            String fechaCita = sc.nextLine();

            System.out.println("Introduce la hora solicitada del dia " + fechaCita + ": ");
            String horaCita = sc.nextLine();
    
            System.out.println("Introduzca descripcion del problema: ");
            String descripcionProblema = sc.nextLine();
    
            try (Connection conexion = ConexionDB.conectar();
                 PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO solicitudesCitas (DNI_Cliente, Fecha_Cita, Hora, descripcionProblema) VALUES (?, ?, ?, ?)")) {
                sentencia.setString(1, dniCliente);
                sentencia.setString(2, fechaCita);
                sentencia.setString(3, horaCita);
                sentencia.setString(4, descripcionProblema);
                sentencia.executeUpdate();
                System.out.println("Cita solicitada con exito.");
    
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
