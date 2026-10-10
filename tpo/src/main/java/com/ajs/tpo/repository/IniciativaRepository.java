package com.ajs.tpo.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ajs.tpo.model.Iniciativa;


// Almacena en memoria las instancias de Iniciativa, indexadas por id unico. No contiene logica de negocio
public class IniciativaRepository {

    private final Map<Integer, Iniciativa> iniciativas = new LinkedHashMap<>();
    private int siguienteId = 1; // TODO: POdira ser static

    public int generarId() {
        while (iniciativas.containsKey(siguienteId)) {
            siguienteId++;
        }
        return siguienteId++;
    }

    // Alta. Falla si ya existe un elemento con ese id
    public void agregar(Iniciativa iniciativa) {
        if (iniciativa == null) throw new IllegalArgumentException("Iniciativa nulo");
        if (iniciativas.containsKey(iniciativa.getId())) {
            throw new IllegalArgumentException("Ya existe iniciativa con id " + iniciativa.getId());
        }
        iniciativas.put(iniciativa.getId(), iniciativa);
    }

    public Optional<Iniciativa> buscarPorId(int id) {
        return Optional.ofNullable(iniciativas.get(id));
    }

    public boolean existe(int id) {
        return iniciativas.containsKey(id);
    }

    // Baja. Devuelve true si existia
    public boolean eliminar(int id) {
        return iniciativas.remove(id) != null;
    }

    // Copia de la lista, en orden de alta
    public List<Iniciativa> listar() {
        return new ArrayList<>(iniciativas.values());
    }

    public int cantidad() {
        return iniciativas.size();
    }
}