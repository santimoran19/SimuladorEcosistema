package simuladorecosistema;

import java.util.ArrayList;
import java.util.Random;

public class Ecosistema {

    private ArrayList<Planta> plantas;
    private ArrayList<Conejo> conejos;
    private ArrayList<Lobo> lobos;
    private Clima climaActual;
    private int turnoActual;

    private Random random;
    private int contadorNombres;  
    private int lobosCreados;   
    private int eventosTurno;    

    // Datos para el reporte final
    private int turnoMayorActividad;
    private int maxEventos;
    private int nacimientosPlantas;
    private int nacimientosConejos;
    private int muertesPlantas;
    private int muertesConejos;
    private int muertesLobos;
    private Entidad plantaMasLongeva;
    private Entidad conejoMasLongevo;
    private Entidad loboMasLongevo;
    private Lobo mejorCazador;

    private ArrayList<Integer> historialPlantas;
    private ArrayList<Integer> historialConejos;
    private ArrayList<Integer> historialLobos;

    private String[] nombresPlantas = {"Helecho", "Trebol", "Pasto", "Menta", "Ortiga"};
    private String[] nombresConejos = {"Blas", "Luna", "Topo", "Rex", "Copito", "Tambor"};
    private String[] nombresLobos = {"Fang", "Sombra", "Colmillo", "Aullido", "Niebla"};

    public Ecosistema(Clima climaInicial) {
        plantas = new ArrayList<>();
        conejos = new ArrayList<>();
        lobos = new ArrayList<>();
        climaActual = climaInicial;
        turnoActual = 0;
        random = new Random();
        historialPlantas = new ArrayList<>();
        historialConejos = new ArrayList<>();
        historialLobos = new ArrayList<>();
    }

    public void procesarTurno() {
        turnoActual++;
        eventosTurno = 0;

        System.out.println();
        System.out.println("=== TURNO " + turnoActual + " | Clima: " + climaActual + " ===");
        System.out.println("Plantas: " + plantas.size() + "   Conejos: " + conejos.size() + "   Lobos: " + lobos.size());
        System.out.println("-- Eventos --");

        ArrayList<Reproducible> reproducibles = new ArrayList<>();
        for (Planta p : plantas) {
            reproducibles.add(p);
        }
        for (Conejo c : conejos) {
            reproducibles.add(c);
        }
        for (Reproducible r : reproducibles) {
            r.intentarReproduccion(this);
        }

        for (Conejo c : conejos) {
            if (c.estaVivo()) {
                c.actuar(this);
            }
        }

        for (Lobo l : lobos) {
            if (l.estaVivo()) {
                l.actuar(this);
            }
        }

        for (Planta p : plantas) {
            if (p.isViva()) {
                p.envejecer();
            }
        }
        for (Conejo c : conejos) {
            if (c.estaVivo()) {
                c.envejecer();
            }
        }
        for (Lobo l : lobos) {
            if (l.estaVivo()) {
                l.envejecer();
            }
        }
        aplicarEfectosClima();

        for (Planta p : plantas) {
            if (p.isViva() && p.getEnergia() <= 0) {
                p.setViva(false);
                registrarEvento("Planta '" + p.getNombre() + "' se seco");
            }
        }
        for (Conejo c : conejos) {
            if (c.estaVivo()) {
                c.verificarMuerte();
                if (!c.estaVivo()) {
                    eventosTurno++;
                }
            }
        }
        for (Lobo l : lobos) {
            if (l.estaVivo()) {
                l.verificarMuerte();
                if (!l.estaVivo()) {
                    eventosTurno++;
                }
            }
        }

        limpiarMuertos();

        if (eventosTurno == 0) {
            System.out.println("(no hubo eventos en este turno)");
        }
        System.out.println("Estado: Plantas: " + plantas.size() + "   Conejos: " + conejos.size() + "   Lobos: " + lobos.size());

        if (eventosTurno > maxEventos) {
            maxEventos = eventosTurno;
            turnoMayorActividad = turnoActual;
        }
        historialPlantas.add(plantas.size());
        historialConejos.add(conejos.size());
        historialLobos.add(lobos.size());
    }

    private void aplicarEfectosClima() {
        switch (climaActual) {
            case SOLEADO:
                modificarEnergiaConejos(5);
                break;
            case LLUVIOSO:
                modificarEnergiaConejos(3);
                modificarEnergiaLobos(-5);
                break;
            case SEQUIA:
                modificarEnergiaConejos(-5);
                break;
            case INVIERNO:
                modificarEnergiaConejos(-8);
                break;
        }
    }

