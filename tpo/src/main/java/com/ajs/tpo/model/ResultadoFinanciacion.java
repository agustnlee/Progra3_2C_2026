package com.ajs.tpo.model;

import java.util.List;

public class ResultadoFinanciacion {

    private List<Iniciativa> seleccionadas;
    private int costoTotal;
    private int impactoTotal;
    private int presupuestoRestante;

    public ResultadoFinanciacion(
            List<Iniciativa> seleccionadas,
            int costoTotal,
            int impactoTotal,
            int presupuestoRestante) {

        this.seleccionadas = seleccionadas;
        this.costoTotal = costoTotal;
        this.impactoTotal = impactoTotal;
        this.presupuestoRestante = presupuestoRestante;
    }

    public List<Iniciativa> getSeleccionadas() {
        return seleccionadas;
    }

    public int getCostoTotal() {
        return costoTotal;
    }

    public int getImpactoTotal() {
        return impactoTotal;
    }

    public int getPresupuestoRestante() {
        return presupuestoRestante;
    }
}
