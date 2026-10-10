package com.ajs.tpo.model;

import java.time.LocalDateTime;

// Publicacion de un Usuario en el muro


public class Publicacion {

    private final int id;
    private final Usuario autor;
    private LocalDateTime fechaHora;

    // temporalmente se va a trabajarlos como int
    private int reacciones;
    private int comentarios;

    public Publicacion(int id, Usuario autor, LocalDateTime fechaHora, int reacciones, int comentarios) {
        if (autor == null) throw new IllegalArgumentException("El autor es obligatorio");
        if (fechaHora == null) throw new IllegalArgumentException("La fecha y hora son obligatorias");
        validarContador(reacciones, "reacciones");
        validarContador(comentarios, "comentarios");
        this.id = id;
        this.autor = autor;
        this.fechaHora = fechaHora;
        this.reacciones = reacciones;
        this.comentarios = comentarios;
    }

    // TODO: Mejorar calculo de relevancia por algo mas sofisticado? Y dsp justificar decision
    public int getRelevancia() {
        return reacciones + 2 * comentarios;
    }

    public int getId() { return id; }
    public Usuario getAutor() { return autor; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public int getReacciones() { return reacciones; }
    public int getComentarios() { return comentarios; }

    public void setFechaHora(LocalDateTime fechaHora) {
        if (fechaHora == null) throw new IllegalArgumentException("La fecha y hora son obligatorias");
        this.fechaHora = fechaHora;
    }

    public void setReacciones(int reacciones) {
        validarContador(reacciones, "reacciones");
        this.reacciones = reacciones;
    }

    public void setComentarios(int comentarios) {
        validarContador(comentarios, "comentarios");
        this.comentarios = comentarios;
    }

    private static void validarContador(int valor, String campo) {
        if (valor < 0) throw new IllegalArgumentException("Las " + campo + " no pueden ser negativas");
    }

    @Override
    public String toString() {
        return "Publicacion{id=" + id + ", autor=" + autor.getNombre() + ", fechaHora=" + fechaHora
                + ", reacciones=" + reacciones + ", comentarios=" + comentarios
                + ", relevancia=" + getRelevancia() + "}";
    }

        
    // metodos para poder trabajar con hashmap si se requiere
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publicacion)) return false;
        return id == ((Publicacion) o).id;
    }

}