/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package simuladorecosistema;

/**
 *
 * @author Lucas
 */
public interface Mortal {
    boolean estaVivo();

    void morir();

    double getEnergia();

    String getNombre();

    default void verificarMuerte() {
        if (getEnergia() <= 0) {
            morir();
            System.out.println("'" + getNombre() + "' murio de inanicion");
        }
    }
}
