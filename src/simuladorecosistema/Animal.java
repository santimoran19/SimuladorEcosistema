package simuladorecosistema;

public abstract class Animal extends Entidad implements Mortal {

    private int velocidad;
    private double peso;

    public Animal(String nombre, double energia, int velocidad, double peso) {
        super(nombre, energia);
        setVelocidad(velocidad);
        setPeso(peso);
    }

    public abstract void comer(Ecosistema eco);

    public void moverse() {
        System.out.println(getNombre() + " se desplazo buscando alimento (velocidad: " + velocidad + ")");
    }

    @Override
    public boolean estaVivo() {
        return isViva();
    }

    @Override
    public void morir() {
        setViva(false);
        setEnergia(0);
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        if (velocidad <= 0) {
            return;
        }
        this.velocidad = velocidad;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        if (peso <= 0) {
            return;
        }
        this.peso = peso;
    }
}
