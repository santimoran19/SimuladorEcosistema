/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simuladorecosistema;

import java.util.ArrayList;

/**
 *
 * @author Lucas
 */
public class Lobo extends Animal {
     private int exitosCaza;   
    public Lobo(String nombre, double energia) {
        super(nombre, energia, 6, 40);   
        this.exitosCaza = 0;
    }

    @Override
    public void actuar(Ecosistema eco) {
        comer(eco);   
    }

    @Override
    public void comer(Ecosistema eco) {
        ArrayList<Conejo> presas = new ArrayList<>();
        for (Conejo c : eco.getConejos()) {
            if (c.estaVivo()) {
                presas.add(c);
            }
        }

        if (presas.size() == 0) {
            moverse();
            eco.registrarEvento("Lobo '" + getNombre() + "' no encontro presas");
            return;
        }

        Conejo presa = presas.get(eco.aleatorio(presas.size()));
        int probabilidad = calcularProbabilidadCaza(eco);

        if (eco.aleatorio(100) < probabilidad) {
            presa.morir();
            setEnergia(getEnergia() + 30);
            exitosCaza++;
            eco.registrarEvento("Lobo '" + getNombre() + "' cazo a Conejo '" + presa.getNombre()
                    + "' (+30 energia) [cacerias: " + exitosCaza + "]");
        } else {
            eco.registrarEvento("Lobo '" + getNombre() + "' fallo la caza");
        }
    }

    public int calcularProbabilidadCaza(Ecosistema eco) {
        int probabilidad = (int) (getEnergia() / 2);
        if (probabilidad < 10) {
            probabilidad = 10;
        }
        if (probabilidad > 80) {
            probabilidad = 80;
        }
        if (eco.getClimaActual() == Clima.INVIERNO) {
            probabilidad = probabilidad + 20; 
        }
        return probabilidad;
    }

    @Override
    public void mostrarEstado() {
        System.out.println("Lobo '" + getNombre() + "' | Energia: " + (int) getEnergia()
                + " | Cacerias exitosas: " + exitosCaza);
    }

    public int getExitosCaza() {
        return exitosCaza;
    }

    public void setExitosCaza(int exitosCaza) {
        if (exitosCaza < 0) {
            return;
        }
        this.exitosCaza = exitosCaza;
    }
}
