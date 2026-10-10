package com.ajs.tpo.model;

// Vinculo Dirigido de difusion: origen -> destino, con tiempo (minutos) positivo; forma parte de grafo usuarios
// Una conexion deshabilitada no debe ser usada por los algoritmos de camino

public class Conexion {

    private final Usuario origen;
    private final Usuario destino;
    private int tiempo;
    private boolean habilitada;

    public Conexion(Usuario origen, Usuario destino, int tiempo) {
        this(origen, destino, tiempo, true);
    }

    public Conexion(Usuario origen, Usuario destino, int tiempo, boolean habilitada) {
        if (origen == null || destino == null) throw new IllegalArgumentException("Origen y destino son obligatorios");
        if (origen.equals(destino)) throw new IllegalArgumentException("Una conexion no puede ser de un usuario consigo mismo");
        validarTiempo(tiempo);
        this.origen = origen;
        this.destino = destino;
        this.tiempo = tiempo;
        this.habilitada = habilitada;
    }

    private static void validarTiempo(int tiempo) {
        if (tiempo <= 0) throw new IllegalArgumentException("El tiempo debe ser positivo"); // limitacion dijkstra
    }

    public Usuario getOrigen() { return origen; }
    public Usuario getDestino() { return destino; }
    public int getTiempo() { return tiempo; }
    public boolean isHabilitada() { return habilitada; }

    public void setTiempo(int tiempo) {
        validarTiempo(tiempo);
        this.tiempo = tiempo;
    }

    public void habilitar() { this.habilitada = true; }
    public void deshabilitar() { this.habilitada = false; }

    @Override
    public String toString() {
        return origen.getNombre() + " -> " + destino.getNombre() + " (" + tiempo + " min"
                + (habilitada ? "" : ", DESHABILITADA") + ")";
    }
}
