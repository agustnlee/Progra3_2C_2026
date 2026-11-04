package com.ajs.tpo.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Entrada y salida comun a todos los modulos de la UI
public class Consola {

    private static final int ANCHO = 60;
    private final Scanner in = new Scanner(System.in);

    public int leerInt(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero.");
            }
        }
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return in.nextLine().trim();
    }

    // Ids separados ("1, 3, 4") y Vacio = lista vacia
    public List<Integer> leerIds(String mensaje) {
        while (true) {
            String linea = leerTexto(mensaje);
            List<Integer> ids = new ArrayList<>();
            try {
                if (!linea.isEmpty()) {
                    for (String parte : linea.split(",")) ids.add(Integer.parseInt(parte.trim()));
                }
                return ids;
            } catch (NumberFormatException e) {
                System.out.println("Formato invalido. Ejemplo: 1, 3, 4");
            }
        }
    }

    // titulo
    public void titulo(String titulo) {
        String base = "-- " + titulo + " ";
        System.out.println("\n" + base + "-".repeat(Math.max(3, ANCHO - base.length())));
    }

    public void cierre() {
        System.out.println("-".repeat(ANCHO));
    }

    // Para Listado, con separador arriba y abajo, una fila por elemento
    public void listado(String titulo, List<String> filas) {
        titulo(titulo);
        if (filas.isEmpty()) System.out.println("(sin elementos)");
        for (String fila : filas) System.out.println(fila);
        cierre();
    }

    public void mensaje(String texto) {
        System.out.println(texto);
    }

    // Ejecuta una accion mostrando el error de validacion en vez de cortar el programa (para las excepciones)
    public void seguro(Runnable accion) {
        try {
            accion.run();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}