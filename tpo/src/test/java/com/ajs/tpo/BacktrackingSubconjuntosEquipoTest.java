package com.ajs.tpo;

import com.ajs.tpo.model.Equipo;
import com.ajs.tpo.model.RestriccionesEquipo;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.algorithm.BacktrackingSubconjuntosEquipo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Pruebas de B1 (BacktrackingSubconjuntosEquipo). Uso: BacktrackingSubconjuntosEquipoTest.ejecutar() -> true si todo paso
//
// Candidatos base (id nombre: habilidades):
//   1 A: java      2 B: sql      3 C: ui      4 D: java, sql
//
// Con tamanio 2 y habilidades {java, sql}, calculado a mano sobre los 6 pares posibles:
//   AB ok, AC no (falta sql), AD ok, BC no (falta java), BD ok, CD ok (D cubre ambas)
//   -> equipos validos: {1,2} {1,4} {2,4} {3,4}

public class BacktrackingSubconjuntosEquipoTest {

    private static int ok;
    private static int fallos;

    private static class Datos {
        final Usuario a = usuario(1, "A", "java");
        final Usuario b = usuario(2, "B", "sql");
        final Usuario c = usuario(3, "C", "ui");
        final Usuario d = usuario(4, "D", "java", "sql");
        final List<Usuario> todos = new ArrayList<>(List.of(a, b, c, d));
    }

