package com.ajs.tpo.service;

import com.ajs.tpo.algorithm.DijkstraDifusion;
import com.ajs.tpo.model.GrafoUsuarios;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.repository.UsuarioRepository;

// Busqueda los usuarios por id, ejecuta Dijkstra sobre el grafo y devuelve el Resultado del algoritmo.
public class DijkstraService {

    private final GrafoUsuarios grafo;
    private final UsuarioRepository usuarios;

    public DijkstraService(GrafoUsuarios grafo, UsuarioRepository usuarios) {
        this.grafo = grafo;
        this.usuarios = usuarios;
    }

    // Aplicacion Dijkstra
    // tira IllegalArgumentException si el origen o el destino no existen.
     
    public DijkstraDifusion.Resultado buscarCaminoMasRapido(int origenId, int destinoId) {
        Usuario origen = obtener(origenId);
        Usuario destino = obtener(destinoId);
        return DijkstraDifusion.ejecutar(grafo, origen, destino);
    }

    private Usuario obtener(int id) {
        return usuarios.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el usuario con id " + id));
    }
}