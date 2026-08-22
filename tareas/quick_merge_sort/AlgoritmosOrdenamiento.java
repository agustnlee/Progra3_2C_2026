public class AlgoritmosOrdenamiento {

    public static long comparaciones = 0;
    public static long movimientos = 0;
 
    // para reiniciar antes de cada corrida
    public static void reiniciarContadores() {
        comparaciones = 0;
        movimientos = 0;
    }
 
    // Metodo privado compartido -> comparar 2 productos por calificacion descendente + (empate) precio ascendente
    // Valores de retorno: negativo si p1 tiene que ir ANTES de p2, positivo si p1 DESPUES de p2, 0 si son equivalentes en orden7

    private static int comparar(Producto p1, Producto p2) {
        comparaciones++;
        if (p1.getCalificacion() != p2.getCalificacion()) {
            return Double.compare(p2.getCalificacion(), p1.getCalificacion()); // para orden descendente
        } else {
            return Double.compare(p1.getPrecio(), p2.getPrecio()); // para orden ascendente
        }
    }
 
    // __ MergeSort ____________________________________________________________
 
    public static void mergeSort(Producto[] arr) {
        if (arr.length < 2) {
            return;
        }
        Producto[] auxiliar = new Producto[arr.length];
        mergeSort(arr, auxiliar, 0, arr.length - 1);
    }
 
    private static void mergeSort(Producto[] arr, Producto[] auxiliar, int izq, int der) {
        if (izq >= der) {
            return;
        }
 
        int medio = (izq + der) / 2;
 
        mergeSort(arr, auxiliar, izq, medio);
        mergeSort(arr, auxiliar, medio + 1, der);
        mezclar(arr, auxiliar, izq, medio, der);
    }
 
    private static void mezclar(Producto[] arr, Producto[] auxiliar, int izq, int medio, int der) {

        // Copiar rango [izq, der] al arreglo auxiliar
        for (int k = izq; k <= der; k++) {
            auxiliar[k] = arr[k];
            movimientos++;
        }
 
        int i = izq;        // puntero parte izquierda
        int j = medio + 1;  // puntero parte derecha
        int k = izq;        // puntero de escritura en arr
 
        while (i <= medio && j <= der) {
            if (comparar(auxiliar[i], auxiliar[j]) <= 0) {
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
 
    // __ QuickSort ____________________________________________________________
     public static void quickSort(Producto[] S) {
        quickSort(S, 0, S.length - 1);
    }

    private static void quickSort(Producto[] S, int inicio, int fin) {
        if (inicio < fin) {
            int p = pivot(S, inicio, fin);
            quickSort(S, inicio, p - 1);
            quickSort(S, p + 1, fin);
        }
    }

    private static int pivot(Producto[] S, int inicio, int fin) {
        Producto pivote = S[fin];
        int k = inicio;
        int l = fin - 1;

        while (k < fin && comparar(S[k], pivote) <= 0) {
            k++;
        }
        while (l > inicio && comparar(S[l], pivote) > 0) {
            l--;
        }

        while (k < l) {
            intercambiar(S, k, l);

            while (comparar(S[k], pivote) <= 0) {
                k++;
            }
            while (comparar(S[l], pivote) > 0) {
                l--;
            }
        }

        intercambiar(S, k, fin);
        return k;
    }

    private static void intercambiar(Producto[] S, int a, int b) {
        Producto aux = S[a];
        movimientos++;
        S[a] = S[b];
        movimientos++;
        S[b] = aux;
        movimientos++;
    }
}
