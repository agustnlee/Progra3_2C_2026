package com.ajs.tpo.algorithm;


import java.util.HashSet;
import java.util.Set;


import com.ajs.tpo.model.Usuario;

public class BacktrackingSubconjuntosEquipo {


    public static void Backtracking (int[] usuarios , int E, int n, Usuario[] todos, int k,  Set<String> habilidadesReq, Set<Usuario> obligatorios, Usuario[] incompatibles) {
        boolean solucion = false;
       
       if (E == 0) {
            usuarios[E] = 0;
        } else {
            usuarios[E] = usuarios[E - 1] + 1;
        }
        

        while ( usuarios[E]<=  n-1 & !solucion ){
           

            if(EquipoValido( usuarios,E,n,todos,k, habilidadesReq,obligatorios,incompatibles)){

                
                if(E == k - 1){
                    solucion= true;
                    
                    for (int t=0; t< usuarios.length;t++) {
                        Usuario u = todos[usuarios[t]];

                        System.out.println("answer:" + u.getId() + " " +u.getNombre());
                    }
                    System.out.println("solved");

                }
                else if(E< k-1){
                     Backtracking(usuarios,E +1,n,todos,k, habilidadesReq,obligatorios,incompatibles);
                }
              
               
            }  usuarios[E] = usuarios[E] + 1;

        }
    }

    public static boolean EquipoValido (int[] usuarios , int E, int n, Usuario[] todos, int k,  Set<String> habilidadesReq, Set<Usuario> obligatorios, Usuario[] incompatibles){
        for (int i = 0; i <= E - 1; i++) {
             System.out.println(usuarios[i] + " y " + usuarios[E]);
            if (usuarios[i] >= usuarios[E]) {
                return false;
            }
        };
        Set<Usuario> usuariosActuales = new HashSet<>();
        for (int i = 0; i <= E; i++) {
            usuariosActuales.add(todos[usuarios[i]]);
        };

        if (!incompatibles.equals(null)){ 
            for (int i = 1; i <= incompatibles.length - 1; i= i+2) {

        if ( usuariosActuales.contains(incompatibles[i-1])& usuariosActuales.contains(incompatibles[i])) {

            return false; };
         };
        };
        if (E == k - 1) {
            if (!obligatorios.isEmpty()  && !usuariosActuales.containsAll(obligatorios)) {
                return false;
            };

        if ( !habilidadesReq.isEmpty()) {
                Set<String> habilidadesTotalesEquipo = new HashSet<>();
                for (Usuario u : usuariosActuales) {
                    habilidadesTotalesEquipo.addAll(u.getHabilidades());
                };
                if (!habilidadesTotalesEquipo.containsAll(habilidadesReq)) {
                    return false;
                };
    };
   
} return true;
}

}



