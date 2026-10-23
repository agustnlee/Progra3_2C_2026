
package com.ajs.tpo;

import com.ajs.tpo.algorithm.FloydDifusion;
import com.ajs.tpo.model.GrafoUsuarios;
import com.ajs.tpo.model.Usuario;
import com.ajs.tpo.repository.UsuarioRepository;
import com.ajs.tpo.service.FloydService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Pruebas de FloydDifusion algoritmo y su servicio


public class FloydDifusionTest {

    private int ejecutadas;
    private int aprobadas;
    private int fallidas;

    public void ejecutar() {
        ejecutadas = 0;
        aprobadas = 0;
        fallidas = 0;

        System.out.println();
        System.out.println("========================================");
        System.out.println(" TESTS: FLOYD DIFUSION");
        System.out.println("========================================");

        ejecutarPrueba("Camino minimo indirecto", this::testCaminoMinimo);
        ejecutarPrueba("Todos los pares alcanzables", this::testTodosLosPares);
        ejecutarPrueba("Grafo dirigido", this::testDireccionalidad);
        ejecutarPrueba("Conexion deshabilitada", this::testConexionDeshabilitada);
        ejecutarPrueba("Destino inalcanzable", this::testInalcanzable);
        ejecutarPrueba("Origen igual a destino", this::testMismoUsuario);
        ejecutarPrueba("Empate entre caminos minimos", this::testEmpate);
        ejecutarPrueba("Servicio por ID", this::testServicio);
        ejecutarPrueba("Invalidacion de resultados al cambiar el grafo",
                this::testInvalidacionCache);
        ejecutarPrueba("Grafo nulo", this::testGrafoNulo);
        ejecutarPrueba("Usuario ajeno al grafo", this::testUsuarioAjeno);

        System.out.println();
        System.out.println("========================================");
        System.out.println(" RESUMEN");
        System.out.println("========================================");
        System.out.println("Ejecutadas: " + ejecutadas);
        System.out.println("Aprobadas:  " + aprobadas);
        System.out.println("Fallidas:   " + fallidas);
        System.out.println("Resultado:  "
                + (fallidas == 0 ? "TODAS APROBADAS" : "HAY FALLOS"));
        System.out.println("========================================");
    }

