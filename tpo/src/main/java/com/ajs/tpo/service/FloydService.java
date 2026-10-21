
package com.ajs.tpo.service;

import com.ajs.tpo.algorithm.FloydDifusion;
import com.ajs.tpo.model.GrafoUsuarios;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.repository.UsuarioRepository;

import java.util.List;

// Servicio para consultar caminos minimos entre pares de usuarios
// Conserva el resultado precalculado mientras la version del grafo no cambie

public class FloydService {

    private final GrafoUsuarios grafo;
    private final UsuarioRepository usuarioRepository;

    private FloydDifusion.Resultado resultadoCacheado;
    private long versionCalculada = Long.MIN_VALUE;

    public FloydService( GrafoUsuarios grafo, UsuarioRepository usuarioRepository ) {
        if (grafo == null || usuarioRepository == null) {
            throw new IllegalArgumentException(
                    "El grafo y el repositorio son obligatorios."
            );
        }

        this.grafo = grafo;
        this.usuarioRepository = usuarioRepository;
    }

    // Fuerza un nuevo calculo de todos los pares.
     
    public synchronized void precalcular() {
        resultadoCacheado = FloydDifusion.ejecutar(grafo);
        versionCalculada = grafo.getVersion();
    }

    // Devuelve el resultado completo.
    // Si la red cambio desde el ultimo calculo, lo actualiza.
    
    public synchronized FloydDifusion.Resultado
    obtenerResultados() {
        asegurarActualizado();
        return resultadoCacheado;
    }

    public synchronized boolean esAlcanzable( int origenId, int destinoId ) {
        Usuario origen = obtenerUsuario(origenId);
        Usuario destino = obtenerUsuario(destinoId);

        asegurarActualizado();

        return resultadoCacheado.esAlcanzable(origen, destino);
    }

    public synchronized long consultarTiempoMinimo( int origenId, int destinoId ) {
        Usuario origen = obtenerUsuario(origenId);
        Usuario destino = obtenerUsuario(destinoId);

        asegurarActualizado();

        return resultadoCacheado.getTiempo(origen, destino);
    }

    public synchronized List<Usuario> consultarCaminoMinimo( int origenId, int destinoId ) {
        Usuario origen = obtenerUsuario(origenId);
        Usuario destino = obtenerUsuario(destinoId);

        asegurarActualizado();

        return resultadoCacheado.reconstruirCamino(
                origen,
                destino
        );
    }

    private void asegurarActualizado() {
        if (resultadoCacheado == null
                || versionCalculada != grafo.getVersion()) {
            precalcular();
        }
    }

    private Usuario obtenerUsuario(int id) {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe un usuario con ID " + id
                ));
    }
}
