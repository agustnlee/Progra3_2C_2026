package com.ajs.tpo;


public class TestRunner {
     public static void main(String[] args) {
        System.out.println("Testing");

      // Mergesort tests
        MergeSortPublicacionesTest.ejecutar();
      // Backtracking equipos Test

        BacktrackingSubconjuntosEquipoTest.ejecutar();

        // Backtracking: cadenas alternativas de difusion (B3)
         BacktrackingCadenasDifusionTest.ejecutar();

      // Dijkstra tests
      DijkstraDifusionTest DT = new DijkstraDifusionTest();
      DT.ejecutar();

      // Floyd Tests
      FloydDifusionTest FT = new FloydDifusionTest();
      FT.ejecutar();

      // Mochila Dinamica
      MochilaDinamicaDifusionTest MD = new MochilaDinamicaDifusionTest();
      MD.ejecutar();
    
    }

}
