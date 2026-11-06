package com.ajs.tpo;

import com.ajs.tpo.algorithm.MergeSortPublicaciones;
import com.ajs.tpo.enums.CriterioOrdenamientoPublicaciones;
import com.ajs.tpo.model.Publicacion;
import com.ajs.tpo.model.Usuario;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

// Pruebas de Merge Sort del muro. Uso: MergeSortPublicacionesTest.ejecutar() -> true si todo paso
//
// Relevancia = reacciones + 3 * comentarios
// POR_FECHA:      fecha mas reciente primero; empate -> menor id
// POR_RELEVANCIA: mayor relevancia primero; empate -> fecha mas reciente; empate -> menor id

public class MergeSortPublicacionesTest {

    private static final CriterioOrdenamientoPublicaciones REL = CriterioOrdenamientoPublicaciones.POR_RELEVANCIA;
    private static final CriterioOrdenamientoPublicaciones FEC = CriterioOrdenamientoPublicaciones.POR_FECHA;
    private static final LocalDateTime BASE = LocalDateTime.of(2026, 1, 1, 10, 0);
    private static final Usuario AUTOR = new Usuario(1, "yo");

    private static int ok;
    private static int fallos;

    public static boolean ejecutar() {
        ok = 0;
        fallos = 0;
        System.out.println("=== MergeSortPublicacionesTest ===");

        caso("Relevancias 9, 8, 5, 5 (ids 3, 4, 1, 2) ya ordenado: no duplica ni pierde (bug anterior)", () -> {
            Publicacion[] arr = {
                    pub(3, 9, BASE, 0), pub(4, 8, BASE, 0), pub(1, 5, BASE, 0), pub(2, 5, BASE, 0)
            };
            MergeSortPublicaciones.mergeSort(arr,0,arr.length-1, REL);
            igual("3-4-1-2", ids(arr), "orden");
        });

        caso("Relevancia cuenta comentarios (x3) y desempata por fecha mas reciente", () -> {
            // A: 10 reacc. = 10 | B: 1 reacc + 3 coment. = 10 (mas reciente que A) | C: 4 coment. = 12
            Publicacion a = pub(1, 10, BASE, 0);
            Publicacion b = pub(2, 1, BASE.plusHours(1), 3);
            Publicacion c = pub(3, 0, BASE.minusDays(1), 4);
            igual(10, a.getRelevancia(), "relevancia A");
            igual(10, b.getRelevancia(), "relevancia B");
            igual(12, c.getRelevancia(), "relevancia C");
            Publicacion[] arr = { a, b, c };
            MergeSortPublicaciones.mergeSort(arr,0,arr.length-1, REL);
            igual("3-2-1", ids(arr), "orden"); // C(12), B(10 reciente), A(10)
        });

        caso("Empate de relevancia y fecha: menor id primero", () -> {
            Publicacion[] arr = { pub(5, 7, BASE, 0), pub(3, 7, BASE, 0), pub(4, 7, BASE, 0) };
            MergeSortPublicaciones.mergeSort(arr,0,arr.length-1, REL);
            igual("3-4-5", ids(arr), "orden");
        });

        caso("POR_FECHA: mas reciente primero; con misma fecha, menor id", () -> {
            Publicacion p1 = pub(1, 0, BASE, 0);
            Publicacion p2 = pub(2, 0, BASE.plusDays(2), 0);
            Publicacion p3 = pub(3, 0, BASE.plusDays(1), 0);
            Publicacion p4 = pub(4, 0, BASE.plusDays(2), 0); // misma fecha que p2
            Publicacion[] arr = { p3, p1, p4, p2 };
            MergeSortPublicaciones.mergeSort(arr,0,arr.length-1, FEC);
            igual("2-4-3-1", ids(arr), "orden");
        });

        caso("POR_FECHA ignora la relevancia", () -> {
            Publicacion viejaPopular = pub(1, 1000, BASE, 0);
            Publicacion nuevaSinNada = pub(2, 0, BASE.plusDays(1), 0);
            Publicacion[] arr = { viejaPopular, nuevaSinNada };
            MergeSortPublicaciones.mergeSort(arr,0,arr.length-1, FEC);
            igual("2-1", ids(arr), "orden");
        });

        caso("Orden inverso completo queda ordenado", () -> {
            Publicacion[] arr = {
                    pub(1, 1, BASE, 0), pub(2, 2, BASE, 0), pub(3, 3, BASE, 0),
                    pub(4, 4, BASE, 0), pub(5, 5, BASE, 0)
            };
            MergeSortPublicaciones.mergeSort(arr,0,arr.length-1, REL);
            igual("5-4-3-2-1", ids(arr), "orden");
        });

        caso("Arreglo vacio, de un elemento y nulo no fallan", () -> {
            Publicacion[] vacio = {};
            MergeSortPublicaciones.mergeSort(vacio,0,vacio.length-1, REL);
            igual(0, vacio.length, "vacio");

            Publicacion[] uno = { pub(1, 3, BASE, 0) };
            MergeSortPublicaciones.mergeSort(uno,0,uno.length-1, FEC);
            igual("1", ids(uno), "un elemento");

            MergeSortPublicaciones.mergeSort(null,0,0, REL);
        });

        caso("La version con rango solo ordena [ini, fin]", () -> {
            Publicacion[] arr = { pub(1, 1, BASE, 0), pub(2, 3, BASE, 0), pub(3, 2, BASE, 0), pub(4, 9, BASE, 0) };
            MergeSortPublicaciones.mergeSort(arr, 0, 2, REL);
            igual("2-3-1-4", ids(arr), "orden"); // el ultimo no se toca
        });

        caso("100 publicaciones aleatorias (semilla fija): orden correcto, sin perder ni repetir ids", () -> {
            for (CriterioOrdenamientoPublicaciones criterio : new CriterioOrdenamientoPublicaciones[] { REL, FEC }) {
                Publicacion[] arr = aleatorias(100, 42);
                MergeSortPublicaciones.mergeSort(arr,0,arr.length-1, criterio);

                Set<Integer> distintos = new HashSet<>();
                for (Publicacion p : arr) distintos.add(p.getId());
                igual(100, distintos.size(), "ids distintos con " + criterio);

                for (int i = 0; i < arr.length - 1; i++) {
                    asegurar(vaAntesOIgual(arr[i], arr[i + 1], criterio),
                            "fuera de orden en posicion " + i + " (" + criterio + "): ids "
                                    + arr[i].getId() + " y " + arr[i + 1].getId());
                }
            }
        });

        return resumen();
    }

