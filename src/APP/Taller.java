package GestorTaller.src.app;
import java.util.ArrayList;
import java.util.Scanner;

public class Taller {
    
    ArrayList<String> herramientas = new ArrayList<>();
    ArrayList<String> piezas = new ArrayList<>();
    ArrayList<String> coches = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    int opcion;

    public void mostrarInventario() {

        do {

            System.out.println("1. Herramientas");
            System.out.println("2. Piezas");
            System.out.println("3. Todo");
            System.out.println("4. Salir");
            opcion = sc.nextInt();

            switch(opcion){

                case 1 -> {

                    if (herramientas.isEmpty()) {
                        System.out.println("Inventario vacio (Puede comprar mas articulos en el apartado -Tienda-)");
                        opcion = 4;
                    } else {
                        System.out.println("Inventario herramientas: ");
                        for (String herramienta : herramientas) {
                            System.out.println(herramienta);
                        }
                    }

                }

                case 2 -> {

                    if (piezas.isEmpty()) {
                        System.out.println("Inventario vacio (Puede comprar mas articulos en el apartado -Tienda-)");
                        opcion = 4;
                    } else {
                        System.out.println("Inventario piezas: ");
                        for (String pieza : piezas) {
                            System.out.println(pieza);
                        }
                    }

                }

                case 3 -> {

                    if (piezas.isEmpty() && herramientas.isEmpty()) {
                        System.out.println("Inventario vacio (Puede comprar mas articulos en el apartado -Tienda-)");
                        opcion = 4;
                    } else {
                        System.out.println("Inventario completo: ");
                        for (String pieza : piezas) {
                            System.out.println(pieza);
                        }
                        for (String herramienta : herramientas) {
                            System.out.println(herramienta);
                        }
                    }

                }

                case 4 -> {

                    System.out.println("Atras..");

                }

            }
        } while(opcion != 4);
    }

    public void vehiculos() {

        System.out.println("1. Añadir vehiculo al taller");
        System.out.println("2. Sacar vehiculo del taller");
        System.out.println("3. Ver vehiculos");
        opcion = sc.nextInt();

        switch(opcion) {

            case 1 -> {

                System.out.println("Introduce la matricula del vehiculo: ");
                String matricula = sc.nextLine();

                System.out.println("Introduce el id del cliente propietario del vehiculo: ");
                int id_cliente= sc.nextInt();

                System.out.println("Introduce la marca del vehiculo: ");
                String marca = sc.nextLine();

                Vehiculo vehiculo = new Vehiculo(matricula, id_cliente, marca);

            }

            case 2 -> {

                System.out.println("Introduce la matricula del vehiculo: ");

            }

            case 3 -> {

                if (coches.isEmpty()) {
                    System.out.println("Taller vacio");
                    opcion = 4;
                } else {
                    System.out.println("Coches en taller: ");
                    for (String coche : coches) {
                        System.out.println(coche);
                    }
                }

            }

            case 4 -> {

                System.out.println("Atras..");

            }
        }
    }
}
