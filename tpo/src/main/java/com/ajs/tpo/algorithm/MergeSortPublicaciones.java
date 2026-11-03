package com.ajs.tpo.algorithm;

import com.ajs.tpo.model.Publicacion;
import com.ajs.tpo.enums.*;

public class MergeSortPublicaciones {

    public static void mergeSort(Publicacion[] arr, CriterioOrdenamientoPublicaciones tipo) { // Para su utilizacion en service
        if (arr == null || arr.length < 2) return;
        mergeSort(arr, 0, arr.length - 1, tipo);
    }


    public static void mergeSort(Publicacion[] arr, int ini, int fin, CriterioOrdenamientoPublicaciones tipo) {
            
       
        if (ini <fin) {
        int medio = (ini + fin) / 2;

        mergeSort(arr , ini, medio, tipo);
        mergeSort(arr , medio + 1, fin, tipo);
        mezclar(arr, ini, fin, tipo); }

    } 

     private static  void mezclar(Publicacion[] arr, int ini,  int fin, CriterioOrdenamientoPublicaciones tipo) {
       Publicacion[] aux= new Publicacion[fin-ini+1];
       int mid=(ini+fin)/2;
      int i=ini;
      int  j=mid+1;
       for(int k=0;k<=fin-ini;k++){
        
        if (tipo== CriterioOrdenamientoPublicaciones.POR_RELEVANCIA){


         if((j>fin)||(arr[i].getRelevancia()>arr[j].getRelevancia()&&i <mid +1)) {
            aux[k] = arr[i];
            i++;} 

            else if(arr[i].getRelevancia()==arr[j].getRelevancia()){
                if (arr[i].getId()<arr[j].getId()) {
                     aux[k] = arr[i]; 
                     i++;
                     } 
                else{ aux[k] = arr[j];
                j++;

                }}
            
            
            else {
                aux[k] = arr[j];
                j++;
            }
       }else if(tipo==CriterioOrdenamientoPublicaciones.POR_FECHA){


         if((j>fin)||(arr[i].getFechaHora().isAfter(arr[j].getFechaHora())&&i <mid +1)) {
            aux[k] = arr[i];
            i++;} 

            else if(arr[i].getFechaHora().equals(arr[j].getFechaHora())){
                if (arr[i].getId()<arr[j].getId()) {
                     aux[k] = arr[i]; 
                     i++;
                     } 
                else{ aux[k] = arr[j];
                j++;

                }}
            
            
            else {
                aux[k] = arr[j];
                j++;
            }
       }
       else {
        break;
       }
    
    }
        for(int k=0;k <=fin-ini;k++){
            arr[ini + k] = aux[k];
        }
    }

       







     
}
