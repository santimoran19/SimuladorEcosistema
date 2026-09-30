package simuladorecosistema;

public class Planta extends Entidad implements Reproducible {

    private int tamanio;  

    public Planta(String nombre, double energia, int tamanio) {
        super(nombre, energia);
        setTamanio(tamanio);
    }

    @Override
    public void actuar(Ecosistema eco) {
        intentarReproduccion(eco);
    }

    @Override
    public void mostrarEstado() {
        System.out.println("Planta '" + getNombre() + "' | Tamanio: " + tamanio + " | Energia: " + (int) getEnergia());
    }
    public double serComida() {
        setEnergia(0);
        setViva(false);
        return tamanio * 10;
    }

    @Override
    public boolean puedeReproducirse() {
        return isViva() && getEnergia() >= 30;
    }

    @Override
    public void reproducirse(Ecosistema eco) {
        if (eco.getPlantas().size() >= 60) {
            return;
        }
        double factor = 1;
        switch (eco.getClimaActual()) {
            case SOLEADO:
                factor = 1.5;
                break;
            case LLUVIOSO:
                factor = 2;
                break;
            case SEQUIA:
                factor = 0.5;
                break;
            case INVIERNO:
                factor = 0;   
                break;
        }
        int probabilidad = (int) (30 * factor);

        if (eco.aleatorio(100) < probabilidad) {
            Planta nueva = new Planta(eco.generarNombre("planta"), 50, eco.aleatorio(5) + 1);
            eco.agregarNacimiento(nueva);
            eco.registrarEvento("Planta '" + getNombre() + "' se reprodujo -> nueva planta '"
                    + nueva.getNombre() + "' (energia: " + (int) nueva.getEnergia() + ")");
        }
    }

    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        if (tamanio < 1) {
            tamanio = 1;
        }
        if (tamanio > 5) {
            tamanio = 5;
        }
        this.tamanio = tamanio;
    }
}
