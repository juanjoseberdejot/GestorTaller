package GestorTaller.src.APP;
import java.util.ArrayList;
import java.util.Scanner;

public class Taller {
    
    ArrayList<String> herramientas = new ArrayList<>();
    ArrayList<String> piezas = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    public void mostrarInventario() {

        int opcion;

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
        }while(opcion != 4);
    }
}
