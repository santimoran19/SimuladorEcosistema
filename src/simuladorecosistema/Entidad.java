package simuladorecosistema;

public abstract class Entidad {

    private String nombre;
    private double energia;
    private int edad;
    private boolean viva;

    public Entidad(String nombre, double energia) {
        setNombre(nombre);
        setEnergia(energia);  
        this.edad = 0;
        this.viva = true;
    }

    public abstract void actuar(Ecosistema eco);

    public abstract void mostrarEstado();

    public void envejecer() {
        edad++;
        setEnergia(energia - 2);  
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.length() == 0) {
            return;   
        }
        this.nombre = nombre;
    }

    public double getEnergia() {
        return energia;
    }

    public void setEnergia(double energia) {
        if (energia < 0) {
            energia = 0;    
        }
        if (energia > 100) {
            energia = 100;  
        }
        this.energia = energia;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        if (edad < 0) {
            return;
        }
        this.edad = edad;
    }

    public boolean isViva() {
        return viva;
    }

    public void setViva(boolean viva) {
        this.viva = viva;
    }
}