    private void modificarEnergiaConejos(double cantidad) {
        for (Conejo c : conejos) {
            if (c.estaVivo()) {
                c.setEnergia(c.getEnergia() + cantidad);
            }
        }
    }

    private void modificarEnergiaLobos(double cantidad) {
        for (Lobo l : lobos) {
            if (l.estaVivo()) {
                l.setEnergia(l.getEnergia() + cantidad);
            }
        }
    }

    private void limpiarMuertos() {
        for (int i = plantas.size() - 1; i >= 0; i--) {
            if (!plantas.get(i).isViva()) {
                revisarRecords(plantas.get(i));
                muertesPlantas++;
                plantas.remove(i);
            }
        }
        for (int i = conejos.size() - 1; i >= 0; i--) {
            if (!conejos.get(i).estaVivo()) {
                revisarRecords(conejos.get(i));
                muertesConejos++;
                conejos.remove(i);
            }
        }
        for (int i = lobos.size() - 1; i >= 0; i--) {
            if (!lobos.get(i).estaVivo()) {
                revisarRecords(lobos.get(i));
                muertesLobos++;
                lobos.remove(i);
            }
        }
    }

    private void revisarRecords(Entidad e) {
        if (e instanceof Planta) {
            if (plantaMasLongeva == null || e.getEdad() > plantaMasLongeva.getEdad()) {
                plantaMasLongeva = e;
            }
        } else if (e instanceof Conejo) {
            if (conejoMasLongevo == null || e.getEdad() > conejoMasLongevo.getEdad()) {
                conejoMasLongevo = e;
            }
        } else if (e instanceof Lobo) {
            if (loboMasLongevo == null || e.getEdad() > loboMasLongevo.getEdad()) {
                loboMasLongevo = e;
            }
            Lobo l = (Lobo) e;
            if (mejorCazador == null || l.getExitosCaza() > mejorCazador.getExitosCaza()) {
                mejorCazador = l;
            }
        }
    }

    public void registrarEvento(String texto) {
        System.out.println(texto);
        eventosTurno++;
    }

    public int aleatorio(int limite) {
        return random.nextInt(limite);   
    }

    public String generarNombre(String tipo) {
        contadorNombres++;
        String base;
        if (tipo.equalsIgnoreCase("planta")) {
            base = nombresPlantas[random.nextInt(nombresPlantas.length)];
        } else if (tipo.equalsIgnoreCase("conejo")) {
            base = nombresConejos[random.nextInt(nombresConejos.length)];
        } else {
            base = nombresLobos[random.nextInt(nombresLobos.length)];
        }
        return base + "-" + contadorNombres;
    }

    public void agregarNacimiento(Planta nueva) {
        plantas.add(nueva);
        nacimientosPlantas++;
    }

    public void agregarNacimiento(Conejo nuevo) {
        conejos.add(nuevo);
        nacimientosConejos++;
    }

    public void mostrarEstado() {
        System.out.println("Clima: " + climaActual + " | Plantas: " + plantas.size()
                + "   Conejos: " + conejos.size() + "   Lobos: " + lobos.size());
    }

    public void agregarEntidad(String tipo) {
        double energia = 0;
        if (tipo.equalsIgnoreCase("planta")) {
            energia = random.nextInt(31) + 30;  
        } else if (tipo.equalsIgnoreCase("conejo")) {
            energia = random.nextInt(31) + 40;  
        } else if (tipo.equalsIgnoreCase("lobo")) {
            energia = random.nextInt(31) + 50;  
        }
        agregarEntidad(tipo, energia);
    }


    public void agregarEntidad(String tipo, double energia) {
        if (tipo.equalsIgnoreCase("planta")) {
            Planta nueva;

            if (random.nextInt(100) < 20) {
                nueva = new PlantaVenenosa(generarNombre("planta"), energia, random.nextInt(5) + 1);
            } else {
                nueva = new Planta(generarNombre("planta"), energia, random.nextInt(5) + 1);
            }
            plantas.add(nueva);
            System.out.println("Se agrego '" + nueva.getNombre() + "' al ecosistema.");

        } else if (tipo.equalsIgnoreCase("conejo")) {
            Conejo nuevo = new Conejo(generarNombre("conejo"), energia);
            conejos.add(nuevo);
            System.out.println("Se agrego '" + nuevo.getNombre() + "' al ecosistema.");

        } else if (tipo.equalsIgnoreCase("lobo")) {
            if (lobosCreados >= 5) {
                System.out.println("No se pueden agregar mas lobos: el maximo es 5 en toda la simulacion.");
                return;
            }
            Lobo nuevo = new Lobo(generarNombre("lobo"), energia);
            lobos.add(nuevo);
            lobosCreados++;
            System.out.println("Se agrego '" + nuevo.getNombre() + "' al ecosistema.");

        } else {
            System.out.println("Tipo de entidad no valido.");
        }
    }

