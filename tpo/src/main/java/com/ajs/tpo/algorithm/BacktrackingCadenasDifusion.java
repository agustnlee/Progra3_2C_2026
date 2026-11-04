package com.ajs.tpo.algorithm;

import com.ajs.tpo.model.Conexion;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.model.GrafoUsuarios;

import java.util.*;

public class BacktrackingCadenasDifusion {

    public static final int MAX_PASOS = 200;

    public static class Cadena {

        private final List<Usuario> usuarios;
        private final int tiempoTotal;

        private Cadena(List<Usuario> usuarios, int tiempoTotal) {
            this.usuarios = Collections.unmodifiableList(new ArrayList<>(usuarios));
            this.tiempoTotal = tiempoTotal;
        }

        public List<Usuario> getUsuarios() {
            return usuarios;
        }
        public int getTiempoTotal() {
            return tiempoTotal;
        }

        @Override
        public String toString() {
            StringBuilder cadena = new StringBuilder();

            for (Usuario usuario : usuarios) {
                if (cadena.length() > 0) {
                    cadena.append(" -> ");
                }

                cadena.append(usuario.getNombre());
            }

            return cadena + " (" + tiempoTotal + " min)";
        }
    }
    // Resultado completo de la busqueda.
    public static class Resultado {

        private final List<Cadena> cadenas;
        private final Cadena cadenaMenorTiempo;
        private final List<String> pasos;

        private Resultado(
                List<Cadena> cadenas,
                Cadena cadenaMenorTiempo,
                List<String> pasos) {

            this.cadenas = Collections.unmodifiableList(
                    new ArrayList<>(cadenas)
            );

            this.cadenaMenorTiempo = cadenaMenorTiempo;

            this.pasos = Collections.unmodifiableList(
                    new ArrayList<>(pasos)
            );
        }

        public List<Cadena> getCadenas() {
            return cadenas;
        }

        public boolean hayCadenas() {
            return !cadenas.isEmpty();
        }

        public Optional<Cadena> getCadenaMenorTiempo() {
            return Optional.ofNullable(cadenaMenorTiempo);
        }

        public List<String> getPasos() {
            return pasos;
        }
    }

    // Busqueda sin usuarios excluidos
    public static Resultado ejecutar(
            GrafoUsuarios grafo,
            Usuario origen,
            Usuario destino,
            int tiempoMaximo,
            int maxIntermediarios) {

        return ejecutar(
                grafo,
                origen,
                destino,
                tiempoMaximo,
                maxIntermediarios,
                Collections.emptySet()
        );
    }

    // Busqueda principal
    public static Resultado ejecutar(
            GrafoUsuarios grafo,
            Usuario origen,
            Usuario destino,
            int tiempoMaximo,
            int maxIntermediarios,
            Set<Usuario> usuariosExcluidos) {

        validarEntrada(
                grafo,
                origen,
                destino,
                tiempoMaximo,
                maxIntermediarios
        );

        Set<Usuario> excluidos = usuariosExcluidos == null
                ? Collections.emptySet()
                : new HashSet<>(usuariosExcluidos);

        if (excluidos.contains(null)) {
            throw new IllegalArgumentException(
                    "El conjunto de usuarios excluidos no puede contener nulos"
            );
        }

        for (Usuario excluido : excluidos) {
            if (!grafo.contieneUsuario(excluido)) {
                throw new IllegalArgumentException(
                        "El usuario excluido " + excluido.getId()
                                + " no pertenece a la red"
                );
            }
        }

        List<Cadena> cadenas = new ArrayList<>();
        List<String> pasos = new ArrayList<>();

        List<Usuario> caminoActual = new ArrayList<>();
        Set<Usuario> visitados = new HashSet<>();

        Cadena[] menorTiempo = new Cadena[1];

        if (excluidos.contains(origen) || excluidos.contains(destino)) {

            registrar(
                    pasos,
                    "No hay cadenas: el origen o el destino esta excluido"
            );

            return new Resultado(cadenas, null, pasos);
        }

        caminoActual.add(origen);
        visitados.add(origen);

        registrar(
                pasos,
                "Inicio DFS desde " + origen.getNombre()
                        + " hacia " + destino.getNombre()
        );

        backtracking(
                grafo,
                origen,
                destino,
                tiempoMaximo,
                maxIntermediarios,
                excluidos,
                caminoActual,
                visitados,
                0,
                cadenas,
                menorTiempo,
                pasos
        );

        if (cadenas.isEmpty()) {
            registrar(
                    pasos,
                    "No se encontraron cadenas que cumplan todas las restricciones"
            );
        }

        return new Resultado(
                cadenas,
                menorTiempo[0],
                pasos
        );
    }

