package com.ajs.tpo.service;

import com.ajs.tpo.algorithm.BacktrackingCadenasDifusion;
import com.ajs.tpo.model.GrafoUsuarios;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.repository.UsuarioRepository;

import java.util.HashSet;
import java.util.Set;

public class CadenasAlternativasService {
    private final GrafoUsuarios grafo;
    private final UsuarioRepository usuarios;

    public CadenasAlternativasService(
            GrafoUsuarios grafo,
            UsuarioRepository usuarios) {

        if (grafo == null || usuarios == null) {
            throw new IllegalArgumentException("El grafo y el repositorio de usuarios son obligatorios");
        }

        this.grafo = grafo;
        this.usuarios = usuarios;
    }

    public BacktrackingCadenasDifusion.Resultado buscarCadenasAlternativas(
            int origenId,
            int destinoId,
            int tiempoMaximo,
            int maxIntermediarios,
            Set<Integer> usuariosExcluidosIds) {

        Usuario origen = obtenerUsuario(origenId);
        Usuario destino = obtenerUsuario(destinoId);

        Set<Usuario> excluidos = new HashSet<>();

        if (usuariosExcluidosIds != null) {

            for (Integer id : usuariosExcluidosIds) {

                if (id == null) {
                    throw new IllegalArgumentException("El conjunto de usuarios excluidos no puede contener IDs nulos");
                }

                excluidos.add(obtenerUsuario(id));
            }
        }

        return BacktrackingCadenasDifusion.ejecutar(
                grafo,
                origen,
                destino,
                tiempoMaximo,
                maxIntermediarios,
                excluidos
        );
    }

    public BacktrackingCadenasDifusion.Resultado buscarCadenasAlternativas(
            int origenId,
            int destinoId,
            int tiempoMaximo,
            int maxIntermediarios) {

        return buscarCadenasAlternativas(
                origenId,
                destinoId,
                tiempoMaximo,
                maxIntermediarios,
                Set.of()
        );
    }

    private Usuario obtenerUsuario(int id) {

        Usuario usuario = usuarios.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("No existe el usuario con ID " + id));

        if (!grafo.contieneUsuario(usuario)) {
            throw new IllegalArgumentException("El usuario con ID " + id + " no pertenece a la red de difusion");
        }

        return usuario;
    }
}
