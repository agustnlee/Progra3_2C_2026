package com.ajs.tpo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ajs.tpo.algorithm.MergeSortPublicaciones;
import com.ajs.tpo.enums.CriterioOrdenamientoPublicaciones;
import com.ajs.tpo.model.Comentario;
import com.ajs.tpo.model.Publicacion;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.repository.PublicacionRepository;
import com.ajs.tpo.repository.UsuarioRepository;

// Gestion de publicaciones, sus comentarios y el muro
public class PublicacionService {

    private final PublicacionRepository publicaciones;
    private final UsuarioRepository usuarios;

    public PublicacionService(PublicacionRepository publicaciones, UsuarioRepository usuarios) {
        this.publicaciones = publicaciones;
        this.usuarios = usuarios;
    }

    // publicacion

    // Alta con fecha y hora actuales
    public Publicacion crear(int autorId, int reacciones) {
        return crear(publicaciones.generarId(), autorId, LocalDateTime.now(), reacciones);
    }

    // Alta con id y fecha fijos (para pruebas y datos de ejemplo)
    public Publicacion crear(int id, int autorId, LocalDateTime fechaHora, int reacciones) {
        Usuario autor = obtenerUsuario(autorId, "autor");
        Publicacion p = new Publicacion(id, autor, fechaHora, reacciones);
        publicaciones.agregar(p);
        return p;
    }

    public Publicacion modificar(int id, LocalDateTime fechaHora, int reacciones) {
        Publicacion p = obtener(id);
        if (fechaHora == null) throw new IllegalArgumentException("La fecha y hora son obligatorias");
        if (reacciones < 0) throw new IllegalArgumentException("Las reacciones no pueden ser negativas");
        p.setFechaHora(fechaHora);
        p.setReacciones(reacciones);
        return p;
    }

    public Publicacion modificarReacciones(int id, int reacciones) {
        Publicacion p = obtener(id);
        p.setReacciones(reacciones);
        return p;
    }

    public void eliminar(int id) {
        obtener(id);
        publicaciones.eliminar(id);
    }

    public Publicacion obtener(int id) {
        return publicaciones.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe la publicacion con id " + id));
    }

    public List<Publicacion> listar() {
        return publicaciones.listar();
    }

    public List<Publicacion> listarPorAutor(int autorId) {
        Usuario autor = obtenerUsuario(autorId, "autor");
        List<Publicacion> res = new ArrayList<>();
        for (Publicacion p : publicaciones.listar()) {
            if (p.getAutor().equals(autor)) res.add(p);
        }
        return res;
    }

    // comentario

    public Comentario agregarComentario(int publicacionId, int autorId, String texto) {
        return agregarComentario(publicacionId, autorId, texto, LocalDateTime.now());
    }

    // Version con fecha fija para pruebas
    public Comentario agregarComentario(int publicacionId, int autorId, String texto, LocalDateTime fechaHora) {
        Publicacion p = obtener(publicacionId);
        Usuario autor = obtenerUsuario(autorId, "usuario");
        return p.agregarComentario(autor, texto, fechaHora);
    }

    public Comentario modificarComentario(int publicacionId, int comentarioId, String nuevoTexto) {
        Comentario c = obtenerComentario(publicacionId, comentarioId);
        c.setTexto(nuevoTexto);
        return c;
    }

    public void eliminarComentario(int publicacionId, int comentarioId) {
        if (!obtener(publicacionId).eliminarComentario(comentarioId)) {
            throw new IllegalArgumentException("No existe el comentario " + comentarioId
                    + " en la publicacion " + publicacionId);
        }
    }

    public List<Comentario> listarComentarios(int publicacionId) {
        return obtener(publicacionId).getComentarios();
    }


    // ** MURO ** (Merge sort aplicacions)

    // aplicacion mergesort por fecha
    public List<Publicacion> muroPorFecha() {
        return ordenarMuro(CriterioOrdenamientoPublicaciones.POR_FECHA);
    }

    // aplicacion mergesort por relevancia
    public List<Publicacion> muroPorRelevancia() {
        return ordenarMuro(CriterioOrdenamientoPublicaciones.POR_RELEVANCIA);
    }

    // merge sort trabaja sobre un arreglo y lo ordena en el lugar, se copia la lista
    // del repositorio a un arreglo, se ordena y se devuelve como lista nueva
    private List<Publicacion> ordenarMuro(CriterioOrdenamientoPublicaciones criterio) {
        Publicacion[] arreglo = publicaciones.listar().toArray(new Publicacion[0]);
        MergeSortPublicaciones.mergeSort(arreglo, criterio);
        return new ArrayList<>(Arrays.asList(arreglo));
    }

    // metodos auxiliares

    private Comentario obtenerComentario(int publicacionId, int comentarioId) {
        return obtener(publicacionId).buscarComentario(comentarioId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el comentario " + comentarioId
                        + " en la publicacion " + publicacionId));
    }

    private Usuario obtenerUsuario(int id, String rol) {
        return usuarios.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el " + rol + " con id " + id));
    }
}