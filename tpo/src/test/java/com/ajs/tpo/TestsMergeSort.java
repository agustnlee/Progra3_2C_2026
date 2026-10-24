package com.ajs.tpo;

import java.time.LocalDateTime;

import com.ajs.tpo.algorithm.MergeSortPublicaciones;
import com.ajs.tpo.model.Publicacion;
import com.ajs.tpo.model.Usuario;
import  com.ajs.tpo.enums.CriterioOrdenamientoPublicaciones;

public class TestsMergeSort {
    

     public void TestMergeSortPorRelevancia(){

        MergeSortPublicaciones mergeTest= new MergeSortPublicaciones();
       


        Usuario MockUsuario = new Usuario(1, "yo");


        Publicacion Mockp1 = new Publicacion(1, MockUsuario, LocalDateTime.now().minusDays(3), 10, 2);
        Publicacion Mockp2 = new Publicacion(2, MockUsuario, LocalDateTime.now().minusDays(2), 25, 5);
        Publicacion Mockp3 = new Publicacion(3, MockUsuario, LocalDateTime.now().minusDays(1), 5, 1);
        Publicacion Mockp4 = new Publicacion(4, MockUsuario, LocalDateTime.now().minusDays(1), 5, 1);

        Publicacion[] Mockpublicaciones = { Mockp3, Mockp1, Mockp2, Mockp4 };

       
        mergeTest.mergeSort(Mockpublicaciones,0,(Mockpublicaciones.length)-1 , CriterioOrdenamientoPublicaciones.POR_RELEVANCIA);

         System.out.println("orden por relevancia");
        for (Publicacion p : Mockpublicaciones) {
            System.out.println(p);
        }

    }

    public void TestMergeSortPorFecha(){

        MergeSortPublicaciones mergeTest= new MergeSortPublicaciones();
        Usuario MockUsuario = new Usuario(1, "yo");

        Publicacion Mockp1 = new Publicacion(1, MockUsuario, LocalDateTime.now().minusDays(3), 10, 2);
        Publicacion Mockp2 = new Publicacion(2, MockUsuario, LocalDateTime.now().minusDays(2), 25, 5);
        Publicacion Mockp3 = new Publicacion(3, MockUsuario, LocalDateTime.now().minusDays(1), 5, 1);
        Publicacion Mockp4 = new Publicacion(4, MockUsuario, LocalDateTime.now().minusDays(1), 5, 1);

        Publicacion[] Mockpublicaciones = { Mockp3, Mockp1, Mockp2, Mockp4 };

      int fin= Mockpublicaciones.length;

      

      mergeTest.mergeSort(Mockpublicaciones,0,(Mockpublicaciones.length )-1, CriterioOrdenamientoPublicaciones.POR_FECHA);

           System.out.println("orden por fecha");
        for (Publicacion p : Mockpublicaciones) {
            System.out.println(p);
        } 


         

    }

    public void TestMergeSortConEnumErroneo (){

         MergeSortPublicaciones mergeTest= new MergeSortPublicaciones();
        Usuario MockUsuario = new Usuario(1, "yo");

        Publicacion Mockp1 = new Publicacion(1, MockUsuario, LocalDateTime.now().minusDays(3), 10, 2);
        Publicacion Mockp2 = new Publicacion(2, MockUsuario, LocalDateTime.now().minusDays(2), 25, 5);
        Publicacion Mockp3 = new Publicacion(3, MockUsuario, LocalDateTime.now().minusDays(1), 5, 1);
        Publicacion Mockp4 = new Publicacion(4, MockUsuario, LocalDateTime.now().minusDays(1), 5, 1);

        Publicacion[] Mockpublicaciones = { Mockp3, Mockp1, Mockp2, Mockp4 };

        
        mergeTest.mergeSort(Mockpublicaciones,0,Mockpublicaciones.length -1, CriterioOrdenamientoPublicaciones.PRUEBA);

           System.out.println("orden por enum no valido");
        for (Publicacion p : Mockpublicaciones) {
            System.out.println(p);



    }

    }}





