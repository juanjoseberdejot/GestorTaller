package model;
public class Vehiculo {
    
    private String matricula;
    private int id_cliente;
    private String marca;

    public Vehiculo(String matricula, int id_cliente, String marca) {

        this.matricula = matricula;
        this.id_cliente = id_cliente;
        this.marca = marca;

    }

    public String getMatricula() {
        return this.matricula;
    }

    @Override
    public String toString() {
        return "Vehiculo [Matricula: " + matricula + ", ID Cliente: " + id_cliente + ", Marca: " + marca + "]";
    }

}
