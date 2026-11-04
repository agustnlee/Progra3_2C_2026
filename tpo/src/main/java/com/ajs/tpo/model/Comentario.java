package com.ajs.tpo.model;

import java.time.LocalDateTime;

// Comentario de un usuario dentro de una publicacion, con id es unico dentro de la publicacion
public class Comentario {

    private final int id;
    private final Usuario autor;
    private final LocalDateTime fechaHora;
    private String texto;

    public Comentario(int id, Usuario autor, String texto, LocalDateTime fechaHora) {
        if (autor == null) throw new IllegalArgumentException("El autor es obligatorio");
        if (fechaHora == null) throw new IllegalArgumentException("La fecha y hora son obligatorias");
        validarTexto(texto);
        this.id = id;
        this.autor = autor;
        this.texto = texto.trim();
        this.fechaHora = fechaHora;
    }

    private static void validarTexto(String texto) {
        if (texto == null || texto.isBlank()) throw new IllegalArgumentException("El comentario no puede estar vacio");
    }

    public int getId() { return id; }
    public Usuario getAutor() { return autor; }
    public String getTexto() { return texto; }
    public LocalDateTime getFechaHora() { return fechaHora; }

    public void setTexto(String texto) {
        validarTexto(texto);
        this.texto = texto.trim();
    }

    @Override
    public String toString() {
        return "#" + id + " " + autor.getNombre() + ": " + texto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comentario)) return false;
        return id == ((Comentario) o).id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }
}