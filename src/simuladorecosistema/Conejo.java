package simuladorecosistema;

import java.util.ArrayList;

public class Conejo extends Animal implements Reproducible {

    public Conejo(String nombre, double energia) {
        super(nombre, energia, 8, 2.5); 
    }

    @Override
    public void actuar(Ecosistema eco) {

        comer(eco);
    }

    @Override
    public void comer(Ecosistema eco) {
        ArrayList<Planta> disponibles = new ArrayList<>();
        for (Planta p : eco.getPlantas()) {
            if (p.isViva()) {
                disponibles.add(p);
            }
        }

        int probabilidadEncontrar = disponibles.size() * 5;
        if (probabilidadEncontrar > 90) {
            probabilidadEncontrar = 90;
        }

        String texto;
        if (eco.aleatorio(100) < probabilidadEncontrar) {
            Planta elegida = disponibles.get(eco.aleatorio(disponibles.size()));
            double valor = elegida.serComida();
            setEnergia(getEnergia() + valor);
            if (valor < 0) {
                texto = "Conejo '" + getNombre() + "' comio '" + elegida.getNombre()
                        + "' pero era venenosa (" + (int) valor + " energia)";
            } else {
                texto = "Conejo '" + getNombre() + "' comio '" + elegida.getNombre()
                        + "' (+" + (int) valor + " energia)";
            }
        } else {
            moverse();
            setEnergia(getEnergia() - 15);
            texto = "Conejo '" + getNombre() + "' no encontro comida (-15 energia)";
        }

        if (getEnergia() < 20) {
            texto = texto + " [PELIGRO: energia=" + (int) getEnergia() + "]";
        }
        eco.registrarEvento(texto);
    }

    @Override
    public void mostrarEstado() {
        String estado = "Conejo '" + getNombre() + "' | Energia: " + (int) getEnergia();
        if (getEnergia() < 20) {
            estado = estado + " | EN PELIGRO";
        }
        System.out.println(estado);
    }

    @Override
    public boolean puedeReproducirse() {
        return isViva() && getEnergia() > 60;
    }

    @Override
    public void reproducirse(Ecosistema eco) {
        if (eco.getConejos().size() >= 2 && eco.aleatorio(100) < 40) {
            Conejo cria = new Conejo(eco.generarNombre("conejo"), 30);
            setEnergia(getEnergia() - 30); 
            eco.agregarNacimiento(cria);
            eco.registrarEvento("Conejo '" + getNombre() + "' tuvo una cria -> nuevo conejo '"
                    + cria.getNombre() + "' (energia: " + (int) cria.getEnergia() + ")");
        }
    }
}
