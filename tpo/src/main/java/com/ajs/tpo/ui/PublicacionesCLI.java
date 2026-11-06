package com.ajs.tpo.ui;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.ajs.tpo.model.Comentario;
import com.ajs.tpo.model.Publicacion;
import com.ajs.tpo.service.PublicacionService;

public class PublicacionesCLI implements ModuloCLI {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Consola consola;
    private final PublicacionService publicaciones;

    public PublicacionesCLI(Consola consola, PublicacionService publicaciones) {
        this.consola = consola;
        this.publicaciones = publicaciones;
    }

    @Override
    public String nombre() { return "Publicaciones y muro"; }

    @Override
    public void ejecutar() {
        int op;
        do {
            consola.titulo("Publicaciones");
            System.out.println("1. Listar   2. Muro por fecha   3. Muro por relevancia   4. Listar por autor");
            System.out.println("5. Crear   6. Modificar reacciones   7. Eliminar");
            System.out.println("8. Ver comentarios   9. Agregar comentario   10. Modificar comentario   11. Eliminar comentario");
            System.out.println("0. Volver");
            consola.cierre();
            op = consola.leerOpcion("Opcion: ");
            final int opcion = op;
            consola.seguro(() -> accion(opcion));
        } while (op != 0);
    }

    private void accion(int op) {
        switch (op) {
            case 1 -> mostrar("Listado de publicaciones", publicaciones.listar());
            case 2 -> mostrar("Muro por fecha (mas reciente primero)", publicaciones.muroPorFecha());
            case 3 -> mostrar("Muro por relevancia", publicaciones.muroPorRelevancia());
            case 4 -> mostrar("Publicaciones del autor", publicaciones.listarPorAutor(consola.leerInt("Id autor: ")));
            case 5 -> consola.mensaje("Creada con id " + publicaciones.crear(
                    consola.leerInt("Id autor: ")).getId());
            case 6 -> {
                publicaciones.modificarReacciones(consola.leerInt("Id publicacion: "), consola.leerInt("Nuevas reacciones: "));
                consola.mensaje("Modificada.");
            }
            case 7 -> { publicaciones.eliminar(consola.leerInt("Id publicacion: ")); consola.mensaje("Eliminada."); }
            case 8 -> {
                List<String> filas = new ArrayList<>();
                for (Comentario c : publicaciones.listarComentarios(consola.leerInt("Id publicacion: "))) {
                    filas.add(c + "  (" + c.getFechaHora().format(FMT) + ")");
                }
                consola.listado("Comentarios", filas);
            }
            case 9 -> consola.mensaje("Comentario agregado con id " + publicaciones.agregarComentario(
                    consola.leerInt("Id publicacion: "), consola.leerInt("Id autor del comentario: "),
                    consola.leerTexto("Texto: ")).getId());
            case 10 -> {
                publicaciones.modificarComentario(consola.leerInt("Id publicacion: "),
                        consola.leerInt("Id comentario: "), consola.leerTexto("Nuevo texto: "));
                consola.mensaje("Comentario modificado.");
            }
            case 11 -> {
                publicaciones.eliminarComentario(consola.leerInt("Id publicacion: "), consola.leerInt("Id comentario: "));
                consola.mensaje("Comentario eliminado.");
            }
            case 0 -> { }
            default -> consola.mensaje("Opcion invalida.");
        }
    }

    private void mostrar(String titulo, List<Publicacion> lista) {
        List<String> filas = new ArrayList<>();
        filas.add(String.format("%-4s %-12s %-17s %-8s %-9s %-6s", "ID", "Autor", "Fecha", "Reacc.", "Coment.", "Relev."));
        for (Publicacion p : lista) {
            filas.add(String.format("%-4d %-12s %-17s %-8d %-9d %-6d", p.getId(), p.getAutor().getNombre(),
                    p.getFechaHora().format(FMT), p.getReacciones(), p.getCantidadComentarios(), p.getRelevancia()));
        }
        if (lista.isEmpty()) filas.clear();
        consola.listado(titulo, filas);
    }
}