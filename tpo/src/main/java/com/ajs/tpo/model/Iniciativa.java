package com.ajs.tpo.model;

// Iniciativa universitaria financiable. Se financia completa o no se financia 

public class Iniciativa {

    private final int id;
    private String nombre;
    private int costo; // TODO: Costo podria pasarse a double pero temporalmente lo dejo como int
    private int impacto;

    public Iniciativa(int id, String nombre, int costo, int impacto) {
        validar(nombre, costo, impacto);
        this.id = id;
        this.nombre = nombre.trim();
        this.costo = costo;
        this.impacto = impacto;
    }

    private static void validar(String nombre, int costo, int impacto) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacio");
        if (costo <= 0) throw new IllegalArgumentException("El costo debe ser un entero positivo");
        if (impacto < 0) throw new IllegalArgumentException("El impacto no puede ser negativo");
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getCosto() { return costo; }
    public int getImpacto() { return impacto; }

    public void setNombre(String nombre) {
        validar(nombre, costo, impacto);
        this.nombre = nombre.trim();
    }

    public void setCosto(int costo) {
        validar(nombre, costo, impacto);
        this.costo = costo;
    }

    public void setImpacto(int impacto) {
        validar(nombre, costo, impacto);
        this.impacto = impacto;
    }

    @Override
    public String toString() {
        return "Iniciativa{id=" + id + ", nombre='" + nombre + "', costo=" + costo + ", impacto=" + impacto + "}";
    }

    // metodos para poder trabajar con hashmap si se requiere

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Iniciativa)) return false;
        return id == ((Iniciativa) o).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
