package com.ajs.tpo.service;
 
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import com.ajs.tpo.algorithm.MergeSortPublicaciones;
import com.ajs.tpo.enums.CriterioOrdenamientoPublicaciones;
import com.ajs.tpo.model.Publicacion;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.repository.PublicacionRepository;
import com.ajs.tpo.repository.UsuarioRepository;
 
// Gestion de publiaciones y muro
public class PublicacionService {
 
    private final PublicacionRepository publicaciones;
    private final UsuarioRepository usuarios;
 
    public PublicacionService(PublicacionRepository publicaciones, UsuarioRepository usuarios) {
        this.publicaciones = publicaciones;
        this.usuarios = usuarios;
    }
 
    // Alta con fecha y hora actuales
    public Publicacion crear(int autorId, int reacciones, int comentarios) {
        return crear(publicaciones.generarId(), autorId, LocalDateTime.now(), reacciones, comentarios);
    }
 
    // Alta para pruebas
    public Publicacion crear(int id, int autorId, LocalDateTime fechaHora, int reacciones, int comentarios) {
        Usuario autor = usuarios.buscarPorId(autorId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el autor con id " + autorId));
        Publicacion p = new Publicacion(id, autor, fechaHora, reacciones, comentarios);
        publicaciones.agregar(p);
        return p;
    }
 
    public Publicacion modificar(int id, LocalDateTime fechaHora, int reacciones, int comentarios) {
        Publicacion p = obtener(id);
        // Se validan todos los datos antes de aplicar para no dejar la publicacion a medias
        if (fechaHora == null) throw new IllegalArgumentException("La fecha y hora son obligatorias");
        if (reacciones < 0 || comentarios < 0) {
            throw new IllegalArgumentException("Reacciones y comentarios no pueden ser negativos");
        }
        p.setFechaHora(fechaHora);
        p.setReacciones(reacciones);
        p.setComentarios(comentarios);
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
    
    // aplicacion mergesort por fecha
    public List<Publicacion> muroPorFecha() {
        return ordenarMuro(CriterioOrdenamientoPublicaciones.POR_FECHA);
    }
 
    // aplicacion mergesort por relevancia
    public List<Publicacion> muroPorRelevancia() {
        return ordenarMuro(CriterioOrdenamientoPublicaciones.POR_RELEVANCIA);
    }
 
    // El merge sort trabaja sobre un arreglo y lo ordena en el lugar:se copia la lista
    // del repositorio a un arreglo, se ordena y se devuelve como lista nueva
    private List<Publicacion> ordenarMuro(CriterioOrdenamientoPublicaciones criterio) {
        Publicacion[] arreglo = publicaciones.listar().toArray(new Publicacion[0]);
        MergeSortPublicaciones.mergeSort(arreglo,0,(arreglo.length) -1, criterio);
        return new ArrayList<>(Arrays.asList(arreglo));
    }
}

 