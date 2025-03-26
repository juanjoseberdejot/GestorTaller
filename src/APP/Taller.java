package GestorTaller.src.APP;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Taller {
    
    ArrayList<Producto> productos = new ArrayList<>();
    ArrayList<Vehiculo> vehiculos = new ArrayList<>();

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
                        for (Producto herramienta : this.herramientas) {
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
                        for (Producto pieza : this.piezas) {
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
                        for (Producto pieza : this.piezas) {
                            System.out.println(pieza);
                        }
                        for (Producto herramienta : this.herramientas) {
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
        sc.nextLine();

        switch(opcion) {

            case 1 -> {
                
                boolean enxiste = false;

                System.out.println("Introduce la matricula del vehiculo: ");
                String matricula = sc.nextLine();

                for (Vehiculo vehiculo : this.vehiculos) {
                    if (vehiculo.getMatricula().equals(matricula)) {
                        System.out.println("Coche ya añadido");
                        enxiste = true;
                    }
                }
                
                if (enxiste) {
                    break;
                }

                if (matricula.isEmpty()) {
                    System.out.println("La matrícula no puede estar vacía.");
                    break;
                }

                System.out.println("Introduce el id del cliente propietario del vehiculo: ");
                int id_cliente = sc.nextInt();
                sc.nextLine();

                if (id_cliente <= 0) {
                    System.out.println("La id del cliente propietario del vehiculo no puede estar vacía.");
                    break;
                }

                System.out.println("Introduce la marca del vehiculo: ");
                String marca = sc.nextLine();

                if (marca.isEmpty()) {
                    System.out.println("La marca del vehiculo no puede estar vacía.");
                    break;
                }

                Vehiculo vehiculo = new Vehiculo(matricula, id_cliente, marca);

                vehiculos.add(vehiculo);

            }

            case 2 -> {

                boolean borrado = false;

                if (vehiculos.isEmpty()) {

                    System.out.println("No hay nada que eliminar");
                    break;
                } else {

                    System.out.println("Introduce la matricula del vehiculo: ");
                    String matriculaABorrar = sc.nextLine();
                    
                    for (Vehiculo vehiculo : this.vehiculos) {

                        if (vehiculo.getMatricula().equals(matriculaABorrar)) {
                            vehiculos.remove(vehiculo);
                            borrado = true;
                            break;
                        }
                    }

                    if (borrado == true) {
                        System.out.println("Vehiculo borrado con exito");
                    } else {
                        System.out.println("No se encontro esa matricula");
                        break;
                    }

                }

            }

            case 3 -> {

                if (vehiculos.isEmpty()) {
                    System.out.println("Taller vacio");
                    opcion = 4;
                } else {
                    System.out.println("Coches en taller: ");
                    for (Vehiculo coche : this.vehiculos) {
                        System.out.println(coche);
                    }
                }

            }

            case 4 -> {

                System.out.println("Atras..");

            }
        }
    }

    public void tienda() {

        productos.add(new Producto("Ruedas coche", 1, 50));
        productos.add(new Producto("Bujia", 1, 15));
        productos.add(new Producto("Destornillador", 2, 20));
        productos.add(new Producto("Llave inglesa", 2, 25));
        productos.add(new Producto("Aceite", 1, 10));
        productos.add(new Producto("Tornillos", 1, 5));
        productos.add(new Producto("Filtro", 1, 30));
        productos.add(new Producto("Pintura", 1, 40));
        productos.add(new Producto("Gafas protec.", 1, 8));
        productos.add(new Producto("Motor", 1, 150));

        do { 
            
            System.out.println("Flujo de caja: " + dineroActual + "€");



        } while ();
    }
}
