package com.ajs.tpo;

import java.util.HashSet;
import java.util.Set;

import com.ajs.tpo.algorithm.BacktrackingSubconjuntosEquipo;

import com.ajs.tpo.model.Usuario;

public class TestBacktrackingEquipos {

    public void TestEquiposConRestriccionDehabilidadesIntegranteObligatorioEIncompatibles(){
        

        
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
        u4.agregarHabilidad("java");

        Usuario u5 = new Usuario(5, "Giuliana");
        
        u5.agregarHabilidad("python");
        u5.agregarHabilidad("java");
        
        Usuario[] todos = new Usuario[] { u1, u2, u3, u4 ,u5};

       
        int tamanioEquipo = 3;
        int[] usuarios = new int[tamanioEquipo]; 

        Set<String> habilidadesReq = new HashSet<>();
        habilidadesReq.add("java");

        
        Set<Usuario> obligatorios = new HashSet<>();
         obligatorios.add(u1);

       
        Usuario[] incompatibles = new Usuario[] {u1,u4};

        System.out.println("Los integrantes validos para formar un equipo son:");
        BacktrackingSubconjuntosEquipo.Backtracking( usuarios,  0,todos.length,todos,tamanioEquipo,habilidadesReq,obligatorios,incompatibles    
        );





    }

}
