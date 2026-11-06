package com.ajs.tpo;

import com.ajs.tpo.algorithm.BacktrackingCadenasDifusion;
import com.ajs.tpo.model.GrafoUsuarios;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.repository.UsuarioRepository;
import com.ajs.tpo.service.CadenasAlternativasService;

import java.util.*;

public class BacktrackingCadenasDifusionTest {

    private static int ok;
    private static int fallos;

    private static class Datos {

        final GrafoUsuarios grafo = new GrafoUsuarios();

        final Usuario a = agregar(1, "A");
        final Usuario b = agregar(2, "B");
        final Usuario c = agregar(3, "C");
        final Usuario d = agregar(4, "D");

        private Usuario agregar(int id, String nombre) {
            Usuario usuario = new Usuario(id, nombre);
            grafo.agregarUsuario(usuario);
            return usuario;
        }

        Datos() {
            conectar(a, b, 2);
            conectar(a, c, 1);
            conectar(a, d, 8);
            conectar(b, c, 1);
            conectar(b, d, 4);
            conectar(c, d, 2);
            conectar(c, a, 1);
        }

        void conectar(Usuario origen, Usuario destino, int tiempo) {
            grafo.agregarConexion(origen, destino, tiempo);
        }

        BacktrackingCadenasDifusion.Resultado buscar(
                int tiempoMaximo,
                int maxIntermediarios) {

            return BacktrackingCadenasDifusion.ejecutar(
                    grafo, a, d, tiempoMaximo, maxIntermediarios, Set.of()
            );
        }
    }

