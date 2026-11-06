package com.ajs.tpo.algorithm;


import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import com.ajs.tpo.model.Equipo;
import com.ajs.tpo.model.RestriccionesEquipo;
import com.ajs.tpo.model.Usuario;

public class BacktrackingSubconjuntosEquipo {

  public static final int MAX_PASOS = 200;
  

  public static void Backtracking(int[] usuarios, int E, int n, Usuario[] todos, int k, Set<String> habilidadesReq, Set<Usuario> obligatorios, Usuario[] incompatibles, 
    List<Equipo> equipos, List<String> pasos) {

        usuarios[E] = 0; 

        while (usuarios[E] <= n - 1) {
            if (EquipoValido(usuarios, E, n, todos, k, habilidadesReq, obligatorios, incompatibles)) {
                if (E == k - 1) {
                    List<Usuario> integrantes = new ArrayList<>();
                    for (int t = 0; t < k; t++) integrantes.add(todos[usuarios[t]]);
                        Equipo eq = new Equipo(integrantes);
                        equipos.add(eq);
                        registrar(pasos, "Equipo valido: " + eq);
                } else if (E < k - 1) {
                    registrar(pasos, "Se agrega " + todos[usuarios[E]].getNombre() + " como integrante " + (E + 1));
                    Backtracking(usuarios, E + 1, n, todos, k, habilidadesReq, obligatorios,
                        incompatibles, equipos, pasos);
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
                if (usuariosActuales.contains(incompatibles[i - 1]) && usuariosActuales.contains(incompatibles[i])) {
                    return false; 
                }
            }
        }

        if (E == k - 1) {
            // CHEQUEO OBLIGATORIOS
            if (obligatorios != null && !obligatorios.isEmpty() & !usuariosActuales.containsAll(obligatorios)) {
                return false;
            }

            // CHEQUEO HABILIDADES
            if (habilidadesReq != null && !habilidadesReq.isEmpty()) {
                Set<String> cubiertas = new HashSet<>();
                for (Usuario u : usuariosActuales) cubiertas.addAll(u.getHabilidades());
                if (!cubiertas.containsAll(habilidadesReq)) return false;
        
            }
        } 
        return true;
  }

  private static void registrar(List<String> pasos, String paso) {
    if (pasos.size() < MAX_PASOS) pasos.add(paso);
  }


  ///// Para la ejecucion del algoritmo desde service
  
  public static class Resultado {
    private final List<Equipo> equipos;
    private final List<String> pasos;

    Resultado(List<Equipo> equipos, List<String> pasos) {
        this.equipos = equipos;
        this.pasos = pasos;
    }

    public List<Equipo> getEquipos() { return Collections.unmodifiableList(equipos); }
    public boolean hayEquipos() { return !equipos.isEmpty(); }
    public List<String> getPasos() { return Collections.unmodifiableList(pasos); }
  }
  
  public static Resultado ejecutar(List<Usuario> candidatos, RestriccionesEquipo r) {
    if (candidatos == null || r == null)
        throw new IllegalArgumentException("Los candidatos y las restricciones son obligatorios");
    for (Usuario o : r.getObligatorios()) {
        if (!candidatos.contains(o))
            throw new IllegalArgumentException("El obligatorio " + o.getNombre() + " no esta entre los candidatos");
    }

    Usuario[] todos = candidatos.toArray(new Usuario[0]);
    List<Equipo> equipos = new ArrayList<>();
    List<String> pasos = new ArrayList<>();

    Backtracking(new int[r.getTamanio()], 0, todos.length, todos, r.getTamanio(),
            r.getHabilidadesRequeridas(), r.getObligatorios(), r.getIncompatiblesComoPares(), equipos, pasos);

    return new Resultado(equipos, pasos);
  }


}





