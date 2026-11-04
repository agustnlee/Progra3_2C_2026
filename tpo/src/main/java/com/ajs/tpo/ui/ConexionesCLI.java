package com.ajs.tpo.ui;

import java.util.ArrayList;
import java.util.List;

import com.ajs.tpo.model.Conexion;
import com.ajs.tpo.model.GrafoUsuarios;
import com.ajs.tpo.service.ConexionService;

public class ConexionesCLI implements ModuloCLI {

    private final Consola consola;
    private final ConexionService conexiones;
    private final GrafoUsuarios grafo;

    public ConexionesCLI(Consola consola, ConexionService conexiones, GrafoUsuarios grafo) {
        this.consola = consola;
        this.conexiones = conexiones;
        this.grafo = grafo;
    }

    @Override
    public String nombre() { return "Conexiones y grafo"; }

    @Override
    public void ejecutar() {
        int op;
        do {
            consola.titulo("Conexiones");
            System.out.println("1. Ver grafo   2. Conexiones de un usuario   3. Crear   4. Modificar tiempo");
            System.out.println("5. Habilitar   6. Deshabilitar   7. Eliminar   0. Volver");
            consola.cierre();
            op = consola.leerInt("Opcion: ");
            final int opcion = op;
            consola.seguro(() -> accion(opcion));
        } while (op != 0);
    }

    private void accion(int op) {
        switch (op) {
            case 1 -> consola.listado("Grafo de usuarios", grafo.representacion());
            case 2 -> {
                int id = consola.leerInt("Id usuario: ");
                List<String> filas = new ArrayList<>();
                for (Conexion c : conexiones.consultarConexiones(id)) filas.add(c.toString());
                consola.listado("Conexiones salientes", filas);
            }
            case 3 -> consola.mensaje("Creada: " + conexiones.crearConexion(
                    consola.leerInt("Origen: "), consola.leerInt("Destino: "), consola.leerInt("Tiempo (min): ")));
            case 4 -> {
                conexiones.modificarTiempo(consola.leerInt("Origen: "), consola.leerInt("Destino: "),
                        consola.leerInt("Nuevo tiempo (min): "));
                consola.mensaje("Tiempo modificado.");
            }
            case 5 -> { conexiones.habilitarConexion(consola.leerInt("Origen: "), consola.leerInt("Destino: ")); consola.mensaje("Habilitada."); }
            case 6 -> { conexiones.deshabilitarConexion(consola.leerInt("Origen: "), consola.leerInt("Destino: ")); consola.mensaje("Deshabilitada."); }
            case 7 -> { conexiones.eliminarConexion(consola.leerInt("Origen: "), consola.leerInt("Destino: ")); consola.mensaje("Eliminada."); }
            case 0 -> { }
            default -> consola.mensaje("Opcion invalida.");
        }
    }
}