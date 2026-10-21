package com.ajs.tpo.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

// Resultado de una combinacion valida de usuarios
// Es inmutable y su igualdad depende solo de QUE usuarios lo integran, no del orden (como un ConjuntoTDA), para evitar repeticiones con diferente orden

public class Equipo {

    private final Set<Usuario> integrantes;

    public Equipo(Collection<Usuario> integrantes) {
        if (integrantes == null || integrantes.isEmpty()) {
            throw new IllegalArgumentException("Un equipo necesita al menos un integrante");
        }
        Set<Usuario> ordenados = new TreeSet<>(Comparator.comparingInt(Usuario::getId));
        ordenados.addAll(integrantes);
        this.integrantes = Collections.unmodifiableSet(ordenados);
    }

    public Set<Usuario> getIntegrantes() {
        return integrantes;
    }

    public int getTamanio() {
        return integrantes.size();
    }

    public boolean contiene(Usuario u) {
        return integrantes.contains(u);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Equipo[");
        boolean primero = true;
        for (Usuario u : integrantes) {
            if (!primero) sb.append(", ");
            sb.append(u.getNombre()).append("(#").append(u.getId()).append(")");
            primero = false;
        }
        return sb.append("]").toString();
    }

    
    // metodos para poder trabajar con hashmap si se requiere
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipo)) return false;
        return integrantes.equals(((Equipo) o).integrantes);
    }

    @Override
    public int hashCode() {
        return integrantes.hashCode();
    }

}