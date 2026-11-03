package com.ajs.tpo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Condiciones que debe cumplir un equipo: tamanio, habilidades a cubrir, integrantes obligatorios
// y pares de usuarios incompatibles (si A es incompatible con B, B lo es con A)

public class RestriccionesEquipo {

    private final int tamanio;
    private final Set<String> habilidadesRequeridas = new LinkedHashSet<>();
    private final Set<Usuario> obligatorios = new LinkedHashSet<>();
    private final Map<Usuario, Set<Usuario>> incompatibles = new HashMap<>();

    public RestriccionesEquipo(int tamanio) {
        if (tamanio < 1) throw new IllegalArgumentException("El tamanio del equipo debe ser al menos 1");
        this.tamanio = tamanio;
    }

    public void agregarHabilidadRequerida(String habilidad) {
        if (habilidad == null || habilidad.isBlank())
            throw new IllegalArgumentException("La habilidad no puede estar vacia");
        habilidadesRequeridas.add(habilidad.trim().toLowerCase()); // normalizacion 
    }

    public void agregarObligatorio(Usuario u) {
        if (u == null) throw new IllegalArgumentException("Usuario nulo");
        if (!obligatorios.contains(u) && obligatorios.size() >= tamanio)
            throw new IllegalArgumentException("Hay mas integrantes obligatorios que el tamanio del equipo");
        obligatorios.add(u);
    }

    public void agregarIncompatibilidad(Usuario a, Usuario b) {
        if (a == null || b == null) throw new IllegalArgumentException("Usuario nulo");
        if (a.equals(b)) throw new IllegalArgumentException("Un usuario no puede ser incompatible consigo mismo");
        incompatibles.computeIfAbsent(a, k -> new HashSet<>()).add(b);
        incompatibles.computeIfAbsent(b, k -> new HashSet<>()).add(a);
    }

    public boolean sonIncompatibles(Usuario a, Usuario b) {
        return incompatibles.getOrDefault(a, Collections.emptySet()).contains(b);
    }

    // Pares {a0, b0, a1, b1, ...}, cada incompatibilidad una sola vez
    public Usuario[] getIncompatiblesComoPares() {
        List<Usuario> pares = new ArrayList<>();
        for (Map.Entry<Usuario, Set<Usuario>> e : incompatibles.entrySet()) {
            for (Usuario b : e.getValue()) {
                if (e.getKey().getId() < b.getId()) {
                    pares.add(e.getKey());
                    pares.add(b);
                }
            }
        }
        return pares.toArray(new Usuario[0]);
    }

    public int getTamanio() { return tamanio; }
    public Set<String> getHabilidadesRequeridas() { return Collections.unmodifiableSet(habilidadesRequeridas); }
    public Set<Usuario> getObligatorios() { return Collections.unmodifiableSet(obligatorios); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Restricciones{tamanio=" + tamanio);
        sb.append(", habilidades=").append(habilidadesRequeridas);
        sb.append(", obligatorios=[");
        boolean primero = true;
        for (Usuario u : obligatorios) {
            if (!primero) sb.append(", ");
            sb.append(u.getNombre());
            primero = false;
        }
        sb.append("], incompatibles=[");

        Usuario[] pares = getIncompatiblesComoPares();
        for (int i = 0; i < pares.length; i += 2) {
            if (i > 0) sb.append(", ");
            sb.append(pares[i].getNombre()).append("-").append(pares[i + 1].getNombre());
        }
        return sb.append("]}").toString();
    }
}