    public static boolean ejecutar() {
        ok = 0;
        fallos = 0;
        System.out.println("=== BacktrackingSubconjuntosEquipoTest (B1) ===");

        caso("Tamanio 2 con {java, sql}: los 4 equipos validos y solo esos", () -> {
            Datos x = new Datos();
            RestriccionesEquipo r = new RestriccionesEquipo(2);
            r.agregarHabilidadRequerida("java");
            r.agregarHabilidadRequerida("sql");
            BacktrackingSubconjuntosEquipo.Resultado res = BacktrackingSubconjuntosEquipo.ejecutar(x.todos, r);
            igual(setDe("1-2", "1-4", "2-4", "3-4"), equipos(res), "equipos");
            todosCumplen(res, r);
        });

        caso("Las habilidades se cubren como UNION: {A(java), B(sql)} es valido", () -> {
            Datos x = new Datos();
            RestriccionesEquipo r = new RestriccionesEquipo(2);
            r.agregarHabilidadRequerida("java");
            r.agregarHabilidadRequerida("sql");
            asegurar(equipos(BacktrackingSubconjuntosEquipo.ejecutar(x.todos, r)).contains("1-2"),
                    "A y B juntos cubren java y sql");
        });

        caso("Sin habilidades requeridas: todas las combinaciones C(4,2) = 6", () -> {
            Datos x = new Datos();
            BacktrackingSubconjuntosEquipo.Resultado res =
                    BacktrackingSubconjuntosEquipo.ejecutar(x.todos, new RestriccionesEquipo(2));
            igual(setDe("1-2", "1-3", "1-4", "2-3", "2-4", "3-4"), equipos(res), "equipos");
        });

        caso("No repite equipos por orden: tamanio 3 da C(4,3) = 4 equipos distintos", () -> {
            Datos x = new Datos();
            BacktrackingSubconjuntosEquipo.Resultado res =
                    BacktrackingSubconjuntosEquipo.ejecutar(x.todos, new RestriccionesEquipo(3));
            igual(4, res.getEquipos().size(), "cantidad devuelta");
            igual(4, equipos(res).size(), "cantidad de equipos distintos");
            igual(setDe("1-2-3", "1-2-4", "1-3-4", "2-3-4"), equipos(res), "equipos");
        });

        caso("Tamanio 1 con {java}: A y D", () -> {
            Datos x = new Datos();
            RestriccionesEquipo r = new RestriccionesEquipo(1);
            r.agregarHabilidadRequerida("java");
            igual(setDe("1", "4"), equipos(BacktrackingSubconjuntosEquipo.ejecutar(x.todos, r)), "equipos");
        });

        caso("Integrante obligatorio C con {java, sql}: solo {C, D}", () -> {
            Datos x = new Datos();
            RestriccionesEquipo r = new RestriccionesEquipo(2);
            r.agregarHabilidadRequerida("java");
            r.agregarHabilidadRequerida("sql");
            r.agregarObligatorio(x.c);
            BacktrackingSubconjuntosEquipo.Resultado res = BacktrackingSubconjuntosEquipo.ejecutar(x.todos, r);
            igual(setDe("3-4"), equipos(res), "equipos");
            todosCumplen(res, r);
        });

        caso("Incompatibles A-B con {java, sql}: quedan {1,4} {2,4} {3,4}", () -> {
            Datos x = new Datos();
            RestriccionesEquipo r = new RestriccionesEquipo(2);
            r.agregarHabilidadRequerida("java");
            r.agregarHabilidadRequerida("sql");
            r.agregarIncompatibilidad(x.a, x.b);
            BacktrackingSubconjuntosEquipo.Resultado res = BacktrackingSubconjuntosEquipo.ejecutar(x.todos, r);
            igual(setDe("1-4", "2-4", "3-4"), equipos(res), "equipos");
            todosCumplen(res, r);
        });

        caso("La incompatibilidad es simetrica (se carga B-A y se cumple igual)", () -> {
            Datos x = new Datos();
            RestriccionesEquipo r = new RestriccionesEquipo(2);
            r.agregarIncompatibilidad(x.b, x.a);
            asegurar(r.sonIncompatibles(x.a, x.b), "A-B debe ser incompatible");
            asegurar(!equipos(BacktrackingSubconjuntosEquipo.ejecutar(x.todos, r)).contains("1-2"),
                    "no puede estar {1,2}");
        });

        caso("Varias incompatibilidades a la vez (A-B y C-D) con tamanio 2", () -> {
            Datos x = new Datos();
            RestriccionesEquipo r = new RestriccionesEquipo(2);
            r.agregarIncompatibilidad(x.a, x.b);
            r.agregarIncompatibilidad(x.c, x.d);
            igual(setDe("1-3", "1-4", "2-3", "2-4"),
                    equipos(BacktrackingSubconjuntosEquipo.ejecutar(x.todos, r)), "equipos");
        });

        caso("Habilidad que nadie tiene: no hay equipos", () -> {
            Datos x = new Datos();
            RestriccionesEquipo r = new RestriccionesEquipo(2);
            r.agregarHabilidadRequerida("go");
            asegurar(!BacktrackingSubconjuntosEquipo.ejecutar(x.todos, r).hayEquipos(), "no debe haber equipos");
        });

        caso("Obligatorios incompatibles entre si: no hay equipos", () -> {
            Datos x = new Datos();
            RestriccionesEquipo r = new RestriccionesEquipo(3);
            r.agregarObligatorio(x.a);
            r.agregarObligatorio(x.b);
            r.agregarIncompatibilidad(x.a, x.b);
            asegurar(!BacktrackingSubconjuntosEquipo.ejecutar(x.todos, r).hayEquipos(), "no debe haber equipos");
        });

        caso("Tamanio mayor a la cantidad de candidatos: sin equipos y sin excepcion", () -> {
            Datos x = new Datos();
            asegurar(!BacktrackingSubconjuntosEquipo.ejecutar(x.todos, new RestriccionesEquipo(5)).hayEquipos(),
                    "no debe haber equipos");
        });

        caso("Tamanio igual a la cantidad de obligatorios: el equipo son los obligatorios", () -> {
            Datos x = new Datos();
            RestriccionesEquipo r = new RestriccionesEquipo(2);
            r.agregarObligatorio(x.a);
            r.agregarObligatorio(x.b);
            r.agregarHabilidadRequerida("java");
            r.agregarHabilidadRequerida("sql");
            igual(setDe("1-2"), equipos(BacktrackingSubconjuntosEquipo.ejecutar(x.todos, r)), "equipos");

            RestriccionesEquipo r2 = new RestriccionesEquipo(2);
            r2.agregarObligatorio(x.a);
            r2.agregarObligatorio(x.b);
            r2.agregarHabilidadRequerida("ui");
            asegurar(!BacktrackingSubconjuntosEquipo.ejecutar(x.todos, r2).hayEquipos(), "no cubren ui");
        });

        caso("Las habilidades se comparan sin importar mayusculas ni espacios", () -> {
            Datos x = new Datos();
            RestriccionesEquipo r = new RestriccionesEquipo(1);
            r.agregarHabilidadRequerida("  JAVA ");
            igual(setDe("1", "4"), equipos(BacktrackingSubconjuntosEquipo.ejecutar(x.todos, r)), "equipos");
        });

        caso("Hay pasos registrados e incluyen los equipos validos", () -> {
            Datos x = new Datos();
            RestriccionesEquipo r = new RestriccionesEquipo(2);
            r.agregarHabilidadRequerida("java");
            BacktrackingSubconjuntosEquipo.Resultado res = BacktrackingSubconjuntosEquipo.ejecutar(x.todos, r);
            asegurar(!res.getPasos().isEmpty(), "debe haber pasos");
            boolean hayEquipoValido = false;
            for (String p : res.getPasos()) {
                if (p.startsWith("Equipo valido")) hayEquipoValido = true;
            }
            asegurar(hayEquipoValido, "algun paso debe informar un equipo valido");
        });

        caso("Con muchos candidatos los pasos se acotan a MAX_PASOS y los equipos no", () -> {
            List<Usuario> muchos = new ArrayList<>();
            for (int i = 1; i <= 12; i++) muchos.add(usuario(i, "U" + i, "java"));
            BacktrackingSubconjuntosEquipo.Resultado res =
                    BacktrackingSubconjuntosEquipo.ejecutar(muchos, new RestriccionesEquipo(4));
            igual(495, res.getEquipos().size(), "C(12,4) equipos"); // 12*11*10*9 / 24 = 495
            igual(BacktrackingSubconjuntosEquipo.MAX_PASOS, res.getPasos().size(), "pasos guardados");
        });

        caso("Datos invalidos lanzan IllegalArgumentException", () -> {
            Datos x = new Datos();
            Usuario fuera = usuario(99, "Fuera", "java");

            RestriccionesEquipo conFuera = new RestriccionesEquipo(2);
            conFuera.agregarObligatorio(fuera); // obligatorio que no esta entre los candidatos
            lanzaIAE(() -> BacktrackingSubconjuntosEquipo.ejecutar(x.todos, conFuera),
                    "obligatorio fuera de candidatos");

            lanzaIAE(() -> new RestriccionesEquipo(0), "tamanio 0");
            lanzaIAE(() -> {
                RestriccionesEquipo r = new RestriccionesEquipo(1);
                r.agregarObligatorio(x.a);
                r.agregarObligatorio(x.b); // mas obligatorios que el tamanio
            }, "mas obligatorios que el tamanio");
            lanzaIAE(() -> new RestriccionesEquipo(2).agregarIncompatibilidad(x.a, x.a), "incompatible consigo mismo");
            lanzaIAE(() -> new RestriccionesEquipo(2).agregarHabilidadRequerida(" "), "habilidad vacia");
            lanzaIAE(() -> BacktrackingSubconjuntosEquipo.ejecutar(null, new RestriccionesEquipo(2)),
                    "candidatos nulos");
            lanzaIAE(() -> BacktrackingSubconjuntosEquipo.ejecutar(x.todos, null), "restricciones nulas");
        });

        return resumen();
    }

