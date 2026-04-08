package com.academo.util.calculo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Calc {

    public static BigDecimal mediaAritmetica(List<BigDecimal> notas){
        if (notas == null || notas.isEmpty()) {
            return BigDecimal.ZERO; // ou null, dependendo da regra
        }

        BigDecimal soma = BigDecimal.ZERO;
        for(BigDecimal nota : notas) {
            soma = soma.add(nota);
        }

        return soma.divide(BigDecimal.valueOf(notas.size()), 2, RoundingMode.HALF_UP);
    }

    /**
     * Recebe uma matriz com n linhas e 2 colunas representando nota na primeira coluna e peso na segunda coluna
     * @param notasEPesos
     * @return Valor da média ponderada
     */
    public static BigDecimal mediaPonderada(BigDecimal[][] notasEPesos){

        BigDecimal notasSomadas = BigDecimal.ZERO;
        BigDecimal pesosSomados = BigDecimal.ZERO;

        for (int i = 0; i < notasEPesos.length; i++) {
            BigDecimal nota = notasEPesos[i][0];
            BigDecimal peso = notasEPesos[i][1];

            if (nota == null || peso == null) continue;

            notasSomadas = notasSomadas.add(nota.multiply(peso));
            pesosSomados = pesosSomados.add(peso);
        }

        // PROTEÇÃO CONTRA DIVISÃO POR ZERO
        if (pesosSomados.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return notasSomadas.divide(pesosSomados, 2, RoundingMode.HALF_UP);
    }


}
