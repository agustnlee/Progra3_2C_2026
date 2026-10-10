package com.ajs.tpo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import tpo.model.Conexion;
import tpo.model.Usuario;

// Grafo dirigido y ponderado de la red de difusion. LISTA DE ADYACENCIA
// Cada Usuario es un vertice; cada Conexion es una arista origen -> destino. A lo sumo 1 conexion entre par origen destino

public class GrafoUsuarios {

    private final Map<Usuario, List<Conexion>> adyacencia = new LinkedHashMap<>();

    // Se incrementa con cada cambio de estructura para saber si un calculo cacheado esta viejo
    private int version = 0;

    public int getVersion() {
        return version;
    }

    // --- Vertices

    public void agregarUsuario(Usuario u) {
        if (u == null) throw new IllegalArgumentException("Usuario nulo");
        if (!adyacencia.containsKey(u)) {
            adyacencia.put(u, new ArrayList<>());
            version++;
        }
    }

    public boolean contieneUsuario(Usuario u) {
        return adyacencia.containsKey(u);
    }

    // Elimina usuario como todas las conexiones salientes y entrantes
    public void eliminarUsuario(Usuario u) {
        if (adyacencia.remove(u) == null) return;
        version++;
        for (List<Conexion> lista : adyacencia.values()) {
            lista.removeIf(c -> c.getDestino().equals(u));
        }
    }

    // Obtener usuarios ordenados por ID
    public List<Usuario> getUsuarios() {
        List<Usuario> lista = new ArrayList<>(adyacencia.keySet());
        lista.sort(Comparator.comparingInt(Usuario::getId));
        return lista;
    }

    public int cantidadUsuarios() {
        return adyacencia.size();
    }

    // --- Aristas

    // crea conexion y agrega usuarios si no estaban los usuarios en el grafo

    public Conexion agregarConexion(Usuario origen, Usuario destino, int tiempo) {
        Conexion nueva = new Conexion(origen, destino, tiempo); // valida datos
        agregarUsuario(origen);
        agregarUsuario(destino);
        if (buscarConexion(origen, destino).isPresent()) {
            throw new IllegalArgumentException("Ya existe una conexion de "
                    + origen.getNombre() + " a " + destino.getNombre());
        }
        adyacencia.get(origen).add(nueva);
        version++;
        return nueva;
    }

    public Optional<Conexion> buscarConexion(Usuario origen, Usuario destino) {
        List<Conexion> lista = adyacencia.get(origen);
        if (lista == null) return Optional.empty();
        for (Conexion c : lista) {
            if (c.getDestino().equals(destino)) return Optional.of(c);
        }
        return Optional.empty();
    }

    public boolean eliminarConexion(Usuario origen, Usuario destino) {
        List<Conexion> lista = adyacencia.get(origen);
        boolean eliminada = lista != null && lista.removeIf(c -> c.getDestino().equals(destino));
        if (eliminada) version++;
        return eliminada;
    }

    public boolean setHabilitada(Usuario origen, Usuario destino, boolean habilitada) {
        Optional<Conexion> c = buscarConexion(origen, destino);
        if (c.isEmpty()) return false;
        if (habilitada) c.get().habilitar(); else c.get().deshabilitar();
        version++;
        return true;
    }

    // --- consutlas

    //  todas las conexiones que salen del usuario (incluye deshabilitadas)
    public List<Conexion> getConexiones(Usuario u) {
        List<Conexion> lista = adyacencia.get(u);
        return lista == null ? Collections.emptyList() : Collections.unmodifiableList(lista);
    }

    // Solo las habilitadas
    public List<Conexion> getConexionesHabilitadas(Usuario u) {
        List<Conexion> resultado = new ArrayList<>();
        for (Conexion c : getConexiones(u)) {
            if (c.isHabilitada()) resultado.add(c);
        }
        return resultado;
    }

    public int cantidadConexiones() {
        int total = 0;
        for (List<Conexion> lista : adyacencia.values()) total += lista.size();
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Usuario u : getUsuarios()) {
            sb.append(u.getNombre()).append(" -> ").append(adyacencia.get(u)).append("\n");
        }
        return sb.toString();
    }
}