package com.ajs.tpo;

import java.util.HashSet;
import java.util.Set;

import com.ajs.tpo.algorithm.BacktrackingSubconjuntosEquipo;

import com.ajs.tpo.model.Usuario;

public class TestBacktrackingEquipos {

    public void TestEquipos(){
        

        
        Usuario u1 = new Usuario(1, "sofia");
        u1.agregarHabilidad("java");
        u1.agregarHabilidad("sql");

        Usuario u2 = new Usuario(2, "juan");
        u2.agregarHabilidad("react");
        u2.agregarHabilidad("java");

        Usuario u3 = new Usuario(3, "tomas");
        u3.agregarHabilidad("java");
        u3.agregarHabilidad("spring");

        Usuario u4 = new Usuario(4, "Fran");
        u4.agregarHabilidad("sql");
        u4.agregarHabilidad("react");

        
        Usuario[] todos = new Usuario[] { u1, u2, u3, u4 };

       
        int k = 3;
        int[] usuarios = new int[k]; 

        Set<String> habilidadesReq = new HashSet<>();
        habilidadesReq.add("Java");

        
        Set<Usuario> obligatorios = new HashSet<>();
         obligatorios.add(u1);

       
        Usuario[] incompatibles = new Usuario[] { };

        
        BacktrackingSubconjuntosEquipo.Backtracking(
            usuarios, 
            0,               // Etapa inicial E = 0
            todos.length,    // n
            todos,           // Arreglo completo de usuarios
            k,               // Tamaño del equipo
            habilidadesReq,  // Habilidades requeridas
            obligatorios,    // Usuarios obligatorios
            incompatibles    // Arreglo de incompatibles
        );





    }

}
