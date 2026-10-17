package com.ajs.tpo.algorithm;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import com.ajs.tpo.model.Conexion;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.model.GrafoUsuarios;
 
// Cadena de Difusion Más rapida con Dijkstra, Greedy

public class DijkstraDifusion {
 
    public static final int INFINITO = Integer.MAX_VALUE;
 
    // Resultado de una ejecucion: distancias, predecesores y pasos de decisiones tomadas
    public static class Resultado {
        private final Usuario origen;
        private final Map<Usuario, Integer> distancias;
        private final Map<Usuario, Usuario> predecesores;
        private final List<String> pasos;
 
        Resultado(Usuario origen, Map<Usuario, Integer> distancias,
                  Map<Usuario, Usuario> predecesores, List<String> pasos) {
            this.origen = origen;
            this.distancias = distancias;
            this.predecesores = predecesores;
            this.pasos = pasos;
        }
 
        public boolean esAlcanzable(Usuario destino) {
            return distancias.containsKey(destino);
        }
 
        // Tiempo minimo en minutos, o infinito si no es alcanzable 
        public int getTiempo(Usuario destino) {
            return distancias.getOrDefault(destino, INFINITO);
        }
 
        // Cadena origen -> ... -> Destino,  Lista vacia si no es alcanzable
        public List<Usuario> reconstruirCamino(Usuario destino) {
            if (!esAlcanzable(destino)) return Collections.emptyList();
            List<Usuario> camino = new ArrayList<>();
            for (Usuario u = destino; u != null; u = predecesores.get(u)) camino.add(u);
            Collections.reverse(camino);
            return camino;
        }
 
        // Decisiones principales tomadas en el calculo
        public List<String> getPasos() {
            return Collections.unmodifiableList(pasos);
        }
 
        public Usuario getOrigen() {
            return origen;
        }
    }
 
    private static class Entrada {
        final Usuario usuario;
        final int tiempo;
        Entrada(Usuario usuario, int tiempo) {
            this.usuario = usuario;
            this.tiempo = tiempo;
        }
    }
 
    // Calcula los caminos minimos desde el origen hacia todos los usuarios alcanzables
    public static Resultado ejecutar(GrafoUsuarios grafo, Usuario origen) {
        return ejecutar(grafo, origen, null);
    }
 
    // lo mismo que el anterior, pero se detiene apenas se fija el destino (destino != null)
    public static Resultado ejecutar(GrafoUsuarios grafo, Usuario origen, Usuario destino) {
        if (grafo == null || origen == null || !grafo.contieneUsuario(origen)) {
            throw new IllegalArgumentException("El usuario de origen no pertenece a la red");
        }
        if (destino != null && !grafo.contieneUsuario(destino)) {
            throw new IllegalArgumentException("El usuario de destino no pertenece a la red");
        }
 
        Map<Usuario, Integer> dist = new HashMap<>();
        Map<Usuario, Usuario> pred = new HashMap<>();
        Set<Usuario> fijados = new HashSet<>();
        List<String> pasos = new ArrayList<>();
 
        PriorityQueue<Entrada> cola = new PriorityQueue<>((a, b) -> {
            int c = Integer.compare(a.tiempo, b.tiempo);
            return c != 0 ? c : Integer.compare(a.usuario.getId(), b.usuario.getId());
        });
 
        dist.put(origen, 0);
        cola.add(new Entrada(origen, 0));
        pasos.add("Inicio: " + origen.getNombre() + " con tiempo 0");
 
        while (!cola.isEmpty()) {
            Entrada actual = cola.poll();
            Usuario u = actual.usuario;
            if (fijados.contains(u)) continue; // entrada vieja (ya habia una merjo)
 
            fijados.add(u);
            pasos.add("Se fija " + u.getNombre() + " con tiempo " + actual.tiempo
                    + " (menor entre los pendientes)");
 
            if (u.equals(destino)) {
                pasos.add("Se alcanzo el destino " + u.getNombre() + ": fin anticipado");
                break;
            }
 
            for (Conexion c : grafo.getConexionesHabilitadas(u)) {
                Usuario v = c.getDestino();
                if (fijados.contains(v)) continue;
 
                int candidato = actual.tiempo + c.getTiempo();
                int conocido = dist.getOrDefault(v, INFINITO);
                if (candidato < conocido) {
                    dist.put(v, candidato);
                    pred.put(v, u);
                    cola.add(new Entrada(v, candidato));
                    pasos.add("  Mejora: " + v.getNombre() + " via " + u.getNombre() + " = " + candidato
                            + (conocido == INFINITO ? " (antes inalcanzado)" : " (antes " + conocido + ")"));
                } else {
                    pasos.add("  Descarta: " + v.getNombre() + " via " + u.getNombre() + " = " + candidato
                            + " (no mejora " + conocido + ")");
                }
            }
        }
 
        // Con fin anticipado quedan distancias provisorias de usuarios no fijados:
        // se conservan solo las definitivas para no informar tiempos que no son minimos
        dist.keySet().retainAll(fijados);
        return new Resultado(origen, dist, pred, pasos);
    }
}
 
