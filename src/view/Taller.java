package view;

import dao.ConexionDB;
import java.sql.*;
import java.util.Scanner;

public class Taller {

    Scanner sc = new Scanner(System.in);
    int opcion;

    public void mostrarInventario() {
        do {
            System.out.println("1. Herramientas");
            System.out.println("2. Piezas");
            System.out.println("3. Todo");
            System.out.println("4. Salir");
            this.opcion = sc.nextInt();

            try (Connection conexion = ConexionDB.conectar();
                 PreparedStatement sentencia = conexion.prepareStatement(obtenerConsultaInventario(this.opcion));
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
        this.opcion = sc.nextInt();
        sc.nextLine();

        switch (this.opcion) {
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

        System.out.println("Introduce la marca del vehiculo: ");
        String marca = sc.nextLine();

        System.out.println("Introduce el id del cliente propietario del vehiculo: ");
        String id_cliente = sc.nextLine();

        try (Connection conexion = ConexionDB.conectar()) {
            // Verificar si el cliente existe
            String verificarCliente = "SELECT COUNT(*) FROM Clientes WHERE DNI = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(verificarCliente)) {
                stmt.setString(1, id_cliente);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("Error: El cliente con DNI " + id_cliente + " no existe.");
                    return;
                }
            }

            // Insertar el vehículo
            String insertarVehiculo = "INSERT INTO Vehiculos (Matricula, Marca, DNI_Cliente, EmpleadoAsignado) VALUES (?, ?, ?, ?)";
            try (PreparedStatement sentencia = conexion.prepareStatement(insertarVehiculo)) {
                sentencia.setString(1, matricula);
                sentencia.setString(2, id_cliente);
                sentencia.setString(3, marca);
                sentencia.setString(4, null); // No hay empleado asignado aun
                sentencia.executeUpdate();
                System.out.println("Vehículo añadido correctamente.");
            }
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
                System.out.println("ID: " + resultados.getString("ID") + "Matricula: " + resultados.getString("Matricula") + ", Marca: " + resultados.getString("Marca") + ", DNI_Cliente: " + resultados.getString("DNI_Cliente") + ", EmpleadoAsignado: " + resultados.getString("EmpleadoAsignado"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public float obtenerPrecioArticulo(int articuloElejido) {
        float precio = 0;
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement sentencia = conexion.prepareStatement("SELECT Precio FROM Tienda WHERE ID = ?")) {
            sentencia.setInt(1, articuloElejido);
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                precio = resultado.getFloat("Precio");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return precio;
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
                            // Insertar el artículo en la tabla de inventario
                            String insertarComprado = "INSERT INTO Inventario (ID, Nombre, Categoria, Precio) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement insertar = conexion.prepareStatement(insertarComprado)) {
                                insertar.setInt(1, resultado.getInt("ID"));
                                insertar.setString(2, resultado.getString("Nombre"));
                                insertar.setInt(3, resultado.getInt("Categoria"));
                                insertar.setDouble(4, resultado.getDouble("Precio"));
                                insertar.executeUpdate();
                            }

                            // Gasto
                            if (obtenerDineroActual() >= obtenerPrecioArticulo(Integer.parseInt(articuloElejido))) {
                                float dineroActual = obtenerDineroActual();
                                float nuevoDinero = dineroActual - obtenerPrecioArticulo(Integer.parseInt(articuloElejido));
                                String actualizarDinero = "UPDATE Dinero SET Dinero = ?";
                                try (Connection innerConexion = ConexionDB.conectar();
                                     PreparedStatement actualizar = innerConexion.prepareStatement(actualizarDinero)) {
                                    actualizar.setFloat(1, nuevoDinero);
                                    actualizar.executeUpdate();
                                    System.out.println("Dinero actualizado: " + nuevoDinero + "€");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("No hay suficiente dinero para comprar este artículo.");
                            }
    
                            // Eliminar el artículo de la tabla Tienda
                            String eliminarArticulo = "DELETE FROM Tienda WHERE ID = ?";
                            try (PreparedStatement eliminar = conexion.prepareStatement(eliminarArticulo)) {
                                eliminar.setString(1, articuloElejido);
                                int filasAfectadas = eliminar.executeUpdate();
                                if (filasAfectadas > 0) {
                                    System.out.println("Articulo comprado con exito (Añadido a Inventario).");
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
        }
    }

    public void clientes() {
        System.out.println("1. Añadir cliente");
        System.out.println("2. Borrar cliente");
        System.out.println("3. Ver clientes");
        System.out.println("4. Ver solicitudes de cita, rechazar o aceptar");
        System.out.println("5. Crear presupuesto");
        System.out.println("6. Salir");
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
                    PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO Cliente (DNI, Nombre, Apellido, Telefono, CantidadCompras) VALUES (?, ?, ?, ?, ?)")) {
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
                        System.out.println("ID: " + resultados.getString("ID") + ", DNI: " + resultados.getString("DNI") + ", Nombre: " + resultados.getString("Nombre") + ", Apellido: " + resultados.getString("Apellido") + ", Telefono: " + resultados.getString("Telefono") + ", CantidadCompras: " + resultados.getString("CantidadCompras"));
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
                        System.out.println("ID: " + resultados.getString("ID") + ", DNI Cliente: " + resultados.getString("DNI_Cliente") + ", Fecha Cita: " + resultados.getString("Fecha_Cita") + ", Hora: " + resultados.getString("Hora") + ", Descripcion: " + resultados.getString("descripcionProblema") + ", Estado: " + resultados.getString("Estado"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                
                System.out.println("Elija cita por ID: ");
                String citaElejida = sc.nextLine();
            
                System.out.println("¿Qué desea hacer con la cita seleccionada? Aceptar(1) Rechazar(2) Cancelar Todo (3)");
                int accion = sc.nextInt();
                sc.nextLine(); // Limpiar el buffer del scanner
            
                switch (accion) {
                    case 1 -> { // Aceptar la cita
                        try (Connection conexion2 = ConexionDB.conectar()) {
                            // Obtener los datos de la cita antes de eliminarla
                            String consultaCita = "SELECT DNI_Cliente, Fecha_Cita, Hora, descripcionProblema FROM solicitudesCitas WHERE ID = ?";
                            try (PreparedStatement obtenerCita = conexion2.prepareStatement(consultaCita)) {
                                obtenerCita.setString(1, citaElejida);
                                ResultSet resultado = obtenerCita.executeQuery();
                    
                                if (resultado.next()) {
                                    // Insertar la cita en la tabla Citas
                                    String insertarCita = "INSERT INTO Citas (DNI_Cliente, Fecha_Cita, Hora, Descipcion, Estado) VALUES (?, ?, ?, ?, ?)";
                                    try (PreparedStatement insertar = conexion2.prepareStatement(insertarCita)) {
                                        insertar.setString(1, resultado.getString("DNI_Cliente"));
                                        insertar.setString(2, resultado.getString("Fecha_Cita"));
                                        insertar.setString(3, resultado.getString("Hora"));
                                        insertar.setString(4, resultado.getString("descripcionProblema"));
                                        insertar.setString(5, "Pendiente");
                                        insertar.executeUpdate();
                                    }
                    
                                    // Cambiar estado de la solicitud cita a "Aceptada" 
                                    String aceptarCita = "UPDATE solicitudesCitas SET Estado = 'Aceptada' WHERE ID = ?";
                                    try (PreparedStatement eliminar = conexion2.prepareStatement(aceptarCita)) {
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
                        try (Connection conexion3 = ConexionDB.conectar()) {
                            // Cambiar estado de la solicitud cita a "Rechazada"
                            String rechazarCita = "UPDATE solicitudesCitas SET Estado = 'Rechazada' WHERE ID = ?";
                            try (PreparedStatement eliminar = conexion3.prepareStatement(rechazarCita)) {
                                eliminar.setString(1, citaElejida);
                                int filasAfectadas = eliminar.executeUpdate();
                                if (filasAfectadas > 0) {
                                    System.out.println("Cita rechazada con éxito.");
                                } else {
                                    System.out.println("No se encontró una cita con ese ID.");
                                }
                            }
                        } catch (SQLException e3) {
                            e3.printStackTrace();
                        }
                        break;
                    }
        
                    case 6 -> {
                        System.out.println("Atras..");
                        break;
                    }
                }
            }

            case 5 -> {
                System.out.println("Introduce el id del vehiculo: ");
                String matricula = sc.nextLine();

                System.out.println("Introduce el presupuesto: ");
                float presupuesto = sc.nextFloat();
                sc.nextLine();

                System.out.println("Introduce el : ");
                String DNI = sc.nextLine();

                try (Connection conexion = ConexionDB.conectar();
                     PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO Presupuestos (Matricula, Presupuesto, DNI_Cliente) VALUES (?, ?, ?)")) {
                    sentencia.setString(1, matricula);
                    sentencia.setFloat(2, presupuesto);
                    sentencia.setString(3, DNI);
                    sentencia.executeUpdate();
                    System.out.println("Presupuesto creado con exito.");

                } catch (SQLException e) {
                    e.printStackTrace();
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
                System.out.println("Introduce la matricula del vehiculo: ");
                String matricula = sc.nextLine();

                System.out.println("Introduce el DNI empleado: ");
                String dniEmpleado = sc.nextLine();

                try (Connection conexion = ConexionDB.conectar();
                     PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO Asignaciones (Matricula, DNI_Empleado) VALUES (?, ?)")) {
                    sentencia.setString(1, matricula);
                    sentencia.setString(2, dniEmpleado);
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
                        System.out.println("ID: " + resultados.getString("ID") + ", Matricula: " + resultados.getString("Matricula") + ", DNI_Empleado: " + resultados.getString("DNI_Empleado"));
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
            
            String estadoDefault = "Pendiente"; // Estado por defecto

            try (Connection conexion = ConexionDB.conectar();
                 PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO solicitudesCitas (DNI_Cliente, Fecha_Cita, Hora, Descripcion, Estado) VALUES (?, ?, ?, ?, ?)")) {
                sentencia.setString(1, dniCliente);
                sentencia.setString(2, fechaCita);
                sentencia.setString(3, horaCita);
                sentencia.setString(4, descripcionProblema);
                sentencia.setString(5, estadoDefault);
                sentencia.executeUpdate();
                System.out.println("Cita solicitada con exito.");
    
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void verEstadoSolicitudesCita() {
        System.out.println("Introduzca su DNI: ");
        String dniCliente = sc.nextLine();
        if (dniCliente != null && dniCliente.equals(buscarClientePorDNI())) {
            try (Connection conexion = ConexionDB.conectar();
                 PreparedStatement sentencia = conexion.prepareStatement("SELECT * FROM solicitudesCitas WHERE DNI_Cliente = ?")) {
                sentencia.setString(1, dniCliente);
                ResultSet resultados = sentencia.executeQuery();

                while (resultados.next()) {
                    System.out.println("ID: " + resultados.getString("ID") + ", Fecha Cita: " + resultados.getString("Fecha_Cita") + ", Hora: " + resultados.getString("Hora") + ", Descripcion: " + resultados.getString("Descripcion") + ", Estado: " + resultados.getString("Estado"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public float obtenerDineroActual() {
        float dineroActual = 0;
        try (Connection conexion = ConexionDB.conectar();
             Statement sentencia = conexion.createStatement();
             ResultSet resultados = sentencia.executeQuery("SELECT * FROM Dinero")) {
            if (resultados.next()) {
                dineroActual = resultados.getFloat("Dinero");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dineroActual;
    }

    public void pagarPresupuesto() {

        try (Connection conexion = ConexionDB.conectar();
             Statement sentencia = conexion.createStatement();
             ResultSet resultados = sentencia.executeQuery("SELECT * FROM Presupuestos")) {
            while (resultados.next()) {
                System.out.println("Matricula: " + resultados.getString("Matricula") + "Presupuesto: " + resultados.getString("Presupuesto") + ", DNI_Cliente: " + resultados.getString("DNI_Cliente"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Elija (1 aceptar pago) (2 rechazar pago): ");
        switch(opcion) {
            case 1 -> {
                // Gasto
                    float dineroActual = obtenerDineroActual();
                    float pagoDinero = obtenerPresupuesto();
                    float nuevoDinero = dineroActual + pagoDinero;
                    String actualizarDinero = "UPDATE Dinero SET Dinero = ?";
                    try (Connection conexion = ConexionDB.conectar();
                         PreparedStatement actualizar = conexion.prepareStatement(actualizarDinero)) {
                        actualizar.setFloat(1, nuevoDinero);
                        actualizar.executeUpdate();
                        System.out.println("Dinero actualizado: " + nuevoDinero + "€");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    public float obtenerPresupuesto() {

        System.out.println("Introduce el DNI del cliente: ");
        int dniCliente = sc.nextInt();
        sc.nextLine();

        System.out.println("Introduce la matricula del vehiculo: ");
        int matricula = sc.nextInt();
        sc.nextLine();

        // Obtener el presupuesto del cliente
        float presupuesto = 0;
        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement sentencia = conexion.prepareStatement("SELECT Presupuesto FROM Presupuestos WHERE DNI_Cliente = ? AND Matricula = ?")) {
            sentencia.setInt(1, dniCliente);
            sentencia.setInt(1, matricula);
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                presupuesto = resultado.getFloat("Presupuesto");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return presupuesto;
    }
}