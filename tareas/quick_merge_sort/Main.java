import java.util.Arrays;

public class Main {

    // TODO: falta crear mock muestras ordenada, aleatoria, etc. Dejo hecho la estructura
    public static void main(String[] args) {


        // estrutctura para test 

        Producto[] productosMerge = crearProductosDeEjemplo();
        Producto[] productosQuick = Arrays.copyOf(productosMerge, productosMerge.length);

        System.out.println("Antes de ordenar:");
        imprimirProductos(productosMerge);

        // __ MergeSort ____________________________________________________________
        AlgoritmosOrdenamiento.reiniciarContadores();
        long inicioMerge = System.nanoTime();
        AlgoritmosOrdenamiento.mergeSort(productosMerge);
        long finMerge = System.nanoTime();

        System.out.println("\nDespues de Merge Sort:");
        imprimirProductos(productosMerge);
        System.out.println("Comparaciones: " + AlgoritmosOrdenamiento.comparaciones);
        System.out.println("Movimientos: " + AlgoritmosOrdenamiento.movimientos);
        System.out.println("Tiempo: " + (finMerge - inicioMerge) + " ns");

        // __ QuickSort ____________________________________________________________
        AlgoritmosOrdenamiento.reiniciarContadores();
        long inicioQuick = System.nanoTime();
        AlgoritmosOrdenamiento.quickSort(productosQuick);
        long finQuick = System.nanoTime();

        System.out.println("\nDespues de Quick Sort:");
        imprimirProductos(productosQuick);
        System.out.println("Comparaciones: " + AlgoritmosOrdenamiento.comparaciones);
        System.out.println("Movimientos: " + AlgoritmosOrdenamiento.movimientos);
        System.out.println("Tiempo: " + (finQuick - inicioQuick) + " ns");
    }

    private static Producto[] crearProductosDeEjemplo() {
        return new Producto[] {
            new Producto(1, "Auriculares", 15000, 4.5),
            new Producto(2, "Mouse", 8000, 4.8),
            new Producto(3, "Teclado", 12000, 4.5), 
            new Producto(4, "Monitor", 90000, 3.9),
            new Producto(5, "Webcam", 20000, 4.8),  
            new Producto(6, "Parlante", 25000, 5.0),
            new Producto(7, "Notebook", 500000, 4.2)
        };
    }

    private static void imprimirProductos(Producto[] productos) {
        for (Producto p : productos) {
            System.out.println("  " + p);
        }
    }
}