    public static boolean ejecutar() {

        ok = 0;
        fallos = 0;

        System.out.println("=== BacktrackingCadenasDifusionTest (B3) ===");

        // PRUEBA 1: Encontrar todas las cadenas y la de menor tiempo.
        caso("Encuentra todas las cadenas y calcula la de menor tiempo", () -> {

            Datos x = new Datos();

            BacktrackingCadenasDifusion.Resultado resultado =
                    x.buscar(20, 2);

            igual(4, resultado.getCadenas().size(),
                    "cantidad de cadenas");

            igual(
                    setDe(
                            "1-4=8",
                            "1-2-4=6",
                            "1-3-4=3",
                            "1-2-3-4=5"
                    ),
                    cadenas(resultado),
                    "cadenas y tiempos"
            );

            igual(
                    List.of(1, 3, 4),
                    ids(resultado.getCadenaMenorTiempo()
                            .orElseThrow().getUsuarios()),
                    "cadena de menor tiempo"
            );

            igual(
                    3,
                    resultado.getCadenaMenorTiempo()
                            .orElseThrow().getTiempoTotal(),
                    "tiempo minimo"
            );

            System.out.println("   Cadenas encontradas:");

            for (BacktrackingCadenasDifusion.Cadena cadena
                    : resultado.getCadenas()) {

                System.out.println("      " + cadena);
            }

            System.out.println(
                    "   Menor tiempo: "
                            + resultado.getCadenaMenorTiempo().orElseThrow()
            );

            todasSonSimples(resultado);
        });

        // PRUEBA 2: Respetar el tiempo maximo.
        caso("Respeta el tiempo maximo y permite igualdad con el limite", () -> {

            Datos x = new Datos();

            BacktrackingCadenasDifusion.Resultado resultado =
                    x.buscar(5, 2);

            igual(
                    setDe("1-3-4=3", "1-2-3-4=5"),
                    cadenas(resultado),
                    "cadenas dentro de 5 minutos"
            );
        });

        // PRUEBA 3: Limite de cero intermediarios.
        caso("Limite cero de intermediarios permite solamente la conexion directa", () -> {

            Datos x = new Datos();

            BacktrackingCadenasDifusion.Resultado resultado =
                    x.buscar(20, 0);

            igual(
                    setDe("1-4=8"),
                    cadenas(resultado),
                    "camino directo"
            );
        });

        // PRUEBA 4: Limite de un intermediario.
        caso("Limite de un intermediario descarta cadenas mas largas", () -> {

            Datos x = new Datos();

            BacktrackingCadenasDifusion.Resultado resultado =
                    x.buscar(20, 1);

            igual(
                    setDe("1-4=8", "1-2-4=6", "1-3-4=3"),
                    cadenas(resultado),
                    "cadenas con hasta un intermediario"
            );
        });

        // PRUEBA 5: Usuarios excluidos.
        caso("No atraviesa usuarios excluidos", () -> {

            Datos x = new Datos();

            BacktrackingCadenasDifusion.Resultado resultado =
                    BacktrackingCadenasDifusion.ejecutar(
                            x.grafo,
                            x.a,
                            x.d,
                            20,
                            2,
                            Set.of(x.c)
                    );

            igual(
                    setDe("1-4=8", "1-2-4=6"),
                    cadenas(resultado),
                    "cadenas sin C"
            );
        });

        // PRUEBA 6: Conexiones deshabilitadas.
        caso("Ignora conexiones deshabilitadas", () -> {

            Datos x = new Datos();

            asegurar(
                    x.grafo.setHabilitada(x.a, x.d, false),
                    "debe deshabilitar A-D"
            );

            BacktrackingCadenasDifusion.Resultado resultado =
                    x.buscar(20, 2);

            asegurar(
                    !cadenas(resultado).contains("1-4=8"),
                    "no debe aparecer A-D"
            );

            igual(
                    3,
                    resultado.getCadenas().size(),
                    "cadenas restantes"
            );
        });

        // PRUEBA 7: No existen caminos validos.
        caso("Sin caminos dentro del tiempo devuelve resultado vacio", () -> {

            Datos x = new Datos();

            BacktrackingCadenasDifusion.Resultado resultado =
                    x.buscar(2, 2);

            asegurar(
                    !resultado.hayCadenas(),
                    "no debe haber cadenas"
            );

            asegurar(
                    resultado.getCadenaMenorTiempo().isEmpty(),
                    "no debe existir minimo"
            );
        });

        // PRUEBA 8: Origen y destino coincidentes.
        caso("Si origen y destino coinciden, la cadena tiene tiempo cero", () -> {

            Datos x = new Datos();

            BacktrackingCadenasDifusion.Resultado resultado =
                    BacktrackingCadenasDifusion.ejecutar(
                            x.grafo,
                            x.a,
                            x.a,
                            0,
                            0,
                            Set.of()
                    );

            igual(
                    1,
                    resultado.getCadenas().size(),
                    "cantidad"
            );

            igual(
                    List.of(1),
                    ids(resultado.getCadenas().get(0).getUsuarios()),
                    "cadena"
            );

            igual(
                    0,
                    resultado.getCadenas().get(0).getTiempoTotal(),
                    "tiempo"
            );
        });

        // PRUEBA 9: Origen o destino excluido.
        caso("Origen o destino excluido impide obtener cadenas", () -> {

            Datos x = new Datos();

            BacktrackingCadenasDifusion.Resultado resultado =
                    BacktrackingCadenasDifusion.ejecutar(
                            x.grafo,
                            x.a,
                            x.d,
                            20,
                            2,
                            Set.of(x.a)
                    );

            asegurar(
                    !resultado.hayCadenas(),
                    "no debe haber cadenas si el origen esta excluido"
            );
        });

        // PRUEBA 10: Usuario excluido inexistente.
        caso("Rechaza un usuario excluido que no pertenece al grafo", () -> {

            Datos x = new Datos();

            Usuario externo = new Usuario(99, "Externo");

            lanzaIAE(() ->
                    BacktrackingCadenasDifusion.ejecutar(
                            x.grafo,
                            x.a,
                            x.d,
                            20,
                            2,
                            Set.of(externo)
                    )
            );
        });

        // PRUEBA 11: Parametros invalidos.
        caso("Rechaza parametros invalidos", () -> {

            Datos x = new Datos();

            lanzaIAE(() ->
                    BacktrackingCadenasDifusion.ejecutar(
                            x.grafo, x.a, x.d, -1, 2
                    )
            );

            lanzaIAE(() ->
                    BacktrackingCadenasDifusion.ejecutar(
                            x.grafo, x.a, x.d, 10, -1
                    )
            );

            lanzaIAE(() ->
                    BacktrackingCadenasDifusion.ejecutar(
                            null, x.a, x.d, 10, 1
                    )
            );
        });

        // PRUEBA 12: Integracion con el Service.
        caso("El servicio resuelve IDs y aplica IDs excluidos", () -> {

            Datos x = new Datos();

            UsuarioRepository repositorio = new UsuarioRepository();

            for (Usuario usuario : x.grafo.getUsuarios()) {
                repositorio.agregar(usuario);
            }

            CadenasAlternativasService servicio =
                    new CadenasAlternativasService(
                            x.grafo,
                            repositorio
                    );

            BacktrackingCadenasDifusion.Resultado resultado =
                    servicio.buscarCadenasAlternativas(
                            1,
                            4,
                            20,
                            2,
                            Set.of(3)
                    );

            igual(
                    setDe("1-4=8", "1-2-4=6"),
                    cadenas(resultado),
                    "cadenas despues de excluir ID 3"
            );

            lanzaIAE(() ->
                    servicio.buscarCadenasAlternativas(
                            1, 4, 20, 2, Set.of(99)
                    )
            );
        });

        System.out.println(
                "--- Resultado: " + ok
                        + " OK, " + fallos + " con fallo ---\n"
        );

        return fallos == 0;
    }

