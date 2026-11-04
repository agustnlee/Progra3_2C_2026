package com.ajs.tpo.ui;

import java.util.List;

public class MenuPrincipal {

    private final Consola consola;
    private final List<ModuloCLI> modulos;

    public MenuPrincipal(Consola consola, List<ModuloCLI> modulos) {
        this.consola = consola;
        this.modulos = modulos;
    }

    public void iniciar() {
        int op;
        do {
            consola.titulo("--- RED SOCIAL UNIVERSITARIA - ADMINISTRADOR ---");
            for (int i = 0; i < modulos.size(); i++) {
                System.out.println((i + 1) + ". " + modulos.get(i).nombre());
            }
            System.out.println("0. Salir");
            consola.cierre();
            op = consola.leerInt("Opcion: ");
            if (op >= 1 && op <= modulos.size()) modulos.get(op - 1).ejecutar();
            else if (op != 0) consola.mensaje("Opcion invalida.");
        } while (op != 0);
        consola.mensaje("Apagando Sistema...");
    }
}