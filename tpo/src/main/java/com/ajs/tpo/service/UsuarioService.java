package com.ajs.tpo.service;

import java.util.List;

import com.ajs.tpo.model.GrafoUsuarios;
import com.ajs.tpo.model.Publicacion;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.repository.PublicacionRepository;
import com.ajs.tpo.repository.UsuarioRepository;

// Funciones de gestion de usuarios
public class UsuarioService {

    private final UsuarioRepository usuarios;
    private final PublicacionRepository publicaciones;
    private final GrafoUsuarios grafo;
    private final EquipoService equipoService;

    public UsuarioService(UsuarioRepository usuarios, PublicacionRepository publicaciones,
                          GrafoUsuarios grafo, EquipoService equipoService) {
        this.usuarios = usuarios;
        this.publicaciones = publicaciones;
        this.grafo = grafo;
        this.equipoService = equipoService;
    }

    public Usuario crear(String nombre) {
        return crear(usuarios.generarId(), nombre);
    }

    // Alta con id fijo para testing
    public Usuario crear(int id, String nombre) {
        Usuario u = new Usuario(id, nombre);
        usuarios.agregar(u);
        grafo.agregarUsuario(u); // usuario como vertice nuevo sin conexiones
        return u;
    }

    public Usuario modificar(int id, String nuevoNombre) {
        Usuario u = obtener(id);
        u.setNombre(nuevoNombre);
        return u;
    }

    // Baja, se elimina del grafo y sus conexiones, de los equipos que integraba,
    // sus publicaciones y sus comentarios en publicaciones ajenas
    public void eliminar(int id) {
        Usuario u = obtener(id);
        grafo.eliminarUsuario(u);
        equipoService.eliminarEquiposDe(u);
        for (Publicacion p : publicaciones.listar()) {
            if (p.getAutor().equals(u)) {
                publicaciones.eliminar(p.getId());
            } else {
                p.eliminarComentariosDe(u);
            }
        }
        usuarios.eliminar(id);
    }

    public Usuario obtener(int id) {
        return usuarios.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el usuario con id " + id));
    }

    public List<Usuario> listar() {
        return usuarios.listar();
    }

    public void registrarHabilidad(int id, String habilidad) {
        obtener(id).agregarHabilidad(habilidad);
    }

    public void quitarHabilidad(int id, String habilidad) {
        if (!obtener(id).quitarHabilidad(habilidad)) {
            throw new IllegalArgumentException("El usuario no tiene la habilidad '" + habilidad + "'");
        }
    }
}