    // Convierte las cadenas en un formato comparable.
    private static Set<String> cadenas(
            BacktrackingCadenasDifusion.Resultado resultado) {

        Set<String> claves = new HashSet<>();

        for (BacktrackingCadenasDifusion.Cadena cadena
                : resultado.getCadenas()) {

            StringBuilder clave = new StringBuilder();

            for (Usuario usuario : cadena.getUsuarios()) {

                if (clave.length() > 0) {
                    clave.append('-');
                }

                clave.append(usuario.getId());
            }

            clave.append('=').append(cadena.getTiempoTotal());

            claves.add(clave.toString());
        }

        return claves;
    }

    // Comprueba que ninguna cadena repita usuarios.
    private static void todasSonSimples(
            BacktrackingCadenasDifusion.Resultado resultado) {

        for (BacktrackingCadenasDifusion.Cadena cadena
                : resultado.getCadenas()) {

            List<Integer> ids = ids(cadena.getUsuarios());

            igual(
                    ids.size(),
                    new HashSet<>(ids).size(),
                    "usuarios no repetidos en " + ids
            );
        }
    }

    // Obtiene los IDs de una lista de usuarios.
    private static List<Integer> ids(List<Usuario> usuarios) {

        List<Integer> ids = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            ids.add(usuario.getId());
        }

        return ids;
    }

    // Construye un conjunto esperado.
    private static Set<String> setDe(String... valores) {
        return new HashSet<>(Arrays.asList(valores));
    }

    // Ejecuta individualmente cada prueba y registra su resultado.
    private static void caso(String nombre, Runnable prueba) {

        try {
            prueba.run();

            System.out.println("[OK]     " + nombre);
            ok++;

        } catch (Throwable error) {

            System.out.println(
                    "[FALLO]  " + nombre
                            + "\n           -> " + error.getMessage()
            );

            fallos++;
        }
    }

    // Verifica una condicion.
    private static void asegurar(boolean condicion, String mensaje) {

        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    // Compara un resultado esperado con el obtenido.
    private static void igual(
            Object esperado,
            Object actual,
            String que) {

        if (!esperado.equals(actual)) {

            throw new AssertionError(
                    que + ": esperado " + esperado
                            + " pero fue " + actual
            );
        }
    }

    // Comprueba que una operacion lance IllegalArgumentException.
    private static void lanzaIAE(Runnable operacion) {

        try {

            operacion.run();

        } catch (IllegalArgumentException esperado) {
            return;
        }

        throw new AssertionError(
                "Se esperaba IllegalArgumentException"
        );
    }

    public static void main(String[] args) {
        System.exit(ejecutar() ? 0 : 1);
    }
}
