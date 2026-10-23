package com.ajs.tpo.algorithm;


import com.ajs.tpo.model.Conexion;
import com.ajs.tpo.model.GrafoUsuarios;
import com.ajs.tpo.model.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// Algoritmo Floyd para calcular caminos minimos entre todos los pares de usuarios en la red, considerando solo conexiones habilitadas

public final class FloydDifusion {

    public static final long INFINITO = Long.MAX_VALUE / 4;

    private FloydDifusion() {}

    // Calcula distancia entre todos los pares
    public static Resultado ejecutar(GrafoUsuarios grafo) {
        if (grafo == null) {
            throw new IllegalArgumentException(
                    "El grafo no puede ser nulo."
            );
        }

        List<Usuario> usuarios = new ArrayList<>(grafo.getUsuarios());
        int n = usuarios.size();

        Map<Usuario, Integer> indices = new HashMap<>();

        for (int i = 0; i < n; i++) {
            indices.put(usuarios.get(i), i);
        }

        long[][] distancias = new long[n][n];
        int[][] siguiente = new int[n][n];

        // inicializacion de matrices
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distancias[i][j] = (i == j) ? 0 : INFINITO;
                siguiente[i][j] = (i == j) ? i : -1;
            }
        }

        // cargar conecciones directas habilitadas
        for (int i = 0; i < n; i++) {
            Usuario origen = usuarios.get(i);

            for (Conexion conexion :
                    grafo.getConexionesHabilitadas(origen)) {

                Integer j = indices.get(conexion.getDestino());

                if (j == null) {
                    continue;
                }

                long tiempo = conexion.getTiempo();

                // Conserva el menor peso directo si hubiera multiples conexiones equivalentes
                if (tiempo < distancias[i][j]) {
                    distancias[i][j] = tiempo;
                    siguiente[i][j] = j;
                }
            }
        }

        // cada usuario k se considera
        // como posible intermediario entre i y j.
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {

                if (distancias[i][k] == INFINITO) {
                    continue;
                }

                for (int j = 0; j < n; j++) {

                    if (distancias[k][j] == INFINITO) {
                        continue;
                    }

                    long candidato =
                            distancias[i][k] + distancias[k][j];

                    if (candidato < distancias[i][j]) {
                        distancias[i][j] = candidato;
                        siguiente[i][j] = siguiente[i][k];
                    }
                }
            }
        }

        return new Resultado(
                usuarios,
                indices,
                distancias,
                siguiente
        );
    }

    // Resultado inmutable estructualmente, expone consultas y no las matrices internas
  
    public static final class Resultado {

        private final List<Usuario> usuarios;
        private final Map<Usuario, Integer> indices;
        private final long[][] distancias;
        private final int[][] siguiente;

        private Resultado( List<Usuario> usuarios, Map<Usuario, Integer> indices, long[][] distancias, int[][] siguiente ) {
            this.usuarios = Collections.unmodifiableList(
                    new ArrayList<>(usuarios)
            );
            this.indices = Collections.unmodifiableMap(
                    new HashMap<>(indices)
            );
            this.distancias = distancias;
            this.siguiente = siguiente;
        }

        public List<Usuario> getUsuarios() {
            return usuarios;
        }

        public boolean esAlcanzable( Usuario origen, Usuario destino ) {
            int i = indiceDe(origen);
            int j = indiceDe(destino);

            return distancias[i][j] != INFINITO;
        }

        // Devuelve INFINITO si inalcanzable
        public long getTiempo( Usuario origen, Usuario destino ) {
            int i = indiceDe(origen);
            int j = indiceDe(destino);

            return distancias[i][j];
        }

        // Reconstruye el camino minimo, incluido origen y destino, lista vacia si no existe
     
        public List<Usuario> reconstruirCamino( Usuario origen, Usuario destino ) {
            int i = indiceDe(origen);
            int j = indiceDe(destino);

            if (distancias[i][j] == INFINITO) {
                return Collections.emptyList();
            }

            List<Usuario> camino = new ArrayList<>();
            camino.add(usuarios.get(i));

            if (i == j) {
                return Collections.unmodifiableList(camino);
            }

            int actual = i;
            int pasos = 0;

            while (actual != j) {
                actual = siguiente[actual][j];

                if (actual < 0 || actual >= usuarios.size()) {
                    throw new IllegalStateException(
                            "No se pudo reconstruir el camino."
                    );
                }

                camino.add(usuarios.get(actual));
                pasos++;

                // Proteccion ante una matriz de sucesores inconsistente.
                if (pasos > usuarios.size()) {
                    throw new IllegalStateException(
                            "Se detecto un ciclo al reconstruir el camino."
                    );
                }
            }

            return Collections.unmodifiableList(camino);
        }

        private int indiceDe(Usuario usuario) {
            Objects.requireNonNull(
                    usuario,
                    "El usuario no puede ser nulo."
            );

            Integer indice = indices.get(usuario);

            if (indice == null) {
                throw new IllegalArgumentException(
                        "El usuario no pertenece al grafo: "
                                + usuario.getId()
                );
            }

            return indice;
        }
    }
}
