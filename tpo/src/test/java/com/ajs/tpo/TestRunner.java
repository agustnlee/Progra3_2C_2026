package com.ajs.tpo;



public class TestRunner {
     public static void main(String[] args) {
        System.out.println("Testing");

      // Mergesort tests
        TestsMergeSort TestMergeSort= new TestsMergeSort();

        TestMergeSort.TestMergeSortPorFecha();
        TestMergeSort.TestMergeSortPorRelevancia();
      // Backtracking equipos Test

        TestBacktrackingEquipos testBack= new TestBacktrackingEquipos();

        testBack.TestEquiposConRestriccionDehabilidadesIntegranteObligatorioEIncompatibles();



      // Dijkstra tests
        //new DijkstraDifusionTest().ejecutar();

      // Floyd Tests
        //new FloydDifusionTest().ejecutar();

      // Mochila Dinamica
        //new MochilaDinamicaDifusionTest().ejecutar();
    
    }

}
