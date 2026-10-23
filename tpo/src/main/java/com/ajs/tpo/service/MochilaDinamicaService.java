package com.ajs.tpo.service;

import com.ajs.tpo.model.Iniciativa;
import com.ajs.tpo.algorithm.MochilaDinamica;
import com.ajs.tpo.model.ResultadoFinanciacion;
import java.util.List;

public class MochilaDinamicaService {

    private MochilaDinamica mochilaDinamica;

    public MochilaDinamicaService() {
        this.mochilaDinamica = new MochilaDinamica();
    }

    public ResultadoFinanciacion seleccionarIniciativas(
            List<Iniciativa> iniciativas, int presupuesto) {

        if (presupuesto < 0) {
            throw new IllegalArgumentException(
                    "El presupuesto no puede ser negativo");
        }

        List<Iniciativa> seleccionadas =
                mochilaDinamica.seleccionar(iniciativas, presupuesto);
        int costoTotal = 0;
        int impactoTotal = 0;

        for (Iniciativa iniciativa : seleccionadas) {
            costoTotal += iniciativa.getCosto();
            impactoTotal += iniciativa.getImpacto();
        }

        int presupuestoRestante = presupuesto - costoTotal;

        return new ResultadoFinanciacion(
                seleccionadas,
                costoTotal,
                impactoTotal,
                presupuestoRestante
        );
    }

    public int obtenerImpactoMaximo(
            List<Iniciativa> iniciativas, int presupuesto) {

        return mochilaDinamica.calcularImpactoMaximo(
                iniciativas, presupuesto);
    }
}
