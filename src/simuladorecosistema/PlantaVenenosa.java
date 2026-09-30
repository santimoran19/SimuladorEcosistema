package simuladorecosistema;

public class PlantaVenenosa extends Planta {

    public PlantaVenenosa(String nombre, double energia, int tamanio) {
        super(nombre, energia, tamanio);
    }

    @Override
    public double serComida() {
        setEnergia(0);
        setViva(false);
        return -30;
    }
}
