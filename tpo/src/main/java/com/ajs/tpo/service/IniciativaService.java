package com.ajs.tpo.service;

import java.util.List;

import com.ajs.tpo.model.Iniciativa;
import com.ajs.tpo.repository.IniciativaRepository;

// Gestion de iniciativas (alta baja modif)
public class IniciativaService {

    private final IniciativaRepository iniciativas;

    public IniciativaService(IniciativaRepository iniciativas) {
        this.iniciativas = iniciativas;
    }

    public Iniciativa crear(String nombre, int costo, int impacto) {
        return crear(iniciativas.generarId(), nombre, costo, impacto);
    }

    // Alta con id fijo para pruebas y datos de ejemplo
    public Iniciativa crear(int id, String nombre, int costo, int impacto) {
        Iniciativa i = new Iniciativa(id, nombre, costo, impacto); // se valida internamente costo > 0 e impacto >= 0
        iniciativas.agregar(i);
        return i;
    }

    public Iniciativa modificar(int id, String nombre, int costo, int impacto) {
        Iniciativa i = obtener(id);
        new Iniciativa(id, nombre, costo, impacto);
        i.setNombre(nombre);
        i.setCosto(costo);
        i.setImpacto(impacto);
        return i;
    }

    public void eliminar(int id) {
        obtener(id);
        iniciativas.eliminar(id);
    }

    public Iniciativa obtener(int id) {
        return iniciativas.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe la iniciativa con id " + id));
    }

    public List<Iniciativa> listar() {
        return iniciativas.listar();
    }
}