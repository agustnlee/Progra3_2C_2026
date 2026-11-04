package com.ajs.tpo;

import java.time.LocalDateTime;
import java.util.List;

import com.ajs.tpo.model.GrafoUsuarios;
import com.ajs.tpo.repository.EquipoRepository;
import com.ajs.tpo.repository.IniciativaRepository;
import com.ajs.tpo.repository.PublicacionRepository;
import com.ajs.tpo.repository.UsuarioRepository;
import com.ajs.tpo.service.ConexionService;
import com.ajs.tpo.service.DijkstraService;
import com.ajs.tpo.service.EquipoService;
import com.ajs.tpo.service.FloydService;
import com.ajs.tpo.service.FormacionEquiposService;
import com.ajs.tpo.service.IniciativaService;
import com.ajs.tpo.service.MochilaDinamicaService;
import com.ajs.tpo.service.PublicacionService;
import com.ajs.tpo.service.UsuarioService;
import com.ajs.tpo.ui.*;

// Clase Principal del Sistema Ejecutada por Main.java

public class App {

    public static void iniciar() {
        UsuarioRepository usuarioRepo = new UsuarioRepository();
        PublicacionRepository pubRepo = new PublicacionRepository();
        IniciativaRepository iniRepo = new IniciativaRepository();
        EquipoRepository equipoRepo = new EquipoRepository();
        GrafoUsuarios grafo = new GrafoUsuarios();

        EquipoService equipoService = new EquipoService(equipoRepo, usuarioRepo);
        UsuarioService usuarioService = new UsuarioService(usuarioRepo, pubRepo, grafo, equipoService);
        PublicacionService pubService = new PublicacionService(pubRepo, usuarioRepo);
        ConexionService conexionService = new ConexionService(grafo, usuarioRepo);
        IniciativaService iniciativaService = new IniciativaService(iniRepo);
        MochilaDinamicaService mochilaService = new MochilaDinamicaService(iniRepo);
        DijkstraService dijkstraService = new DijkstraService(grafo, usuarioRepo);
        FloydService floydService = new FloydService(grafo, usuarioRepo);
        FormacionEquiposService formacionService = new FormacionEquiposService(usuarioRepo);

        cargarDatosDePrueba(usuarioService, pubService, conexionService, iniciativaService);

        Consola consola = new Consola();
        List<ModuloCLI> modulos = List.of(
                new UsuariosCLI(consola, usuarioService),
                new PublicacionesCLI(consola, pubService),
                new ConexionesCLI(consola, conexionService, grafo),
                new DifusionCLI(consola, dijkstraService, floydService),
                new IniciativasCLI(consola, iniciativaService, mochilaService),
                new EquiposCLI(consola, formacionService, equipoService)
        );
        new MenuPrincipal(consola, modulos).iniciar();
    }


    
private static void cargarDatosDePrueba(UsuarioService us, PublicacionService ps,
                                        ConexionService cs, IniciativaService is) {

    // Usuarios (ids 1 a 8)
    us.crear("Agustin"); us.crear("Juani"); us.crear("Sofia"); us.crear("Diego");
    us.crear("Eva"); us.crear("Lucas"); us.crear("Marta"); us.crear("Pablo");

    us.registrarHabilidad(1, "java");
    us.registrarHabilidad(2, "sql");
    us.registrarHabilidad(3, "diseño");
    us.registrarHabilidad(4, "java"); us.registrarHabilidad(4, "sql");
    us.registrarHabilidad(5, "diseño"); us.registrarHabilidad(5, "ui");
    us.registrarHabilidad(6, "python");
    us.registrarHabilidad(7, "sql"); us.registrarHabilidad(7, "python");
    us.registrarHabilidad(8, "java"); us.registrarHabilidad(8, "ui");

    // Conexiones  
    cs.crearConexion(1, 2, 5);  cs.crearConexion(1, 3, 2);  cs.crearConexion(3, 2, 1);
    cs.crearConexion(2, 4, 4);  cs.crearConexion(3, 4, 10); cs.crearConexion(4, 5, 3);
    cs.crearConexion(2, 6, 7);  cs.crearConexion(5, 6, 2);  cs.crearConexion(6, 7, 3);
    cs.crearConexion(4, 7, 9);  cs.crearConexion(7, 5, 1);  cs.crearConexion(2, 1, 6);
    cs.crearConexion(6, 4, 2);  cs.crearConexion(1, 5, 15);
    // El usuario 8 queda sin conexiones

    // Publicaciones con reacciones prefijadas 
    LocalDateTime ahora = LocalDateTime.now();
    ps.crear(1, 1, ahora.minusHours(5), 10);
    ps.crear(2, 2, ahora.minusHours(2), 4);
    ps.crear(3, 3, ahora.minusDays(1), 30);
    ps.crear(4, 4, ahora.minusHours(8), 4);   // mismas reacciones que la 2
    ps.crear(5, 6, ahora.minusDays(2), 15);

    ps.agregarComentario(2, 1, "Muy buena propuesta");
    ps.agregarComentario(2, 3, "Cuenten conmigo");
    ps.agregarComentario(4, 2, "Me interesa");
    ps.agregarComentario(4, 5, "Cuando empezamos?");
    ps.agregarComentario(5, 7, "Excelente");

    // Iniciativas 
    is.crear("Biblioteca digital", 40, 70);
    is.crear("Torneo de programacion", 20, 35);
    is.crear("Laboratorio de robotica", 60, 90);
    is.crear("Feria de empleo", 30, 50);
    is.crear("Taller de diseño", 15, 25);
    is.crear("Hackathon anual", 45, 80);
}

}  

