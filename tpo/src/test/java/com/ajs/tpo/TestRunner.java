package com.ajs.tpo;


public class TestRunner {
     public static void main(String[] args) {
        System.out.println("Testing");

      // Mergesort tests
        TestsMergeSort TestMergeSort= new TestsMergeSort();

        TestMergeSort.TestMergeSortPorFecha();
        TestMergeSort.TestMergeSortPorRelevancia();
      // Backtracking equipos Test

        BacktrackingSubconjuntosEquipoTest.ejecutar();




      // Dijkstra tests
        // DijkstraDifusionTest.ejecutar();

      // Floyd Tests
        //FloydDifusionTest.ejecutar();

      // Mochila Dinamica
        // MochilaDinamicaDifusionTest.ejecutar();
    
    }

}
