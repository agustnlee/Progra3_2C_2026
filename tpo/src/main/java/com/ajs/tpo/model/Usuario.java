package com.ajs.tpo.model;


import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;


public class Usuario {

    private final int id;
    private String nombre;
    private final Set<String> habilidades; // por ahora se va a tomar habilidades como string

    public Usuario(int id, String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        this.id = id; // TODO: chequeo de id no existente con un metodo de repositorio durante el servicio de creacion de usuario
        this.nombre = nombre.trim();
        this.habilidades = new LinkedHashSet<>();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        this.nombre = nombre.trim();
    }

    // Solo lectura
    public Set<String> getHabilidades() {
        return Collections.unmodifiableSet(habilidades);
    }

    public boolean agregarHabilidad(String habilidad) {
        if (habilidad == null || habilidad.isBlank()) {
            throw new IllegalArgumentException("La habilidad no puede estar vacia");
        }
        return habilidades.add(normalizar(habilidad));
    }

    public boolean quitarHabilidad(String habilidad) {
        return habilidades.remove(normalizar(habilidad));
    }

    public boolean tieneHabilidad(String habilidad) {
        return habilidades.contains(normalizar(habilidad));
    }

    private static String normalizar(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nombre='" + nombre + "', habilidades=" + habilidades + "}";
    }

    
    // metodos para poder trabajar con hashmap si se requiere

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        return id == ((Usuario) o).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}