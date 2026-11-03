package com.ajs.tpo.algorithm;

import com.ajs.tpo.model.Publicacion;
import com.ajs.tpo.enums.*;

public class MergeSortPublicaciones {

// compara 2 publicaciones por relevancia descendente y si son iguales se desempata por id ascendente
// negativo si p1 tiene que ir ANTES de p2, positivo si p1 DESPUES de p2, 0 si son iguales en relevancia
  /*   private static  int CompararRelevancia(Publicacion p1, Publicacion p2) {
       
        if (p1.getRelevancia()!=p2.getRelevancia()) {
            System.out.println("primer valor:"+ p1.getId()+ "segundo valor:"+p2.getId());
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
*/


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
