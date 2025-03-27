package view;
import java.sql.Connection;
import java.util.Scanner;

import dao.ConexionBD;
import model.Dinero;

public class App {
    public static void main (String[]args) {

        // Verificar conexion con base de datos
        Connection conexion = ConexionBD.conectar();
        if (conexion != null) {
            System.out.println("Conexión establecida correctamente.");
        } else {
            System.out.println("No se pudo establecer la conexión.");
        }

        Scanner sc = new Scanner(System.in);
        Taller taller = new Taller();
        Dinero dinero = new Dinero(1000);
        
        float dineroActual = dinero.getDineroActual();

        String contraseñaEmpleado = "soy un empleado";
        System.out.println("Contraseña: ");
        String contraseñaIntroducida = sc.nextLine();

        int opcion;

        if (contraseñaIntroducida.equals(contraseñaEmpleado)) {        
            
            do {
                //Menu Empleado
                System.out.println("Flujo de caja: " + dineroActual + "€");
                System.out.println("1. Inventario");
                System.out.println("2. Vehiculos");
                System.out.println("3. Tienda");
                System.out.println("4. Clientes");
                System.out.println("5. Asignaciones");
                System.out.println("6. Salir");
                opcion = sc.nextInt();
                sc.nextLine();

                //Metodos Menu Empleado
                switch(opcion){
                    case 1 -> {
                        taller.mostrarInventario();
                    }
                    case 2 -> {
                        taller.vehiculos();
                    }
                    case 3 -> {
                        //tienda();
                    }
                    case 4 -> {
                        //clientes();
                    }
                    case 5 -> {
                        //Asignaciones();
                    }
                    case 6 -> {
                        System.out.println("Adios..");
                    }
                }
            }while(opcion != 6);

        } else {
            
            do {
                //Menu Cliente
                System.out.println("1. Crear peticion");
                System.out.println("2. Ver estado peticion");
                System.out.println("3. Presupuestos");
                System.out.println("4. Reclamaciones");
                System.out.println("5. Salir");
                opcion = sc.nextInt();  
                sc.nextLine();

                //Metodos Menu Cliente
                switch(opcion){
                    case 1 -> {
                        //crearPeticion();
                    }
                    case 2 -> {
                        //verEstadoPeticiones();
                    }
                    case 3 -> {
                        //Presupuestos();
                    }
                    case 4 -> {
                        //Reclamaciones();
                    }
                    case 5 -> {
                        System.out.println("Adios..");
                    }
                }
            }while(opcion != 5);
        }
    }
}