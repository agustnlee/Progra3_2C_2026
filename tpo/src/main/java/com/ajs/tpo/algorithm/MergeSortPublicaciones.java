package com.ajs.tpo.algorithm;

import com.ajs.tpo.model.Publicacion;

public class MergeSortPublicaciones {

    public static long comparaciones = 0;
    public static long movimientos = 0;

    public static void reiniciarContadores() {
        comparaciones = 0;
        movimientos = 0;

    }
// compara 2 productos por relevancia descendente y si son iguales se desempata por id ascendente
// negativo si p1 tiene que ir ANTES de p2, positivo si p1 DESPUES de p2, 0 si son iguales en relevancia
    private static int CompararRelevancia(Publicacion p1, Publicacion p2) {
        comparaciones++;



        if (p1.getFechaHora() != p2.getFechaHora()) {
            return Double.compare(p2.getRelevancia(), p1.getRelevancia()); // para orden descendente
        } else {
            return Double.compare(p1.getId(), p2.getId()); // para orden ascendente
        
        }
    }

    public static void mergeSort(Publicacion[] arr, int tipo) {
        if (arr.length < 2) {
            return;
        }
        Publicacion[] auxiliar = new Publicacion[arr.length];
        mergeSort(arr, auxiliar, 0, arr.length - 1, tipo);
    }

    private static void mergeSort(Publicacion[] arr, Publicacion[] auxiliar, int izq, int der, int tipo) {
        if (izq >= der) {
            return;
        }

        int medio = (izq + der) / 2;

        mergeSort(arr, auxiliar, izq, medio, tipo);
        mergeSort(arr, auxiliar, medio + 1, der, tipo);
        mezclar(arr, auxiliar, izq, medio, der, tipo);
    }

     private static void mezclar(Publicacion[] arr, Publicacion[] auxiliar, int izq, int medio, int der, int tipo) {

        // Copiar rango [izq, der] al arreglo auxiliar
        for (int k = izq; k <= der; k++) {
            auxiliar[k] = arr[k];
            movimientos++;
        }

        int i = izq;        // puntero parte izquierda
        int j = medio + 1;  // puntero parte derecha
        int k = izq;        // puntero de escritura en arr

        while (i <= medio && j <= der) {

            if (tipo ==0){
                
                if (CompararRelevancia(auxiliar[i], auxiliar[j]) <= 0) {
                arr[k] = auxiliar[i];
                movimientos++;
                i++;
            } else {
                arr[k] = auxiliar[j];
                movimientos++;
                j++;
            }
            k++;
        }

        else{
            if(auxiliar[i].getFechaHora().isAfter(auxiliar[j].getFechaHora())){
                 arr[k] = auxiliar[i];
                movimientos++;
                i++;
            } else {
                arr[k] = auxiliar[j];
                movimientos++;
                j++;
            }
            k++;

            }
        }
    
    
           

        // copiar sobrante izq
        while (i <= medio) {
            arr[k] = auxiliar[i];
            movimientos++;
            i++;
            k++;
        }

        // copair sobrante der
        while (j <= der) {
            arr[k] = auxiliar[j];
            movimientos++;
            j++;
            k++;
        }
    }


    
}
