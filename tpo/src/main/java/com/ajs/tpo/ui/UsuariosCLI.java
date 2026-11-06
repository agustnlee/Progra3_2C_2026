package com.ajs.tpo.ui;

import java.util.ArrayList;
import java.util.List;

import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.service.UsuarioService;

public class UsuariosCLI implements ModuloCLI {

    private final Consola consola;
    private final UsuarioService usuarios;

    public UsuariosCLI(Consola consola, UsuarioService usuarios) {
        this.consola = consola;
        this.usuarios = usuarios;
    }

    @Override
    public String nombre() { return "Usuarios"; }

    @Override
    public void ejecutar() {
        int op;
        do {
            consola.titulo("Usuarios");
            System.out.println("1. Listar   2. Crear   3. Modificar nombre   4. Eliminar");
            System.out.println("5. Agregar habilidad   6. Quitar habilidad   0. Volver");
            consola.cierre();
            op = consola.leerOpcion("Opcion: ");
            final int opcion = op;
            consola.seguro(() -> accion(opcion));
        } while (op != 0);
    }

    private void accion(int op) {
        switch (op) {
            case 1 -> listar();
            case 2 -> consola.mensaje("Creado: " + formato(usuarios.crear(consola.leerTexto("Nombre: "))));
            case 3 -> consola.mensaje("Modificado: " + formato(
                    usuarios.modificar(consola.leerInt("Id: "), consola.leerTexto("Nuevo nombre: "))));
            case 4 -> { usuarios.eliminar(consola.leerInt("Id: ")); consola.mensaje("Usuario eliminado."); }
            case 5 -> { usuarios.registrarHabilidad(consola.leerInt("Id: "), consola.leerTexto("Habilidad: ")); consola.mensaje("Habilidad agregada."); }
            case 6 -> { usuarios.quitarHabilidad(consola.leerInt("Id: "), consola.leerTexto("Habilidad: ")); consola.mensaje("Habilidad quitada."); }
            case 0 -> { }
            default -> consola.mensaje("Opcion invalida.");
        }
    }

    private void listar() {
        List<String> filas = new ArrayList<>();
        for (Usuario u : usuarios.listar()) filas.add(formato(u));
        consola.listado("Listado de usuarios", filas);
    }

    private static String formato(Usuario u) {
        return String.format("[%d] %-12s habilidades: %s", u.getId(), u.getNombre(), u.getHabilidades());
    }
}