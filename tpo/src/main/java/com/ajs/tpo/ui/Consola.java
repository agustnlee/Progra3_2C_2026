package com.ajs.tpo.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Entrada y salida comun a todos los modulos de la UI.
public class Consola {

    private static final int ANCHO = 60;
    private static final String CANCELAR = "-";

    private final Scanner in = new Scanner(System.in);
    private boolean avisado = false; // el aviso de cancelar se muestra una vez por operacion

    // Opcion de menu: no cancelable
    public int leerOpcion(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero.");
            }
        }
    }

    public int leerInt(String mensaje) {
        while (true) {
            String linea = leerLinea(mensaje);
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero.");
            }
        }
    }

    public String leerTexto(String mensaje) {
        return leerLinea(mensaje);
    }

    // Lee una linea (si es - se cancela)
    private String leerLinea(String mensaje) {
        if (!avisado) {
            System.out.println("(Ingrese - para cancelar operacion)");
            avisado = true;
        }
        System.out.print(mensaje);
        String linea = in.nextLine().trim();
        if (linea.equals(CANCELAR)) throw new CancelacionException();
        return linea;
    }

    // Ids separados por coma ("1, 3, 4"). Vacio = lista vacia
    public List<Integer> leerIds(String mensaje) {
        while (true) {
            String linea = leerLinea(mensaje);
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


    // Titulo
    public void titulo(String titulo) {
        String base = "-- " + titulo + " ";
        System.out.println("\n" + base + "-".repeat(Math.max(3, ANCHO - base.length())));
    }

    public void cierre() {
        System.out.println("-".repeat(ANCHO));
    }

    // Listado con separador arriba y abajo, una fila por elemento
    public void listado(String titulo, List<String> filas) {
        titulo(titulo);
        if (filas.isEmpty()) System.out.println("(sin elementos)");
        for (String fila : filas) System.out.println(fila);
        cierre();
    }

    public void mensaje(String texto) {
        System.out.println(texto);
    }

    // Ejecuta una accion mostrando los errores de validacion o la cancelacion en vez de cortar el programa
    public void seguro(Runnable accion) {
        avisado = false;
        try {
            accion.run();
        } catch (CancelacionException e) {
            System.out.println("Operacion cancelada.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}