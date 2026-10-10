package com.ajs.tpo.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import tpo.model.Usuario;

// Almacena en memoria las instancias de Usuario, indexadas por id unico.vNo contiene logica de negocio

public class UsuarioRepository {

    private final Map<Integer, Usuario> usuarios = new LinkedHashMap<>();
    private int siguienteId = 1;   // TODO: Se podria considerar pasarlo a static

    // Id autoincremental para altas desde la UI
    public int generarId() {
        while (usuarios.containsKey(siguienteId)) {
            siguienteId++;
        }
        return siguienteId++;
    }

    // Alta. Falla si ya existe un elemento con ese id
    public void agregar(Usuario usuario) {
        if (usuario == null) throw new IllegalArgumentException("Usuario nulo");
        if (usuarios.containsKey(usuario.getId())) {
            throw new IllegalArgumentException("Ya existe usuario con id " + usuario.getId());
        }
        usuarios.put(usuario.getId(), usuario);
    }

    public Optional<Usuario> buscarPorId(int id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    public boolean existe(int id) {
        return usuarios.containsKey(id);
    }

    // Baja. Devuelve true si existia
    public boolean eliminar(int id) {
        return usuarios.remove(id) != null;
    }

    // Copia de la lista, en orden de alta.
    public List<Usuario> listar() {
        return new ArrayList<>(usuarios.values());
    }

    public int cantidad() {
        return usuarios.size();
    }
}