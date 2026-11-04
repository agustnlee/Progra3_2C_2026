package com.ajs.tpo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ajs.tpo.algorithm.BacktrackingSubconjuntosEquipo;
import com.ajs.tpo.model.Equipo;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.repository.EquipoRepository;
import com.ajs.tpo.repository.UsuarioRepository;

// Gestion CRUD de equipos confirmados por el administrador
// Solo valida lo esencial, usuarios existentes, sin repetidos y sin equipos duplicados.
// No exige cumplir restricciones de habilidades ni incompatibilidades, es decision desde el punto de vista del administrador (si no es valido, se denega accion)
// Un mismo usuario puede integrar varios equipos

public class EquipoService {

    private final EquipoRepository equipos;
    private final UsuarioRepository usuarios;

    public EquipoService(EquipoRepository equipos, UsuarioRepository usuarios) {
        this.equipos = equipos;
        this.usuarios = usuarios;
    }

    // Alta manual a partir de ids de usuarios, devuelve el id del equipo creado
    public int crear(Collection<Integer> usuariosIds) {
        if (usuariosIds == null || usuariosIds.isEmpty()) {
            throw new IllegalArgumentException("El equipo necesita al menos un integrante");
        }
        Set<Integer> vistos = new HashSet<>();
        List<Usuario> integrantes = new ArrayList<>();
        for (int id : usuariosIds) {
            if (!vistos.add(id)) throw new IllegalArgumentException("El usuario " + id + " esta repetido");
            integrantes.add(obtenerUsuario(id));
        }
        return guardar(new Equipo(integrantes));
    }

    // Confirma el equipo numero "posicion" (desde 1) de un resultado del algoritmo de BacktrackingSubconjuntoEquipo
    public int crearDesdeResultado(BacktrackingSubconjuntosEquipo.Resultado resultado, int posicion) {
        if (resultado == null) throw new IllegalArgumentException("El resultado no puede ser nulo");
        List<Equipo> validos = resultado.getEquipos();
        if (posicion < 1 || posicion > validos.size()) {
            throw new IllegalArgumentException("La posicion debe estar entre 1 y " + validos.size());
        }
        return guardar(validos.get(posicion - 1));
    }

    public void eliminar(int equipoId) {
        if (!equipos.eliminar(equipoId)) {
            throw new IllegalArgumentException("No existe el equipo con id " + equipoId);
        }
    }

    public Equipo obtener(int equipoId) {
        return equipos.buscarPorId(equipoId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el equipo con id " + equipoId));
    }

    // id de equipo -> equipo, en orden de alta
    public Map<Integer, Equipo> listar() {
        return equipos.listar();
    }

    // Baja de equipos que incluyan a un usuario (se utiliza par ala baja de usuarios)
    // Como un equipo es inmutable y fue formado para un tamanio concreto, eso se elimina completo.
    // Devuelve cuantos equipos se eliminaron
    public int eliminarEquiposDe(Usuario usuario) {
        int eliminados = 0;
        for (Map.Entry<Integer, Equipo> e : equipos.listar().entrySet()) {
            if (e.getValue().contiene(usuario)) {
                equipos.eliminar(e.getKey());
                eliminados++;
            }
        }
        return eliminados;
    }

    private int guardar(Equipo equipo) {
        if (equipos.existe(equipo)) {
            throw new IllegalArgumentException("Ya existe un equipo con esos integrantes");
        }
        return equipos.agregar(equipo);
    }

    private Usuario obtenerUsuario(int id) {
        return usuarios.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el usuario con id " + id));
    }
}