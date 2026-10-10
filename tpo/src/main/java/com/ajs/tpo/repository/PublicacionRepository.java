package com.ajs.tpo.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.ajs.tpo.model.Publicacion;

// Almacena en memoria las instancias de Publicacion, indexadas por id unico. No coentiene logica de negocio

public class PublicacionRepository {

    private final Map<Integer, Publicacion> publicaciones = new LinkedHashMap<>();
    private int siguienteId = 1; //TODO: Se podria considerar static

    public int generarId() {
        while (publicaciones.containsKey(siguienteId)) {
            siguienteId++;
        }
        return siguienteId++;
    }

    // Alta. Falla si ya existe un elemento con ese id. 
    public void agregar(Publicacion publicacion) {
        if (publicacion == null) throw new IllegalArgumentException("Publicacion nulo");
        if (publicaciones.containsKey(publicacion.getId())) {
            throw new IllegalArgumentException("Ya existe publicacion con id " + publicacion.getId());
        }
        publicaciones.put(publicacion.getId(), publicacion);
    }

    public Optional<Publicacion> buscarPorId(int id) {
        return Optional.ofNullable(publicaciones.get(id));
    }

    public boolean existe(int id) {
        return publicaciones.containsKey(id);
    }

    // Baja. Devuelve true si existia
    public boolean eliminar(int id) {
        return publicaciones.remove(id) != null;
    }

    // Copia de la lista, en orden de alta
    public List<Publicacion> listar() {
        return new ArrayList<>(publicaciones.values());
    }

    public int cantidad() {
        return publicaciones.size();
    }
}