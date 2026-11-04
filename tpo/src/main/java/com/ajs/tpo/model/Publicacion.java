package com.ajs.tpo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Publicacion de un Usuario en el muro

public class Publicacion {

    // Relevancia = PESO_REACCION * reacciones + PESO_COMENTARIO * comentarios
    // Enteros para desempate
    public static final int PESO_REACCION = 1;
    public static final int PESO_COMENTARIO = 3;

    private final int id;
    private final Usuario autor;
    private LocalDateTime fechaHora;
    private int reacciones;

    // Indexados por id de comentario, manteniendo el orden de alta
    private final Map<Integer, Comentario> comentarios = new LinkedHashMap<>();
    private int siguienteIdComentario = 1;

    public Publicacion(int id, Usuario autor, LocalDateTime fechaHora) { // creacion desde CLI
        this(id, autor, fechaHora, 0);
    }

    public Publicacion(int id, Usuario autor, LocalDateTime fechaHora, int reacciones) { // creacion para testing
        if (autor == null) throw new IllegalArgumentException("El autor es obligatorio");
        if (fechaHora == null) throw new IllegalArgumentException("La fecha y hora son obligatorias");
        validarReacciones(reacciones);
        this.id = id;
        this.autor = autor;
        this.fechaHora = fechaHora;
        this.reacciones = reacciones;
    }

    public int getRelevancia() {
        return PESO_REACCION * reacciones + PESO_COMENTARIO * comentarios.size();
    }

    // comentarios

    public Comentario agregarComentario(Usuario autorComentario, String texto, LocalDateTime fechaHora) {
        Comentario c = new Comentario(siguienteIdComentario, autorComentario, texto, fechaHora);
        comentarios.put(c.getId(), c);
        siguienteIdComentario++; // el id nunca se reutiliza, aunque se borren comentarios
        return c;
    }

    public Optional<Comentario> buscarComentario(int comentarioId) {
        return Optional.ofNullable(comentarios.get(comentarioId));
    }

    // Devuelve true si existia
    public boolean eliminarComentario(int comentarioId) {
        return comentarios.remove(comentarioId) != null;
    }

    // Elimina todos los comentarios de un usuario (baja del usuario). Devuelve cantidad borrada
    public int eliminarComentariosDe(Usuario usuario) {
        int antes = comentarios.size();
        comentarios.values().removeIf(c -> c.getAutor().equals(usuario));
        return antes - comentarios.size();
    }

    // Copia, en orden de alta
    public List<Comentario> getComentarios() {
        return new ArrayList<>(comentarios.values());
    }

    public int getCantidadComentarios() {
        return comentarios.size();
    }

    // getter y setters

    public int getId() { return id; }
    public Usuario getAutor() { return autor; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public int getReacciones() { return reacciones; }

    public void setFechaHora(LocalDateTime fechaHora) {
        if (fechaHora == null) throw new IllegalArgumentException("La fecha y hora son obligatorias");
        this.fechaHora = fechaHora;
    }

    public void setReacciones(int reacciones) {
        validarReacciones(reacciones);
        this.reacciones = reacciones;
    }

    private static void validarReacciones(int valor) {
        if (valor < 0) throw new IllegalArgumentException("Las reacciones no pueden ser negativas");
    }

    @Override
    public String toString() {
        return "Publicacion{id=" + id + ", autor=" + autor.getNombre() + ", fechaHora=" + fechaHora
                + ", reacciones=" + reacciones + ", comentarios=" + comentarios.size()
                + ", relevancia=" + getRelevancia() + "}";
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publicacion)) return false;
        return id == ((Publicacion) o).id;
    }
}