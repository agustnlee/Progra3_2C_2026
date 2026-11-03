package com.ajs.tpo.algorithm;


import java.util.HashSet;
import java.util.Set;


import com.ajs.tpo.model.Usuario;

public class BacktrackingSubconjuntosEquipo {


public static void Backtracking(int[] usuarios, int E, int n, Usuario[] todos, int k, Set<String> habilidadesReq, Set<Usuario> obligatorios, Usuario[] incompatibles) {
// usuarios es la lista vacia que gurada los integrantes solucion,
//  E son las etapas,
//  n es el largo de la lista de todos los integrantes que se van a comparar, 
// todos son todos los integrantes que se evaluan para formar un equipo,
//k es el tamaño del equipo que se quiere armar,
//habilidadesReq es un conjunto con las habilidades que tienen que tener los usuarios para estar en un equipo,
//Obligatorios es un conjunto de usuarios que deben ser parte del equipo
// e incompatibles es una lista de usuarios que tiene los usuarios que no pueden estar en un mismo equipo de a pares. si un integrante no puede estar con varios otros se repite en la lista



        boolean solucion = false;
        usuarios[E] = 0; 

        while (usuarios[E] <= n - 1 & !solucion) {
            if (EquipoValido(usuarios, E, n, todos, k, habilidadesReq, obligatorios, incompatibles)) {
                if (E == k - 1) {
                    for (int t = 0; t < usuarios.length; t++) {
                        Usuario u = todos[usuarios[t]];
                        System.out.println(" id:" + u.getId() + " nombre: " + u.getNombre());

                    }
                    System.out.println(" ");
                   
                } else if (E < k - 1) {
                    Backtracking(usuarios, E + 1, n, todos, k, habilidadesReq, obligatorios, incompatibles);
                }
            } 
            usuarios[E] = usuarios[E] + 1;
          
        }
    }

    public static boolean EquipoValido(int[] usuarios, int E, int n, Usuario[] todos, int k, Set<String> habilidadesReq, Set<Usuario> obligatorios, Usuario[] incompatibles) {
        // CHEQUEO REPETIDOS
        for (int i = 0; i <= E - 1; i++) {
            if (usuarios[i] >= usuarios[E]) {
                return false;
            }
        }

        Set<Usuario> usuariosActuales = new HashSet<>();
        for (int i = 0; i <= E; i++) {
            usuariosActuales.add(todos[usuarios[i]]);
        }

        //CHEQUEO INCOMPATIBLES 
        if (incompatibles != null) { 
            for (int i = 1; i <= incompatibles.length - 1; i = i + 2) {
                if (usuariosActuales.contains(incompatibles[i - 1]) & usuariosActuales.contains(incompatibles[i])) {
                    return false; 
                }
            }
        }

        if (E == k - 1) {
            // CHEQUEO OBLIGATORIOS
            if (!obligatorios.isEmpty() & !usuariosActuales.containsAll(obligatorios)) {
                return false;
            }

            // CHEQUEO HABILIDADES
            if (!habilidadesReq.isEmpty()) {
                for (Usuario u : usuariosActuales) {
                   if (!u.getHabilidades().containsAll(habilidadesReq)) {
                    return false;
                }
                
                }
            }
        } 
        return true;
    }
}





