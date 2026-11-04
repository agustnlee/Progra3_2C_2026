package com.ajs.tpo.ui;

import java.util.ArrayList;
import java.util.List;

import com.ajs.tpo.model.Iniciativa;
import com.ajs.tpo.model.ResultadoFinanciacion;
import com.ajs.tpo.service.IniciativaService;
import com.ajs.tpo.service.MochilaDinamicaService;

public class IniciativasCLI implements ModuloCLI {

    private final Consola consola;
    private final IniciativaService iniciativas;
    private final MochilaDinamicaService mochila;

    public IniciativasCLI(Consola consola, IniciativaService iniciativas, MochilaDinamicaService mochila) {
        this.consola = consola;
        this.iniciativas = iniciativas;
        this.mochila = mochila;
    }

    @Override
    public String nombre() { return "Iniciativas y financiamiento (Mochila 0/1)"; }

    @Override
    public void ejecutar() {
        int op;
        do {
            consola.titulo("Iniciativas");
            System.out.println("1. Listar   2. Crear   3. Modificar   4. Eliminar");
            System.out.println("5. Seleccionar iniciativas a financiar   0. Volver");
            consola.cierre();
            op = consola.leerInt("Opcion: ");
            final int opcion = op;
            consola.seguro(() -> accion(opcion));
        } while (op != 0);
    }

    private void accion(int op) {
        switch (op) {
            case 1 -> consola.listado("Listado de iniciativas", filas(iniciativas.listar()));
            case 2 -> consola.mensaje("Creada: " + fila(iniciativas.crear(
                    consola.leerTexto("Nombre: "), consola.leerInt("Costo (entero > 0): "),
                    consola.leerInt("Impacto (entero >= 0): "))));
            case 3 -> consola.mensaje("Modificada: " + fila(iniciativas.modificar(
                    consola.leerInt("Id: "), consola.leerTexto("Nombre: "),
                    consola.leerInt("Costo: "), consola.leerInt("Impacto: "))));
            case 4 -> { iniciativas.eliminar(consola.leerInt("Id: ")); consola.mensaje("Eliminada."); }
            case 5 -> financiar(consola.leerInt("Presupuesto disponible: "));
            case 0 -> { }
            default -> consola.mensaje("Opcion invalida.");
        }
    }

    private void financiar(int presupuesto) {
        List<Iniciativa> todas = iniciativas.listar();
        ResultadoFinanciacion r = mochila.seleccionarIniciativas(presupuesto);

        consola.listado("Iniciativas seleccionadas", filas(r.getSeleccionadas()));
        System.out.println("Costo total:          " + r.getCostoTotal());
        System.out.println("Impacto total:        " + r.getImpactoTotal());
        System.out.println("Presupuesto restante: " + r.getPresupuestoRestante());

        List<String> noElegidas = new ArrayList<>();
        for (Iniciativa i : todas) {
            if (!r.getSeleccionadas().contains(i)) noElegidas.add(fila(i));
        }
        consola.listado("Iniciativas no financiadas", noElegidas);

        consola.titulo("Resumen del procedimiento");
        System.out.println("1. Se consideraron " + todas.size() + " iniciativas con presupuesto " + presupuesto + ".");
        System.out.println("2. Para cada iniciativa y cada presupuesto parcial se guardo el mayor impacto posible,");
        System.out.println("   decidiendo entre no financiarla o financiarla y sumar su impacto al mejor resultado");
        System.out.println("   con el presupuesto que queda.");
        System.out.println("3. Se recorrio la tabla hacia atras para identificar cuales se financian.");
        consola.cierre();
    }

    private List<String> filas(List<Iniciativa> lista) {
        List<String> filas = new ArrayList<>();
        for (Iniciativa i : lista) filas.add(fila(i));
        return filas;
    }

    private static String fila(Iniciativa i) {
        return String.format("[%d] %-26s costo: %-6d impacto: %d", i.getId(), i.getNombre(), i.getCosto(), i.getImpacto());
    }
}