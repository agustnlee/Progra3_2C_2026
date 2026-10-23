package com.ajs.tpo.algorithm;

import com.ajs.tpo.model.Publicacion;
import com.ajs.tpo.enums.*;

public class MergeSortPublicaciones {

// compara 2 publicaciones por relevancia descendente y si son iguales se desempata por id ascendente
// negativo si p1 tiene que ir ANTES de p2, positivo si p1 DESPUES de p2, 0 si son iguales en relevancia
    private static  int CompararRelevancia(Publicacion p1, Publicacion p2) {
       
        if (p1.getRelevancia()!=p2.getRelevancia()) {
            return Double.compare(p2.getRelevancia(), p1.getRelevancia()); // para orden descendente
        } else {
            return CompararFecha(p1, p2); // para orden ascendente  
        }
    }

    private static  int CompararId(Publicacion p1, Publicacion p2){
       
        return Integer.compare(p1.getId(), p2.getId());
    }

    private static int CompararFecha(Publicacion p1, Publicacion p2){
       
        if (!p1.getFechaHora().equals(p2.getFechaHora())){
           if (p1.getFechaHora().isAfter(p2.getFechaHora())){
            return -1;
           }
           else return 1;
        }
        else return CompararId(p1, p2);
    }



    public static void mergeSort(Publicacion[] arr, int izq, int der, CriterioOrdenamientoPublicaciones tipo) {
        if (izq <=der) {
        int medio = (izq + der) / 2;

        mergeSort(arr , izq, medio, tipo);
        mergeSort(arr , medio + 1, der, tipo);
        mezclar(arr, izq, medio, der, tipo); }

    }

     private static  void mezclar(Publicacion[] arr, int izq, int medio, int der, CriterioOrdenamientoPublicaciones tipo) {

        Publicacion[] auxiliar = new Publicacion[der-izq +1]; 
        

        int i = izq;        // puntero parte izquierda
        int j = medio + 1;  // puntero parte derecha
        for (int k=0;k<=der-izq;k++){
            
            if (tipo.equals(CriterioOrdenamientoPublicaciones.POR_RELEVANCIA)){
            if(i>der || CompararRelevancia(arr[i], arr[j])<0 & i<=medio ){

                auxiliar[k]= arr[i];
                i=i+i;
            }
            else{
                auxiliar[k]= arr[j];
                j=j+1;
            }

            }

            else if(tipo.equals(CriterioOrdenamientoPublicaciones.POR_FECHA)){
                 if(i>der || CompararFecha(arr[i], arr[j])<0 & i<=medio ){

                auxiliar[k]= arr[i];
                i=i+i;
            }
            else{
                auxiliar[k]= arr[j];
                j=j+1;
            }

            }



            }
        }

            
            
             

}