package com.ajs.tpo.repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.ajs.tpo.model.Equipo;

// Almacena en memoria los equipos confirmados, indexados por un id que asigna el repositorio
// Equipo no tiene id propio, su igualdad depende solo de sus integrantes

public class EquipoRepository {

    private final Map<Integer, Equipo> equipos = new LinkedHashMap<>();
    private int siguienteId = 1;

    // Alta de equipo, devuleve id asignado
    public int agregar(Equipo equipo) {
        if (equipo == null) throw new IllegalArgumentException("Equipo nulo");
        int id = siguienteId++; // el id nunca se reutiliza
        equipos.put(id, equipo);
        return id;
    }

    public Optional<Equipo> buscarPorId(int id) {
        return Optional.ofNullable(equipos.get(id));
    }

    // true si ya hay un equipo con exactamente los mismos integrantes
    public boolean existe(Equipo equipo) {
        return equipos.containsValue(equipo);
    }

    // Baja, Devuelve true si existia
    public boolean eliminar(int id) {
        return equipos.remove(id) != null;
    }

    // Copia, en orden de alta
    public Map<Integer, Equipo> listar() {
        return new LinkedHashMap<>(equipos);
    }

    public int cantidad() {
        return equipos.size();
    }
}