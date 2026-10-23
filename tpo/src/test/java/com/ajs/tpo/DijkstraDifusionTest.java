
package test.java.com.ajs.tpo;

import com.ajs.tpo.algorithm.DijkstraDifusion;
import com.ajs.tpo.algorithm.DijkstraDifusion.Resultado;
import com.ajs.tpo.model.GrafoUsuarios;
import com.ajs.tpo.model.Usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DijkstraDifusionTest {

    private int pruebasEjecutadas;
    private int pruebasAprobadas;
    private int pruebasFallidas;

    // Ejecucion automatica de pruebas y muestra en consola

    public void ejecutar() {
        pruebasEjecutadas = 0;
        pruebasAprobadas = 0;
        pruebasFallidas = 0;

        System.out.println();
        System.out.println("========================================");
        System.out.println(" TESTS: DIJKSTRA DIFUSION");
        System.out.println("========================================");

        ejecutarPrueba(
                "Camino minimo con alternativas",
                this::testCaminoMinimo
        );

        ejecutarPrueba(
                "Camino directo frente a camino indirecto",
                this::testDirectoFrenteIndirecto
        );

        ejecutarPrueba(
                "Conexion deshabilitada con ruta alternativa",
                this::testConexionDeshabilitadaConAlternativa
        );

        ejecutarPrueba(
                "Destino inalcanzable",
                this::testDestinoInalcanzable
        );

        ejecutarPrueba(
                "Origen igual a destino",
                this::testOrigenIgualDestino
        );

        ejecutarPrueba(
                "Empate entre caminos de igual costo",
                this::testEmpateEntreCaminos
        );

        ejecutarPrueba(
                "Actualizacion de distancia y entradas obsoletas",
                this::testActualizacionDeDistancia
        );

        ejecutarPrueba(
                "Origen aislado",
                this::testOrigenAislado
        );

        ejecutarPrueba(
                "Calculo desde un origen hacia todos los alcanzables",
                this::testCalculoHaciaTodos
        );

        ejecutarPrueba(
                "Destino inalcanzable por conexion deshabilitada",
                this::testSoloConexionDeshabilitada
        );

        ejecutarPrueba(
                "Grafo nulo",
                this::testGrafoNulo
        );

        ejecutarPrueba(
                "Origen inexistente en el grafo",
                this::testOrigenInexistente
        );

        ejecutarPrueba(
                "Destino inexistente en el grafo",
                this::testDestinoInexistente
        );

        System.out.println();
        System.out.println("========================================");
        System.out.println(" RESUMEN");
        System.out.println("========================================");
        System.out.println("Pruebas ejecutadas: " + pruebasEjecutadas);
        System.out.println("Aprobadas:          " + pruebasAprobadas);
        System.out.println("Fallidas:           " + pruebasFallidas);
        System.out.println("Resultado: " +
                (pruebasFallidas == 0 ? "TODAS APROBADAS" : "HAY FALLOS"));
        System.out.println("========================================");
    }

    // Casos de prueba

    private void testCaminoMinimo() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2, 3, 4);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);
        Usuario c = usuario(grafo, 3);
        Usuario d = usuario(grafo, 4);

        conectar(grafo, a, b, 8);
        conectar(grafo, a, c, 2);
        conectar(grafo, c, b, 1);
        conectar(grafo, b, d, 2);
        conectar(grafo, c, d, 9);

        Resultado resultado = DijkstraDifusion.ejecutar(grafo, a, d);

        comprobar(resultado.esAlcanzable(d),
                "El destino deberia ser alcanzable.");

        comprobar(resultado.getTiempo(d) == 5,
                "El tiempo minimo esperado es 5, pero fue "
                        + resultado.getTiempo(d) + ".");

        comprobar(ids(resultado.reconstruirCamino(d))
                        .equals(Arrays.asList(1, 3, 2, 4)),
                "El camino esperado es [1, 3, 2, 4], pero fue "
                        + ids(resultado.reconstruirCamino(d)) + ".");
    }

    private void testDirectoFrenteIndirecto() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2, 3);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);
        Usuario c = usuario(grafo, 3);

        conectar(grafo, a, c, 10);
        conectar(grafo, a, b, 3);
        conectar(grafo, b, c, 4);

        Resultado resultado = DijkstraDifusion.ejecutar(grafo, a, c);

        comprobar(resultado.getTiempo(c) == 7,
                "Debe elegir el camino indirecto de costo 7, no el directo de costo 10.");

        comprobar(ids(resultado.reconstruirCamino(c))
                        .equals(Arrays.asList(1, 2, 3)),
                "El camino esperado es [1, 2, 3].");
    }

    private void testConexionDeshabilitadaConAlternativa() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2, 3, 4);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);
        Usuario c = usuario(grafo, 3);
        Usuario d = usuario(grafo, 4);

        conectar(grafo, a, b, 1);
        conectar(grafo, a, c, 5);
        conectar(grafo, c, b, 2);
        conectar(grafo, b, d, 1);

        // Se deshabilita la conexion directa A -> B.
        comprobar(grafo.setHabilitada(a, b, false),
                "La conexion A -> B deberia poder deshabilitarse.");

        Resultado resultado = DijkstraDifusion.ejecutar(grafo, a, d);

        comprobar(resultado.esAlcanzable(d),
                "Debe existir una ruta alternativa hacia D.");

        comprobar(resultado.getTiempo(d) == 8,
                "El tiempo esperado es 8 utilizando A -> C -> B -> D.");

        comprobar(ids(resultado.reconstruirCamino(d))
                        .equals(Arrays.asList(1, 3, 2, 4)),
                "Debe evitar la conexion deshabilitada.");
    }

    private void testDestinoInalcanzable() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2, 3, 4);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);
        Usuario c = usuario(grafo, 3);
        Usuario d = usuario(grafo, 4);

        conectar(grafo, a, b, 2);
        conectar(grafo, c, d, 1);

        Resultado resultado = DijkstraDifusion.ejecutar(grafo, a, d);

        comprobar(!resultado.esAlcanzable(d),
                "El destino D no debe ser alcanzable desde A.");

        comprobar(resultado.getTiempo(d) == Integer.MAX_VALUE,
                "Un destino inalcanzable debe devolver INFINITO.");

        comprobar(resultado.reconstruirCamino(d).isEmpty(),
                "El camino de un destino inalcanzable debe estar vacio.");
    }

    private void testOrigenIgualDestino() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);

        conectar(grafo, a, b, 5);

        Resultado resultado = DijkstraDifusion.ejecutar(grafo, a, a);

        comprobar(resultado.esAlcanzable(a),
                "El origen debe ser alcanzable desde si mismo.");

        comprobar(resultado.getTiempo(a) == 0,
                "El tiempo desde el origen hasta si mismo debe ser 0.");

        comprobar(ids(resultado.reconstruirCamino(a))
                        .equals(List.of(1)),
                "El camino debe contener solamente el origen.");
    }

    private void testEmpateEntreCaminos() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2, 3, 4);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);
        Usuario c = usuario(grafo, 3);
        Usuario d = usuario(grafo, 4);

        conectar(grafo, a, b, 2);
        conectar(grafo, b, d, 3);

        conectar(grafo, a, c, 1);
        conectar(grafo, c, d, 4);

        Resultado resultado = DijkstraDifusion.ejecutar(grafo, a, d);
        List<Integer> camino = ids(resultado.reconstruirCamino(d));

        comprobar(resultado.getTiempo(d) == 5,
                "Ambos caminos tienen costo 5.");

        comprobar(
                camino.equals(Arrays.asList(1, 2, 4))
                        || camino.equals(Arrays.asList(1, 3, 4)),
                "Debe devolver uno de los dos caminos validos de costo 5."
        );
    }

    private void testActualizacionDeDistancia() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2, 3, 4);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);
        Usuario c = usuario(grafo, 3);
        Usuario d = usuario(grafo, 4);

        conectar(grafo, a, b, 10);
        conectar(grafo, a, c, 1);
        conectar(grafo, c, b, 1);
        conectar(grafo, b, d, 1);
        conectar(grafo, a, d, 20);

        Resultado resultado = DijkstraDifusion.ejecutar(grafo, a, d);

        comprobar(resultado.getTiempo(d) == 3,
                "La distancia debe actualizarse al encontrar una ruta mejor.");

        comprobar(ids(resultado.reconstruirCamino(d))
                        .equals(Arrays.asList(1, 3, 2, 4)),
                "Debe reconstruir el camino actualizado [1, 3, 2, 4].");
    }

    private void testOrigenAislado() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);

        Resultado resultado = DijkstraDifusion.ejecutar(grafo, a, b);

        comprobar(!resultado.esAlcanzable(b),
                "Un nodo aislado no debe alcanzar a otro nodo.");

        comprobar(resultado.getTiempo(a) == 0,
                "La distancia del origen debe ser 0.");

        comprobar(resultado.reconstruirCamino(b).isEmpty(),
                "No debe reconstruirse un camino hacia el nodo aislado.");
    }

    private void testCalculoHaciaTodos() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2, 3, 4, 5);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);
        Usuario c = usuario(grafo, 3);
        Usuario d = usuario(grafo, 4);
        Usuario e = usuario(grafo, 5);

        conectar(grafo, a, b, 2);
        conectar(grafo, a, c, 7);
        conectar(grafo, b, c, 1);
        conectar(grafo, c, d, 3);

        // Sobrecarga sin destino: calcula desde A hacia todos
        // los usuarios alcanzables.
        Resultado resultado = DijkstraDifusion.ejecutar(grafo, a);

        comprobar(resultado.getTiempo(a) == 0,
                "La distancia a A debe ser 0.");

        comprobar(resultado.getTiempo(b) == 2,
                "La distancia minima a B debe ser 2.");

        comprobar(resultado.getTiempo(c) == 3,
                "La distancia minima a C debe ser 3.");

        comprobar(resultado.getTiempo(d) == 6,
                "La distancia minima a D debe ser 6.");

        comprobar(!resultado.esAlcanzable(e),
                "E debe permanecer inalcanzable desde A.");
    }

    private void testSoloConexionDeshabilitada() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);

        conectar(grafo, a, b, 3);

        comprobar(grafo.setHabilitada(a, b, false),
                "La conexion deberia deshabilitarse.");

        Resultado resultado = DijkstraDifusion.ejecutar(grafo, a, b);

        comprobar(!resultado.esAlcanzable(b),
                "Una conexion deshabilitada no debe permitir alcanzar B.");

        comprobar(resultado.reconstruirCamino(b).isEmpty(),
                "No debe existir un camino valido.");
    }

    private void testGrafoNulo() {
        Usuario a = new Usuario(1, "Usuario 1");

        esperarIllegalArgument(
                () -> DijkstraDifusion.ejecutar(null, a, null),
                "Un grafo nulo debe rechazarse."
        );
    }

    private void testOrigenInexistente() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2);

        Usuario inexistente = new Usuario(99, "Inexistente");
        Usuario destino = usuario(grafo, 2);

        esperarIllegalArgument(
                () -> DijkstraDifusion.ejecutar(grafo, inexistente, destino),
                "Un origen que no pertenece al grafo debe rechazarse."
        );
    }

    private void testDestinoInexistente() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2);

        Usuario origen = usuario(grafo, 1);
        Usuario inexistente = new Usuario(99, "Inexistente");

        esperarIllegalArgument(
                () -> DijkstraDifusion.ejecutar(grafo, origen, inexistente),
                "Un destino que no pertenece al grafo debe rechazarse."
        );
    }

    // helpers privados

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

    private void esperarIllegalArgument(Runnable operacion, String mensaje) {
        try {
            operacion.run();
        } catch (IllegalArgumentException esperado) {
            return;
        }

        throw new AssertionError(mensaje);
    }

    private GrafoUsuarios nuevoGrafo(int... ids) {
        GrafoUsuarios grafo = new GrafoUsuarios();

        for (int id : ids) {
            grafo.agregarUsuario(new Usuario(id, "Usuario " + id));
        }

        return grafo;
    }

    private Usuario usuario(GrafoUsuarios grafo, int id) {
        for (Usuario usuario : grafo.getUsuarios()) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }

        throw new AssertionError("No existe el usuario con ID " + id);
    }

    private void conectar(
            GrafoUsuarios grafo,
            Usuario origen,
            Usuario destino,
            int tiempo
    ) {
        grafo.agregarConexion(origen, destino, tiempo);
    }

    private List<Integer> ids(List<Usuario> usuarios) {
        List<Integer> resultado = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            resultado.add(usuario.getId());
        }

        return resultado;
    }
}