    // ---------- Helpers de dominio ----------

    private static Publicacion pub(int id, int reacciones, LocalDateTime fecha, int comentarios) {
        Publicacion p = new Publicacion(id, AUTOR, fecha, reacciones);
        for (int i = 0; i < comentarios; i++) p.agregarComentario(AUTOR, "comentario " + i, fecha);
        return p;
    }

    private static Publicacion[] aleatorias(int cantidad, long semilla) {
        Random rnd = new Random(semilla);
        Publicacion[] arr = new Publicacion[cantidad];
        for (int i = 0; i < cantidad; i++) {
            // pocos valores posibles para forzar muchos empates
            arr[i] = pub(i + 1, rnd.nextInt(5), BASE.plusHours(rnd.nextInt(4)), rnd.nextInt(3));
        }
        return arr;
    }

    // Verificacion independiente del algoritmo: a puede estar antes que b (o son equivalentes)
    private static boolean vaAntesOIgual(Publicacion a, Publicacion b, CriterioOrdenamientoPublicaciones c) {
        if (c == REL && a.getRelevancia() != b.getRelevancia()) return a.getRelevancia() > b.getRelevancia();
        if (!a.getFechaHora().equals(b.getFechaHora())) return a.getFechaHora().isAfter(b.getFechaHora());
        return a.getId() <= b.getId();
    }

    private static String ids(Publicacion[] arr) {
        StringBuilder sb = new StringBuilder();
        for (Publicacion p : arr) {
            if (sb.length() > 0) sb.append("-");
            sb.append(p.getId());
        }
        return sb.toString();
    }

    // ---------- Mini framework ----------

    private static void caso(String nombre, Runnable prueba) {
        try {
            prueba.run();
            System.out.println("[OK]     " + nombre);
            ok++;
        } catch (AssertionError e) {
            System.out.println("[FALLO]  " + nombre + "\n           -> " + e.getMessage());
            fallos++;
        } catch (Exception e) {
            System.out.println("[ERROR]  " + nombre + "\n           -> excepcion inesperada: " + e);
            fallos++;
        }
    }

    private static void asegurar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }

    private static void igual(Object esperado, Object actual, String que) {
        if (!esperado.equals(actual)) {
            throw new AssertionError(que + ": esperado " + esperado + " pero fue " + actual);
        }
    }

    private static boolean resumen() {
        System.out.println("--- Resultado: " + ok + " OK, " + fallos + " con fallo ---\n");
        return fallos == 0;
    }

    public static void main(String[] args) {
        System.exit(ejecutar() ? 0 : 1);
    }
}