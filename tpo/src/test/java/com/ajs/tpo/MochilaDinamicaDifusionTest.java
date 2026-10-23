package com.ajs.tpo;

import com.ajs.tpo.model.Iniciativa;
import com.ajs.tpo.service.MochilaDinamicaService;
import com.ajs.tpo.model.ResultadoFinanciacion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MochilaDinamicaDifusionTest {

    private int pruebasEjecutadas;
    private int pruebasAprobadas;
    private int pruebasFallidas;

    public void ejecutar() {
        pruebasEjecutadas = 0;
        pruebasAprobadas = 0;
        pruebasFallidas = 0;

        System.out.println();
        System.out.println("========================================");
        System.out.println(" TESTS: MOCHILA DINAMICA");
        System.out.println("========================================");

        ejecutarPrueba(
                "Seleccion optima entre varias iniciativas",
                this::testSeleccionOptima
        );

        ejecutarPrueba(
                "Presupuesto exacto",
                this::testPresupuestoExacto
        );

        ejecutarPrueba(
                "No superar el presupuesto",
                this::testNoSuperarPresupuesto
        );

        ejecutarPrueba(
                "Una iniciativa no se selecciona dos veces",
                this::testIniciativaUnica
        );

        ejecutarPrueba(
                "Ninguna iniciativa entra en el presupuesto",
                this::testNingunaEntra
        );

        ejecutarPrueba(
                "Lista de iniciativas vacia",
                this::testListaVacia
        );

        ejecutarPrueba(
                "Presupuesto cero",
                this::testPresupuestoCero
        );

        ejecutarPrueba(
                "Una iniciativa con mayor impacto",
                this::testMayorImpacto
        );

        ejecutarPrueba(
                "Impactos iguales con costos diferentes",
                this::testImpactosIguales
        );

        ejecutarPrueba(
                "No es obligatorio gastar todo el presupuesto",
                this::testNoGastarTodo
        );

        System.out.println();
        System.out.println("========================================");
        System.out.println(" RESUMEN");
        System.out.println("========================================");
        System.out.println("Pruebas ejecutadas: " + pruebasEjecutadas);
        System.out.println("Aprobadas:          " + pruebasAprobadas);
        System.out.println("Fallidas:           " + pruebasFallidas);
        System.out.println("Resultado: "
                + (pruebasFallidas == 0
                ? "TODAS APROBADAS" : "HAY FALLOS"));
        System.out.println("========================================");
    }

    // Casos de prueba

    private void testSeleccionOptima() {
        List<Iniciativa> iniciativas = new ArrayList<>();

        iniciativas.add(iniciativa(1, 4, 7));
        iniciativas.add(iniciativa(2, 3, 5));
        iniciativas.add(iniciativa(3, 5, 10));
        iniciativas.add(iniciativa(4, 2, 4));

        ResultadoFinanciacion resultado =
                resolver(iniciativas, 7);

        comprobar(resultado.getImpactoTotal() == 14, "El impacto maximo esperado es 14.");

        comprobar(resultado.getCostoTotal() == 7, "El costo total esperado es 7.");

        comprobar(ids(resultado.getSeleccionadas()).containsAll(Arrays.asList(3, 4)) && resultado.getSeleccionadas().size() == 2, "Debe seleccionar las iniciativas 3 y 4.");

        comprobar(resultado.getPresupuestoRestante() == 0, "El presupuesto restante debe ser 0.");
    }

    private void testPresupuestoExacto() {
        List<Iniciativa> iniciativas = new ArrayList<>();

        iniciativas.add(iniciativa(1, 5, 10));
        iniciativas.add(iniciativa(2, 4, 6));

        ResultadoFinanciacion resultado =
                resolver(iniciativas, 5);

        comprobar(resultado.getCostoTotal() == 5,
                "Debe poder utilizar exactamente el presupuesto.");

        comprobar(resultado.getImpactoTotal() == 10,
                "Debe obtener un impacto de 10.");
    }

    private void testNoSuperarPresupuesto() {
        List<Iniciativa> iniciativas = new ArrayList<>();

        iniciativas.add(iniciativa(1, 8, 100));
        iniciativas.add(iniciativa(2, 4, 7));
        iniciativas.add(iniciativa(3, 3, 5));

        ResultadoFinanciacion resultado =
                resolver(iniciativas, 7);

        comprobar(resultado.getCostoTotal() <= 7,
                "El costo no puede superar el presupuesto.");

        comprobar(resultado.getImpactoTotal() == 12,
                "Debe elegir las iniciativas 2 y 3, con impacto 12.");

        comprobar(resultado.getPresupuestoRestante() == 0,
                "Debe utilizar los 7 disponibles.");
    }

    private void testIniciativaUnica() {
        List<Iniciativa> iniciativas = new ArrayList<>();

        iniciativas.add(iniciativa(1, 3, 10));

        ResultadoFinanciacion resultado =
                resolver(iniciativas, 6);

        comprobar(resultado.getSeleccionadas().size() == 1,
                "La iniciativa solo puede seleccionarse una vez.");

        comprobar(resultado.getImpactoTotal() == 10,
                "El impacto debe ser 10, no 20.");

        comprobar(resultado.getCostoTotal() == 3,
                "El costo debe ser 3, no 6.");
    }

    private void testNingunaEntra() {
        List<Iniciativa> iniciativas = new ArrayList<>();

        iniciativas.add(iniciativa(1, 5, 10));
        iniciativas.add(iniciativa(2, 8, 20));

        ResultadoFinanciacion resultado =
                resolver(iniciativas, 3);

        comprobar(resultado.getSeleccionadas().isEmpty(),
                "No debe seleccionar iniciativas que excedan el presupuesto.");

        comprobar(resultado.getImpactoTotal() == 0,
                "El impacto debe ser 0.");

        comprobar(resultado.getCostoTotal() == 0,
                "El costo total debe ser 0.");

        comprobar(resultado.getPresupuestoRestante() == 3,
                "El presupuesto debe permanecer intacto.");
    }

    private void testListaVacia() {
        List<Iniciativa> iniciativas = new ArrayList<>();

        ResultadoFinanciacion resultado =
                resolver(iniciativas, 10);

        comprobar(resultado.getSeleccionadas().isEmpty(),
                "No debe seleccionar iniciativas.");

        comprobar(resultado.getImpactoTotal() == 0,
                "El impacto debe ser 0.");

        comprobar(resultado.getPresupuestoRestante() == 10,
                "Todo el presupuesto debe permanecer disponible.");
    }

    private void testPresupuestoCero() {
        List<Iniciativa> iniciativas = new ArrayList<>();

        iniciativas.add(iniciativa(1, 1, 10));
        iniciativas.add(iniciativa(2, 2, 20));

        ResultadoFinanciacion resultado =
                resolver(iniciativas, 0);

        comprobar(resultado.getSeleccionadas().isEmpty(),
                "Con presupuesto cero no se puede financiar ninguna.");

        comprobar(resultado.getImpactoTotal() == 0,
                "El impacto debe ser 0.");
    }

    private void testMayorImpacto() {
        List<Iniciativa> iniciativas = new ArrayList<>();

        iniciativas.add(iniciativa(1, 5, 8));
        iniciativas.add(iniciativa(2, 5, 12));

        ResultadoFinanciacion resultado =
                resolver(iniciativas, 5);

        comprobar(resultado.getImpactoTotal() == 12,
                "Debe elegir la iniciativa de impacto 12.");

        comprobar(ids(resultado.getSeleccionadas()).contains(2),
                "Debe seleccionar la iniciativa 2.");
    }

    private void testImpactosIguales() {
        List<Iniciativa> iniciativas = new ArrayList<>();

        iniciativas.add(iniciativa(1, 5, 10));
        iniciativas.add(iniciativa(2, 3, 10));

        ResultadoFinanciacion resultado =
                resolver(iniciativas, 5);

        comprobar(resultado.getImpactoTotal() == 10,
                "El impacto maximo debe ser 10.");

        comprobar(resultado.getCostoTotal() <= 5,
                "No debe superar el presupuesto.");
    }

    private void testNoGastarTodo() {
        List<Iniciativa> iniciativas = new ArrayList<>();

        iniciativas.add(iniciativa(1, 3, 10));
        iniciativas.add(iniciativa(2, 6, 9));

        ResultadoFinanciacion resultado =
                resolver(iniciativas, 6);

        comprobar(resultado.getImpactoTotal() == 10,
                "Debe elegir impacto 10 aunque quede presupuesto.");

        comprobar(resultado.getCostoTotal() == 3,
                "El costo optimo es 3.");

        comprobar(resultado.getPresupuestoRestante() == 3,
                "Deben quedar 3 unidades de presupuesto.");
    }

    // Metodos auxiliares

    private ResultadoFinanciacion resolver(
            List<Iniciativa> iniciativas, int presupuesto) {

        MochilaDinamicaService mochilaService = new MochilaDinamicaService();

        return mochilaService.seleccionarIniciativas(
                iniciativas, presupuesto);
    }

    private Iniciativa iniciativa(int id, int costo, int impacto) {
        return new Iniciativa(id, "Iniciativa " + id, costo, impacto);
    }

    private List<Integer> ids(List<Iniciativa> iniciativas) {
        List<Integer> resultado = new ArrayList<>();

        for (Iniciativa iniciativa : iniciativas) {
            resultado.add(iniciativa.getId());
        }

        return resultado;
    }

    private void ejecutarPrueba(String nombre, Runnable prueba) {
        pruebasEjecutadas++;

        try {
            prueba.run();
            pruebasAprobadas++;
            System.out.println("[OK]   " + nombre);
        } catch (Throwable error) {
            pruebasFallidas++;
            System.out.println("[FALLO] " + nombre);
            System.out.println("       Motivo: " + error.getMessage());
        }
    }

    private void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }
}