    private void testCaminoMinimo() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2, 3, 4);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);
        Usuario c = usuario(grafo, 3);
        Usuario d = usuario(grafo, 4);

        grafo.agregarConexion(a, b, 8);
        grafo.agregarConexion(a, c, 2);
        grafo.agregarConexion(c, b, 1);
        grafo.agregarConexion(b, d, 2);
        grafo.agregarConexion(c, d, 9);

        FloydDifusion.Resultado resultado =
                FloydDifusion.ejecutar(grafo);

        comprobar(resultado.getTiempo(a, d) == 5,
                "El tiempo minimo de A a D debe ser 5.");

        comprobar(ids(resultado.reconstruirCamino(a, d))
                        .equals(Arrays.asList(1, 3, 2, 4)),
                "El camino esperado es [1, 3, 2, 4].");
    }

    private void testTodosLosPares() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2, 3);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);
        Usuario c = usuario(grafo, 3);

        grafo.agregarConexion(a, b, 4);
        grafo.agregarConexion(b, c, 6);
        grafo.agregarConexion(a, c, 20);

        FloydDifusion.Resultado resultado =
                FloydDifusion.ejecutar(grafo);

        comprobar(resultado.getTiempo(a, b) == 4,
                "A -> B debe costar 4.");

        comprobar(resultado.getTiempo(b, c) == 6,
                "B -> C debe costar 6.");

        comprobar(resultado.getTiempo(a, c) == 10,
                "A -> C debe mejorar de 20 a 10.");

        comprobar(!resultado.esAlcanzable(c, a),
                "C no debe alcanzar A.");
    }

    private void testDireccionalidad() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);

        grafo.agregarConexion(a, b, 3);

        FloydDifusion.Resultado resultado =
                FloydDifusion.ejecutar(grafo);

        comprobar(resultado.esAlcanzable(a, b),
                "A debe alcanzar B.");

        comprobar(!resultado.esAlcanzable(b, a),
                "B no debe alcanzar A sin una conexion de regreso.");
    }

    private void testConexionDeshabilitada() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2, 3);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);
        Usuario c = usuario(grafo, 3);

        grafo.agregarConexion(a, b, 1);
        grafo.agregarConexion(b, c, 2);
        grafo.agregarConexion(a, c, 10);

        comprobar(grafo.setHabilitada(a, b, false),
                "Se deberia poder deshabilitar A -> B.");

        FloydDifusion.Resultado resultado =
                FloydDifusion.ejecutar(grafo);

        comprobar(resultado.getTiempo(a, c) == 10,
                "Debe ignorar A -> B y usar la conexion directa de costo 10.");
    }

    private void testInalcanzable() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2, 3);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);
        Usuario c = usuario(grafo, 3);

        grafo.agregarConexion(a, b, 2);

        FloydDifusion.Resultado resultado =
                FloydDifusion.ejecutar(grafo);

        comprobar(!resultado.esAlcanzable(a, c),
                "C debe ser inalcanzable desde A.");

        comprobar(resultado.getTiempo(a, c)
                        == FloydDifusion.INFINITO,
                "El tiempo debe ser INFINITO.");

        comprobar(resultado.reconstruirCamino(a, c).isEmpty(),
                "No debe existir un camino reconstruible.");
    }

    private void testMismoUsuario() {
        GrafoUsuarios grafo = nuevoGrafo(1);
        Usuario a = usuario(grafo, 1);

        FloydDifusion.Resultado resultado =
                FloydDifusion.ejecutar(grafo);

        comprobar(resultado.esAlcanzable(a, a),
                "Un usuario debe ser alcanzable desde si mismo.");

        comprobar(resultado.getTiempo(a, a) == 0,
                "El tiempo de A a A debe ser 0.");

        comprobar(ids(resultado.reconstruirCamino(a, a))
                        .equals(List.of(1)),
                "El camino debe contener solamente A.");
    }

    private void testEmpate() {
        GrafoUsuarios grafo = nuevoGrafo(1, 2, 3, 4);

        Usuario a = usuario(grafo, 1);
        Usuario b = usuario(grafo, 2);
        Usuario c = usuario(grafo, 3);
        Usuario d = usuario(grafo, 4);

        grafo.agregarConexion(a, b, 2);
        grafo.agregarConexion(b, d, 3);
        grafo.agregarConexion(a, c, 1);
        grafo.agregarConexion(c, d, 4);

        FloydDifusion.Resultado resultado =
                FloydDifusion.ejecutar(grafo);

        comprobar(resultado.getTiempo(a, d) == 5,
                "El costo minimo debe ser 5.");

        List<Integer> camino = ids(resultado.reconstruirCamino(a, d));

        comprobar(
                camino.equals(Arrays.asList(1, 2, 4))
                        || camino.equals(Arrays.asList(1, 3, 4)),
                "Debe devolver uno de los caminos validos de costo 5."
        );
    }

    private void testServicio() {
        Fixture fixture = nuevoFixture(1, 2, 3);

        Usuario a = usuario(fixture.grafo, 1);
        Usuario b = usuario(fixture.grafo, 2);
        Usuario c = usuario(fixture.grafo, 3);

        fixture.grafo.agregarConexion(a, b, 3);
        fixture.grafo.agregarConexion(b, c, 4);

        FloydService servicio = new FloydService(
                fixture.grafo,
                fixture.repositorio
        );

        servicio.precalcular();

        comprobar(servicio.consultarTiempoMinimo(1, 3) == 7,
                "El servicio debe devolver el tiempo minimo 7.");

        comprobar(ids(servicio.consultarCaminoMinimo(1, 3))
                        .equals(Arrays.asList(1, 2, 3)),
                "El servicio debe reconstruir el camino correcto.");

        comprobar(servicio.esAlcanzable(1, 3),
                "El servicio debe informar que C es alcanzable.");
    }

 private void testInvalidacionCache() {
        Fixture fixture = nuevoFixture(1, 2, 3);

        Usuario a = usuario(fixture.grafo, 1);
        Usuario b = usuario(fixture.grafo, 2);
        Usuario c = usuario(fixture.grafo, 3);

        // Grafo inicial: A -> B -> C, con costo total 16.
        fixture.grafo.agregarConexion(a, b, 8);
        fixture.grafo.agregarConexion(b, c, 8);

        FloydService servicio = new FloydService(
            fixture.grafo,
            fixture.repositorio
        );

        comprobar(servicio.consultarTiempoMinimo(1, 3) == 16,
            "El costo inicial debe ser 16.");

        // Se agrega una nueva conexión que mejora el camino: A -> C.
        fixture.grafo.agregarConexion(a, c, 3);

        comprobar(servicio.consultarTiempoMinimo(1, 3) == 3,
            "El servicio debe recalcular tras cambiar el grafo.");
  }

    private void testGrafoNulo() {
        esperarIllegalArgument(
                () -> FloydDifusion.ejecutar(null),
                "El algoritmo debe rechazar un grafo nulo."
        );
    }

    private void testUsuarioAjeno() {
        GrafoUsuarios grafo = nuevoGrafo(1);
        Usuario a = usuario(grafo, 1);
        Usuario ajeno = new Usuario(99, "Ajeno");

        FloydDifusion.Resultado resultado =
                FloydDifusion.ejecutar(grafo);

        esperarIllegalArgument(
                () -> resultado.getTiempo(a, ajeno),
                "Debe rechazar usuarios que no pertenecen al grafo."
        );
    }


    // pruebas privadas infraestructura
  

    private void ejecutarPrueba(String nombre, Runnable prueba) {
        ejecutadas++;

        try {
            prueba.run();
            aprobadas++;
            System.out.println("[OK]    " + nombre);
        } catch (Throwable error) {
            fallidas++;
            System.out.println("[FALLO] " + nombre);
            System.out.println("        Motivo: " + error.getMessage());
        }
    }

    private void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    private void esperarIllegalArgument(
            Runnable operacion,
            String mensaje
    ) {
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

    private Fixture nuevoFixture(int... ids) {
        GrafoUsuarios grafo = new GrafoUsuarios();
        UsuarioRepository repositorio = new UsuarioRepository();

        for (int id : ids) {
            Usuario usuario = new Usuario(id, "Usuario " + id);
            grafo.agregarUsuario(usuario);
            repositorio.agregar(usuario);
        }

        return new Fixture(grafo, repositorio);
    }

    private Usuario usuario(GrafoUsuarios grafo, int id) {
        for (Usuario usuario : grafo.getUsuarios()) {
            if (usuario.getId() == id) {
                return usuario;
            }
        }

        throw new AssertionError("No existe el usuario " + id);
    }

    private List<Integer> ids(List<Usuario> usuarios) {
        List<Integer> resultado = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            resultado.add(usuario.getId());
        }

        return resultado;
    }

    private static final class Fixture {
        private final GrafoUsuarios grafo;
        private final UsuarioRepository repositorio;

        private Fixture(
                GrafoUsuarios grafo,
                UsuarioRepository repositorio
        ) {
            this.grafo = grafo;
            this.repositorio = repositorio;
        }
    }
}
