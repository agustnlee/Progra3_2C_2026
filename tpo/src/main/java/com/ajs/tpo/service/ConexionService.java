package com.ajs.tpo.service;

import java.util.ArrayList;
import java.util.List;
import com.ajs.tpo.model.GrafoUsuarios;
import com.ajs.tpo.model.Conexion;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.repository.UsuarioRepository;

// Gestion de conexiones entre usuarios que viven dentro de GrafosUsuarios y extraidos por id

public class ConexionService {

    private final GrafoUsuarios grafo;
    private final UsuarioRepository usuarios;

    public ConexionService(GrafoUsuarios grafo, UsuarioRepository usuarios) {
        this.grafo = grafo;
        this.usuarios = usuarios;
    }

    // creacion conexion dirigida origen -> destino con tiempo positivo en minutos
    public Conexion crearConexion(int origenId, int destinoId, int tiempo) {
        return grafo.agregarConexion(obtener(origenId), obtener(destinoId), tiempo);
    }

    // habilita una conexion para que los algoritmos de camino puedan usarla
    public void habilitarConexion(int origenId, int destinoId) {
        cambiarEstado(origenId, destinoId, true);
    }

    // deshabilita una conexion (queda en el grafo, pero los algoritmos la ignoran)
    public void deshabilitarConexion(int origenId, int destinoId) {
        cambiarEstado(origenId, destinoId, false);
    }

    public void eliminarConexion(int origenId, int destinoId) {
        if (!grafo.eliminarConexion(obtener(origenId), obtener(destinoId))) {
            throw new IllegalArgumentException("No existe esa conexion");
        }
    }

    //  conexiones que salen del usuario (incluye las deshabilitadas)
    public List<Conexion> consultarConexiones(int usuarioId) {
        return new ArrayList<>(grafo.getConexiones(obtener(usuarioId)));
    }

    private void cambiarEstado(int origenId, int destinoId, boolean habilitada) {
        if (!grafo.setHabilitada(obtener(origenId), obtener(destinoId), habilitada)) {
            throw new IllegalArgumentException("No existe esa conexion");
        }
    }

    private Usuario obtener(int id) {
        return usuarios.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el usuario con id " + id));
    }
}