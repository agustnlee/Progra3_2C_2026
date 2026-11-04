package com.ajs.tpo.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ajs.tpo.algorithm.BacktrackingSubconjuntosEquipo;
import com.ajs.tpo.model.Equipo;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.service.EquipoService;
import com.ajs.tpo.service.FormacionEquiposService;

public class EquiposCLI implements ModuloCLI {

    private static final int MAX_PASOS_MOSTRADOS = 20;

    private final Consola consola;
    private final FormacionEquiposService formacion;
    private final EquipoService equipos;

    // Ultimo resultado de B1, para poder confirmar uno de sus equipos
    private BacktrackingSubconjuntosEquipo.Resultado ultimo;

    public EquiposCLI(Consola consola, FormacionEquiposService formacion, EquipoService equipos) {
        this.consola = consola;
        this.formacion = formacion;
        this.equipos = equipos;
    }

    @Override
    public String nombre() { return "Equipos"; }

    @Override
    public void ejecutar() {
        int op;
        do {
            consola.titulo("Equipos");
            System.out.println("1. Buscar equipos validos con restricciones   2. Confirmar un equipo del ultimo resultado");
            System.out.println("3. Crear equipo manual   4. Listar equipos confirmados   5. Eliminar equipo   0. Volver");
            consola.cierre();
            op = consola.leerOpcion("Opcion: ");
            final int opcion = op;
            consola.seguro(() -> accion(opcion));
        } while (op != 0);
    }

    private void accion(int op) {
        switch (op) {
            case 1 -> buscar();
            case 2 -> {
                if (ultimo == null || !ultimo.hayEquipos()) {
                    consola.mensaje("Primero ejecute la busqueda con restricciones (opcion 1) y obtenga equipos validos.");
                    return;
                }
                int id = equipos.crearDesdeResultado(ultimo, consola.leerInt("Numero de equipo del resultado: "));
                consola.mensaje("Equipo confirmado con id " + id);
            }
            case 3 -> consola.mensaje("Equipo creado con id "
                    + equipos.crear(consola.leerIds("Ids de integrantes (ej. 1): ")));
            case 4 -> {
                List<String> filas = new ArrayList<>();
                for (Map.Entry<Integer, Equipo> e : equipos.listar().entrySet()) {
                    filas.add("[" + e.getKey() + "] " + detalle(e.getValue()));
                }
                consola.listado("Equipos confirmados", filas);
            }
            case 5 -> { equipos.eliminar(consola.leerInt("Id de equipo: ")); consola.mensaje("Equipo eliminado."); }
            case 0 -> { }
            default -> consola.mensaje("Opcion invalida.");
        }
    }

    private void buscar() {
        int tamanio = consola.leerInt("Tamanio del equipo: ");
        List<String> habilidades = leerLista("Habilidades requeridas, separadas por coma (vacio = ninguna): ");
        List<Integer> obligatorios = consola.leerIds("Ids de integrantes obligatorios (vacio = ninguno): ");
        List<int[]> incompatibles = leerPares("Pares incompatibles, ej. 1-2, (vacio = ninguno): ");

        ultimo = formacion.formarEquipos(tamanio, habilidades, obligatorios, incompatibles);

        List<String> filas = new ArrayList<>();
        int n = 1;
        for (Equipo e : ultimo.getEquipos()) filas.add("(" + n++ + ") " + detalle(e));
        consola.listado("Equipos validos encontrados: " + ultimo.getEquipos().size(), filas);

        if (!ultimo.hayEquipos()) consola.mensaje("No existe ningun equipo que cumpla las restricciones.");

        System.out.println("Decisiones principales:");
        int mostrados = 0;
        for (String paso : ultimo.getPasos()) {
            if (mostrados == MAX_PASOS_MOSTRADOS) {
                System.out.println("  ... (" + (ultimo.getPasos().size() - mostrados) + " pasos mas registrados)");
                break;
            }
            System.out.println("  " + paso);
            mostrados++;
        }
        consola.cierre();
    }

    private static String detalle(Equipo e) {
        StringBuilder sb = new StringBuilder();
        for (Usuario u : e.getIntegrantes()) {
            if (sb.length() > 0) sb.append("; ");
            sb.append(u.getId()).append(" ").append(u.getNombre()).append(" ").append(u.getHabilidades());
        }
        return "{" + sb + "}";
    }

    private List<String> leerLista(String mensaje) {
        List<String> res = new ArrayList<>();
        String linea = consola.leerTexto(mensaje);
        if (!linea.isEmpty()) {
            for (String p : linea.split(",")) if (!p.isBlank()) res.add(p.trim());
        }
        return res;
    }

    private List<int[]> leerPares(String mensaje) {
        while (true) {
            List<int[]> pares = new ArrayList<>();
            String linea = consola.leerTexto(mensaje);
            try {
                if (!linea.isEmpty()) {
                    for (String p : linea.split(",")) {
                        String[] ab = p.trim().split("-");
                        if (ab.length != 2) throw new NumberFormatException();
                        pares.add(new int[] { Integer.parseInt(ab[0].trim()), Integer.parseInt(ab[1].trim()) });
                    }
                }
                return pares;
            } catch (NumberFormatException e) {
                consola.mensaje("Formato invalido. Ejemplo: 1-2");
            }
        }
    }
}