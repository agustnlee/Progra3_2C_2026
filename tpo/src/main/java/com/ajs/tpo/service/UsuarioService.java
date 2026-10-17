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
 
    public UsuarioService(UsuarioRepository usuarios, PublicacionRepository publicaciones, GrafoUsuarios grafo) {
        this.usuarios = usuarios;
        this.publicaciones = publicaciones;
        this.grafo = grafo;
    }
 
    public Usuario crear(String nombre) {
        return crear(usuarios.generarId(), nombre);
    }

    // Alta con id fijo para datos de testing 
    public Usuario crear(int id, String nombre) {
        Usuario u = new Usuario(id, nombre);
        usuarios.agregar(u);
        grafo.agregarUsuario(u); // usuario como vertice, aunque sin conecciones
        return u;
    }
 
    public Usuario modificar(int id, String nuevoNombre) {
        Usuario u = obtener(id);
        u.setNombre(nuevoNombre);
        return u;
    }
 
    // Baja de usuario, tambien se elimina del grafo con sus conexiones
    public void eliminar(int id) {
        Usuario u = obtener(id);
        grafo.eliminarUsuario(u);
        for (Publicacion p : publicaciones.listar()) {
            if (p.getAutor().equals(u)) publicaciones.eliminar(p.getId());
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
 