package simuladorecosistema;

import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int cantPlantas, cantConejos, cantLobos, turnos;
        Clima clima;
        boolean confirmado;

        System.out.println("=========== SIMULADOR DE ECOSISTEMA ===========");

        do {
            System.out.println();
            System.out.println("--- Configuracion inicial ---");
            cantPlantas = pedirEntero(sc, "Cantidad inicial de plantas (5 a 30): ", 5, 30);
            cantConejos = pedirEntero(sc, "Cantidad inicial de conejos (2 a 15): ", 2, 15);
            cantLobos = pedirEntero(sc, "Cantidad inicial de lobos (1 a 5): ", 1, 5);
            clima = pedirClima(sc);
            turnos = pedirEntero(sc, "Cantidad de turnos de la simulacion (10 a 50): ", 10, 50);

            System.out.println();
            System.out.println("Resumen: " + cantPlantas + " plantas, " + cantConejos + " conejos, "
                    + cantLobos + " lobos, clima " + clima + ", " + turnos + " turnos.");
            confirmado = pedirConfirmacion(sc, "Confirma la configuracion? (s/n): ");
        } while (!confirmado);

        Ecosistema eco = new Ecosistema(clima);
        System.out.println();
        for (int i = 0; i < cantPlantas; i++) {
            eco.agregarEntidad("planta");
        }
        for (int i = 0; i < cantConejos; i++) {
            eco.agregarEntidad("conejo");
        }
        for (int i = 0; i < cantLobos; i++) {
            eco.agregarEntidad("lobo");
        }
        System.out.println();
        eco.mostrarEstado();
        System.out.println(">>> Presione Enter para comenzar...");
        sc.nextLine();

      
        boolean terminado = false;
        while (!terminado) {
            eco.procesarTurno();

            if (eco.getTurnoActual() >= turnos || eco.ecosistemaColapsado()) {
                terminado = true;
            } else {
                System.out.println(">>> Presione Enter para continuar...");
                sc.nextLine();

          
                if (eco.getTurnoActual() % 3 == 0) {
                    menuIntervencion(sc, eco);
                }
            }
        }

    
        eco.generarReporteFinal();
    }


    public static int pedirEntero(Scanner sc, String mensaje, int min, int max) {
        int numero = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensaje);
            try {
                numero = Integer.parseInt(sc.nextLine());
                if (numero >= min && numero <= max) {
                    valido = true;
                } else {
                    System.out.println("Error: el valor debe estar entre " + min + " y " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un numero entero.");
            }
        }
        return numero;
    }

    public static Clima pedirClima(Scanner sc) {
        Clima[] climas = Clima.values();
        System.out.println("Climas disponibles:");
        for (int i = 0; i < climas.length; i++) {
            System.out.println("  " + (i + 1) + ". " + climas[i]);
        }
        int opcion = pedirEntero(sc, "Elija el clima (1 a " + climas.length + "): ", 1, climas.length);
        return climas[opcion - 1];
    }

    public static boolean pedirConfirmacion(Scanner sc, String mensaje) {
        String respuesta;
        do {
            System.out.print(mensaje);
            respuesta = sc.nextLine();
        } while (!respuesta.equalsIgnoreCase("s") && !respuesta.equalsIgnoreCase("n"));
        return respuesta.equalsIgnoreCase("s");
    }

    public static String pedirTipoEntidad(Scanner sc) {
        String tipo;
        do {
            System.out.print("Que entidad agregar? (planta/conejo/lobo): ");
            tipo = sc.nextLine().toLowerCase();
        } while (!tipo.equals("planta") && !tipo.equals("conejo") && !tipo.equals("lobo"));
        return tipo;
    }

    public static void menuIntervencion(Scanner sc, Ecosistema eco) {
        System.out.println();
        System.out.println("=== INTERVENCION (cada 3 turnos) ===");
        System.out.println("1. Cambiar clima (actual: " + eco.getClimaActual() + ")");
        System.out.println("2. Agregar entidad");
        System.out.println("3. Solo avanzar");
        int opcion = pedirEntero(sc, "Opcion: ", 1, 3);

        switch (opcion) {
            case 1:
                Clima nuevo = pedirClima(sc);
                if (pedirConfirmacion(sc, "Confirma cambiar el clima a " + nuevo + "? (s/n): ")) {
                    eco.cambiarClima(nuevo);
                } else {
                    System.out.println("Accion cancelada.");
                }
                break;

            case 2:
                String tipo = pedirTipoEntidad(sc);
                if (tipo.equals("lobo") && eco.getLobosCreados() >= 5) {
                    System.out.println("No se pueden agregar mas lobos: ya se crearon 5 en la simulacion.");
                } else if (pedirConfirmacion(sc, "Desea elegir la energia inicial? (s/n): ")) {
                    int energia = pedirEntero(sc, "Energia inicial (10 a 100): ", 10, 100);
                    if (pedirConfirmacion(sc, "Confirma agregar " + tipo + " con energia " + energia + "? (s/n): ")) {
                        eco.agregarEntidad(tipo, energia);
                    } else {
                        System.out.println("Accion cancelada.");
                    }
                } else {
                    if (pedirConfirmacion(sc, "Confirma agregar " + tipo + "? (s/n): ")) {
                        eco.agregarEntidad(tipo);         
                    } else {
                        System.out.println("Accion cancelada.");
                    }
                }
                break;

            case 3:
                System.out.println("Se avanza sin intervenir.");
                break;
        }
    }
}
