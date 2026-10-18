package com.ajs.tpo;


public class TestRunner {
     public static void main(String[] args) {
        System.out.println("Testing");

        TestsMergeSort TestMergeSort= new TestsMergeSort();

        TestMergeSort.TestMergeSortPorFecha();
        TestMergeSort.TestMergeSortPorRelevancia();
        TestMergeSort.TestMergeSortConEnumErroneo();

        new DijkstraDifusionTest().ejecutar();
    
    }

}
