package com.ajs.tpo.service;

import com.ajs.tpo.algorithm.BacktrackingSubconjuntosEquipo;
import com.ajs.tpo.model.RestriccionesEquipo;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.repository.UsuarioRepository;

import java.util.Collection;

// Arma las restricciones a partir de ids ejecuta el backtracking sobre todos los usuarios y devuelve el Resultado

public class FormacionEquiposService {

    private final UsuarioRepository usuarios;

    public FormacionEquiposService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    // incompatibles, cada elemento es un par {idA, idB}
    public BacktrackingSubconjuntosEquipo.Resultado formarEquipos(int tamanio, Collection<String> habilidades, Collection<Integer> obligatoriosIds,
      Collection<int[]> incompatibles) {
        
        RestriccionesEquipo r = new RestriccionesEquipo(tamanio);
        if (habilidades != null) {
            for (String h : habilidades) r.agregarHabilidadRequerida(h);
        }
        if (obligatoriosIds != null) {
            for (int id : obligatoriosIds) r.agregarObligatorio(obtener(id));
        }
        if (incompatibles != null) {
            for (int[] par : incompatibles) {
                if (par.length != 2) throw new IllegalArgumentException("Cada incompatibilidad es un par de ids");
                r.agregarIncompatibilidad(obtener(par[0]), obtener(par[1]));
            }
        }
        return BacktrackingSubconjuntosEquipo.ejecutar(usuarios.listar(), r);
    }

    private Usuario obtener(int id) {
        return usuarios.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el usuario con id " + id));
    }
}