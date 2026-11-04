package com.ajs.tpo.service;

import com.ajs.tpo.model.Iniciativa;
import com.ajs.tpo.algorithm.MochilaDinamica;
import com.ajs.tpo.model.ResultadoFinanciacion;
import com.ajs.tpo.repository.IniciativaRepository;

import java.util.List;

public class MochilaDinamicaService {

    private MochilaDinamica mochilaDinamica;
    private final IniciativaRepository iniciativas;

    public MochilaDinamicaService() {
        this.mochilaDinamica = new MochilaDinamica();
        this.iniciativas = null;
    }

    public MochilaDinamicaService(IniciativaRepository iniciativas) {
        this.iniciativas = iniciativas;
    }

    public ResultadoFinanciacion seleccionarIniciativas(int presupuesto) {
        if (iniciativas == null) {
            throw new IllegalStateException("El servicio no tiene repositorio de iniciativas");
        }
        return seleccionarIniciativas(iniciativas.listar(), presupuesto);
    }

    public ResultadoFinanciacion seleccionarIniciativas(List<Iniciativa> lista, int presupuesto) {
        if (presupuesto < 0) {
            throw new IllegalArgumentException("El presupuesto no puede ser negativo");
        }

        List<Iniciativa> seleccionadas = 
                mochilaDinamica.seleccionar(lista, presupuesto);
        int costoTotal = 0;
        int impactoTotal = 0;

        for (Iniciativa iniciativa : seleccionadas) {
            costoTotal += iniciativa.getCosto();
            impactoTotal += iniciativa.getImpacto();
        }

        int presupuestoRestante = presupuesto - costoTotal;

        return new ResultadoFinanciacion(seleccionadas, costoTotal, impactoTotal, presupuestoRestante);
    }


    public int obtenerImpactoMaximo(
            List<Iniciativa> iniciativas, int presupuesto) {

        return mochilaDinamica.calcularImpactoMaximo(
                iniciativas, presupuesto);
    }
}