    public void cambiarClima(Clima nuevo) {
        climaActual = nuevo;
        System.out.println("El clima cambio a " + climaActual + ".");
    }

    public boolean ecosistemaColapsado() {
        return plantas.size() == 0 || conejos.size() == 0 || lobos.size() == 0;
    }

    public void generarReporteFinal() {
        System.out.println();
        System.out.println("==================== REPORTE FINAL ====================");

        if (ecosistemaColapsado()) {
            System.out.println("Causa de fin: COLAPSO del ecosistema en el turno " + turnoActual);
            if (plantas.size() == 0) {
                System.out.println("  - Se extinguieron las plantas");
            }
            if (conejos.size() == 0) {
                System.out.println("  - Se extinguieron los conejos");
            }
            if (lobos.size() == 0) {
                System.out.println("  - Se extinguieron los lobos");
            }
        } else {
            System.out.println("Causa de fin: se completaron los " + turnoActual + " turnos configurados");
        }

        System.out.println();
        System.out.println("Turno de mayor actividad: turno " + turnoMayorActividad + " (" + maxEventos + " eventos)");

        for (Planta p : plantas) {
            revisarRecords(p);
        }
        for (Conejo c : conejos) {
            revisarRecords(c);
        }
        for (Lobo l : lobos) {
            revisarRecords(l);
        }

        System.out.println();
        System.out.println("Entidades mas longevas:");
        mostrarLongevo("Planta", plantaMasLongeva);
        mostrarLongevo("Conejo", conejoMasLongevo);
        mostrarLongevo("Lobo", loboMasLongevo);

        System.out.println();
        if (mejorCazador != null) {
            System.out.println("Lobo con mas cacerias exitosas: '" + mejorCazador.getNombre()
                    + "' (" + mejorCazador.getExitosCaza() + " cacerias)");
        }

        System.out.println();
        System.out.println("Nacimientos y muertes:");
        System.out.println("  Plantas -> nacimientos: " + nacimientosPlantas + " | muertes: " + muertesPlantas);
        System.out.println("  Conejos -> nacimientos: " + nacimientosConejos + " | muertes: " + muertesConejos);
        System.out.println("  Lobos   -> nacimientos: 0 | muertes: " + muertesLobos);

        System.out.println();
        System.out.println("Maximos y minimos de cada poblacion:");
        mostrarMaximoYMinimo("Plantas", historialPlantas);
        mostrarMaximoYMinimo("Conejos", historialConejos);
        mostrarMaximoYMinimo("Lobos", historialLobos);

        System.out.println();
        System.out.println("Sobrevivientes:");
        ArrayList<Entidad> sobrevivientes = new ArrayList<>();
        for (Planta p : plantas) {
            sobrevivientes.add(p);
        }
        for (Conejo c : conejos) {
            sobrevivientes.add(c);
        }
        for (Lobo l : lobos) {
            sobrevivientes.add(l);
        }
        if (sobrevivientes.size() == 0) {
            System.out.println("  No quedo ninguna entidad viva.");
        }
        for (Entidad e : sobrevivientes) {
            e.mostrarEstado();
        }
        System.out.println("=======================================================");
    }

    private void mostrarLongevo(String tipo, Entidad e) {
        if (e == null) {
            System.out.println("  " + tipo + ": no hubo ejemplares");
        } else {
            System.out.println("  " + tipo + ": '" + e.getNombre() + "' con " + e.getEdad() + " turnos de edad");
        }
    }

    private void mostrarMaximoYMinimo(String nombre, ArrayList<Integer> historial) {
        if (historial.size() == 0) {
            return;
        }
        int max = historial.get(0);
        int min = historial.get(0);
        int turnoMax = 1;
        int turnoMin = 1;
        for (int i = 1; i < historial.size(); i++) {
            if (historial.get(i) > max) {
                max = historial.get(i);
                turnoMax = i + 1;
            }
            if (historial.get(i) < min) {
                min = historial.get(i);
                turnoMin = i + 1;
            }
        }
        System.out.println("  " + nombre + ": maximo " + max + " (turno " + turnoMax + ") | minimo "
                + min + " (turno " + turnoMin + ")");
    }

    public ArrayList<Planta> getPlantas() {
        return plantas;
    }

    public ArrayList<Conejo> getConejos() {
        return conejos;
    }

    public ArrayList<Lobo> getLobos() {
        return lobos;
    }

    public Clima getClimaActual() {
        return climaActual;
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    public int getLobosCreados() {
        return lobosCreados;
    }
}