    // ---------- Helpers de dominio ----------

    private static Usuario usuario(int id, String nombre, String... habilidades) {
        Usuario u = new Usuario(id, nombre);
        for (String h : habilidades) u.agregarHabilidad(h);
        return u;
    }

    // Clave de un equipo: ids ordenados, ej. "1-4" (Equipo ya itera ordenado por id)
    private static String clave(Equipo e) {
        StringBuilder sb = new StringBuilder();
        for (Usuario u : e.getIntegrantes()) {
            if (sb.length() > 0) sb.append("-");
            sb.append(u.getId());
        }
        return sb.toString();
    }

    private static Set<String> equipos(BacktrackingSubconjuntosEquipo.Resultado res) {
        Set<String> s = new HashSet<>();
        for (Equipo e : res.getEquipos()) s.add(clave(e));
        return s;
    }

    private static Set<String> setDe(String... claves) {
        Set<String> s = new HashSet<>();
        for (String c : claves) s.add(c);
        return s;
    }

    // Verifica de forma independiente que cada equipo devuelto cumple TODAS las restricciones
    private static void todosCumplen(BacktrackingSubconjuntosEquipo.Resultado res, RestriccionesEquipo r) {
        for (Equipo e : res.getEquipos()) {
            igual(r.getTamanio(), e.getTamanio(), "tamanio de " + e);
            for (Usuario o : r.getObligatorios())
                asegurar(e.contiene(o), e + " no incluye al obligatorio " + o.getNombre());
            Set<String> cubiertas = new HashSet<>();
            for (Usuario u : e.getIntegrantes()) cubiertas.addAll(u.getHabilidades());
            asegurar(cubiertas.containsAll(r.getHabilidadesRequeridas()),
                    e + " no cubre las habilidades requeridas");
            for (Usuario u1 : e.getIntegrantes()) {
                for (Usuario u2 : e.getIntegrantes()) {
                    asegurar(!r.sonIncompatibles(u1, u2),
                            e + " tiene incompatibles " + u1.getNombre() + "-" + u2.getNombre());
                }
            }
        }
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

    private static void lanzaIAE(Runnable r, String que) {
        try {
            r.run();
        } catch (IllegalArgumentException e) {
            return;
        }
        throw new AssertionError(que + ": se esperaba IllegalArgumentException");
    }

    private static boolean resumen() {
        System.out.println("--- Resultado: " + ok + " OK, " + fallos + " con fallo ---\n");
        return fallos == 0;
    }

    public static void main(String[] args) {
        System.exit(ejecutar() ? 0 : 1);
    }
}