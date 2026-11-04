package com.ajs.tpo.ui;

import java.util.List;

import com.ajs.tpo.algorithm.DijkstraDifusion;
import com.ajs.tpo.algorithm.FloydDifusion;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.service.DijkstraService;
import com.ajs.tpo.service.FloydService;

public class DifusionCLI implements ModuloCLI {

    private final Consola consola;
    private final DijkstraService dijkstra;
    private final FloydService floyd;

    public DifusionCLI(Consola consola, DijkstraService dijkstra, FloydService floyd) {
        this.consola = consola;
        this.dijkstra = dijkstra;
        this.floyd = floyd;
    }

    @Override
    public String nombre() { return "Difusion y cadenas"; }

    @Override
    public void ejecutar() {
        int op;
        do {
            consola.titulo("Difusion");
            System.out.println("1. Cadena mas rapida entre dos usuarios"); //DIjkstra
            System.out.println("2. Consultar un par de usuarios"); // Floyd
            System.out.println("3. Tabla de tiempos entre todos los pares"); // Floyd
            System.out.println("4. Cadenas alternativas con restricciones"); // b3
            System.out.println("0. Volver");
            consola.cierre();
            op = consola.leerOpcion("Opcion: ");
            final int opcion = op;
            consola.seguro(() -> accion(opcion));
        } while (op != 0);
    }

    private void accion(int op) {
        switch (op) {
            case 1 -> dijkstra();
            case 2 -> consultarPar();
            case 3 -> tablaCompleta();
            case 4 -> consola.mensaje("Pendiente "); // TODO: Agregar metodo para llamar 
            case 0 -> { }
            default -> consola.mensaje("Opcion invalida.");
        }
    }

    private void dijkstra() {
        int origenId = consola.leerOpcion("Id origen: ");
        int destinoId = consola.leerOpcion("Id destino: ");
        DijkstraDifusion.Resultado r = this.dijkstra.buscarCaminoMasRapido(origenId, destinoId);

        // El destino se obtiene a partir de la cadena; si no es alcanzable no hay cadena
        List<Usuario> camino = null;
        boolean alcanzable = false;
        for (Usuario u : r.getDistancias().keySet()) {
            if (u.getId() == destinoId) {
                alcanzable = r.esAlcanzable(u);
                if (alcanzable) camino = r.reconstruirCamino(u);
                if (alcanzable) {
                    consola.titulo("Resultado");
                    System.out.println("Cadena: " + cadena(camino));
                    System.out.println("Usuarios que la integran: " + camino.size());
                    System.out.println("Tiempo total: " + r.getTiempo(u) + " min");
                    consola.cierre();
                }
            }
        }
        if (!alcanzable) {
            consola.titulo("Resultado");
            System.out.println("El destino no es alcanzable desde el origen.");
            consola.cierre();
        }

        consola.titulo("Decisiones principales");
        for (String paso : r.getPasos()) {
            String t = paso.trim();
            if (t.startsWith("Se fija") || t.startsWith("Mejora") || t.startsWith("Se alcanzo")) {
                System.out.println((paso.startsWith("  ") ? "    " : "  ") + t);
            }
        }
        consola.cierre();
    }

    private void consultarPar() {
        int o = consola.leerOpcion("Id origen: ");
        int d = consola.leerOpcion("Id destino: ");
        consola.titulo("Resultado");
        if (!floyd.esAlcanzable(o, d)) {
            System.out.println("No existe cadena de difusion entre esos usuarios.");
        } else {
            System.out.println("Cadena: " + cadena(floyd.consultarCaminoMinimo(o, d)));
            System.out.println("Tiempo minimo: " + floyd.consultarTiempoMinimo(o, d) + " min");
        }
        consola.cierre();
    }

    private void tablaCompleta() {
        FloydDifusion.Resultado r = floyd.obtenerResultados();
        List<String> filas = new java.util.ArrayList<>();
        for (Usuario origen : r.getUsuarios()) {
            StringBuilder sb = new StringBuilder("{" + origen.getId() + " " + origen.getNombre() + "} -> {");
            boolean primero = true;
            for (Usuario destino : r.getUsuarios()) {
                if (origen.equals(destino) || !r.esAlcanzable(origen, destino)) continue;
                if (!primero) sb.append(", ");
                sb.append(destino.getId()).append(" ").append(destino.getNombre())
                  .append(": ").append(r.getTiempo(origen, destino)).append(" min");
                primero = false;
            }
            filas.add(sb.append("}").toString());
        }
        consola.listado("Tiempos minimos de difusion (solo destinos alcanzables)", filas);
    }

    private static String cadena(List<Usuario> camino) {
        StringBuilder sb = new StringBuilder();
        for (Usuario u : camino) {
            if (sb.length() > 0) sb.append(" -> ");
            sb.append(u.getNombre());
        }
        return sb.toString();
    }
}