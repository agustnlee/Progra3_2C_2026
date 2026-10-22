package com.ajs.tpo.algorithm;

import com.ajs.tpo.model.Iniciativa;
import java.util.ArrayList;
import java.util.List;

public class MochilaDinamica {
    public List<Iniciativa> seleccionar(
            List<Iniciativa> iniciativas, int presupuesto) {

        if (iniciativas == null) {
            throw new IllegalArgumentException(
                    "La lista de iniciativas no puede ser null");
        }

        if (presupuesto < 0) {
            throw new IllegalArgumentException(
                    "El presupuesto no puede ser negativo");
        }

        int n = iniciativas.size();

        // dp[i][p]: maximo impacto usando las primeras i iniciativas
        // con un presupuesto maximo p.
        int[][] dp = new int[n + 1][presupuesto + 1];

        for (int i = 1; i <= n; i++) {

            Iniciativa iniciativa = iniciativas.get(i - 1);

            if (iniciativa == null
                    || iniciativa.getCosto() <= 0
                    || iniciativa.getImpacto() < 0) {
                throw new IllegalArgumentException(
                        "Iniciativa invalida: costo positivo e impacto no negativo");
            }

            int costo = iniciativa.getCosto();
            int impacto = iniciativa.getImpacto();

            for (int p = 0; p <= presupuesto; p++) {

                int sinSeleccionar = dp[i - 1][p];

                int seleccionando = 0;

                if (costo <= p) {
                    seleccionando =
                            dp[i - 1][p - costo] + impacto;
                }

                dp[i][p] = Math.max(
                        sinSeleccionar, seleccionando);
            }
        }

        List<Iniciativa> seleccionadas = new ArrayList<>();

        int p = presupuesto;

        for (int i = n; i > 0; i--) {
            if (dp[i][p] != dp[i - 1][p]) {

                Iniciativa iniciativa = iniciativas.get(i - 1);

                seleccionadas.add(iniciativa);
                p = p - iniciativa.getCosto();
            }
        }

        return seleccionadas;
    }

    public int calcularImpactoMaximo(
            List<Iniciativa> iniciativas, int presupuesto) {

        List<Iniciativa> seleccionadas =
                seleccionar(iniciativas, presupuesto);

        int impactoTotal = 0;

        for (Iniciativa iniciativa : seleccionadas) {
            impactoTotal += iniciativa.getImpacto();
        }
        return impactoTotal;
    }
}