    // Metodo recursivo que realiza el recorrido DFS.
    private static void backtracking(
            GrafoUsuarios grafo,
            Usuario actual,
            Usuario destino,
            int tiempoMaximo,
            int maxIntermediarios,
            Set<Usuario> excluidos,
            List<Usuario> caminoActual,
            Set<Usuario> visitados,
            int tiempoAcumulado,
            List<Cadena> cadenas,
            Cadena[] menorTiempo,
            List<String> pasos) {

        // CASO BASE:
        // Llegamos al destino, por lo tanto encontramos una solucion.
        if (actual.equals(destino)) {

            Cadena cadena = new Cadena(
                    caminoActual,
                    tiempoAcumulado
            );

            cadenas.add(cadena);

            registrar(pasos, "Cadena valida: " + cadena);

            if (menorTiempo[0] == null
                    || tiempoAcumulado < menorTiempo[0].getTiempoTotal()) {

                menorTiempo[0] = cadena;
            }

            return;
        }

        for (Conexion conexion : grafo.getConexionesHabilitadas(actual)) {

            Usuario siguiente = conexion.getDestino();

            // Poda 1: no podemos utilizar usuarios excluidos, ni repetir usuarios dentro de una cadena
            if (excluidos.contains(siguiente)
                    || visitados.contains(siguiente)) {
                continue;
            }

            // Poda 2: controlamos la cantidad de intermediarios
            if (!siguiente.equals(destino)
                    && caminoActual.size() - 1 >= maxIntermediarios) {
                continue;
            }

            // Poda 3: no podemos superar el tiempo maximo
            if (conexion.getTiempo() > tiempoMaximo - tiempoAcumulado) {
                continue;
            }

            int nuevoTiempo = tiempoAcumulado + conexion.getTiempo();

            // ELEGIR: Agregamos el siguiente usuario a la cadena.
            caminoActual.add(siguiente);
            visitados.add(siguiente);
            registrar(
                    pasos,
                    "Avanza a " + siguiente.getNombre()
                            + " (" + nuevoTiempo + " min acumulados)"
            );

            // AVANZAR: Continuamos recursivamente desde el nuevo usuario.
            backtracking(
                    grafo,
                    siguiente,
                    destino,
                    tiempoMaximo,
                    maxIntermediarios,
                    excluidos,
                    caminoActual,
                    visitados,
                    nuevoTiempo,
                    cadenas,
                    menorTiempo,
                    pasos
            );

            // DESHACER: Quitamos la ultima eleccion para probar otra alternativa.
            visitados.remove(siguiente);

            caminoActual.remove(caminoActual.size() - 1);
            registrar(
                    pasos,
                    "Retrocede desde " + siguiente.getNombre()
                            + " para probar otra alternativa");
        }
    }

    private static void validarEntrada(
            GrafoUsuarios grafo,
            Usuario origen,
            Usuario destino,
            int tiempoMaximo,
            int maxIntermediarios) {

        if (grafo == null || origen == null || destino == null) {
            throw new IllegalArgumentException(
                    "El grafo, el origen y el destino son obligatorios"
            );
        }

        if (!grafo.contieneUsuario(origen)) {
            throw new IllegalArgumentException(
                    "El usuario de origen no pertenece a la red"
            );
        }

        if (!grafo.contieneUsuario(destino)) {
            throw new IllegalArgumentException(
                    "El usuario de destino no pertenece a la red"
            );
        }

        if (tiempoMaximo < 0) {
            throw new IllegalArgumentException(
                    "El tiempo maximo no puede ser negativo"
            );
        }

        if (maxIntermediarios < 0) {
            throw new IllegalArgumentException(
                    "El limite de intermediarios no puede ser negativo"
            );
        }
    }

    private static void registrar(
            List<String> pasos,
            String paso) {

        if (pasos.size() < MAX_PASOS) {
            pasos.add(paso);
        }
    }
}
