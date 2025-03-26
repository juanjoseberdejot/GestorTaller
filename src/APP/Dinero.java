package GestorTaller.src.APP;

public class Dinero {
    
    private float dinero = 1000;

    public Dinero(float dinero) {

        this.dinero = dinero;

    }

    public float getDineroActual() {
        
        return this.dinero;

    }

    public void setDinero(float dineroActual) {
        this.dinero = dineroActual;
    }
}
