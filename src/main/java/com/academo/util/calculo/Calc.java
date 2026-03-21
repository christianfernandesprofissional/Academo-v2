package com.academo.util.calculo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Calc {

    public static BigDecimal mediaAritmetica(List<BigDecimal> notas){
        BigDecimal soma = new BigDecimal("0");
        for(BigDecimal nota : notas) soma = soma.add(nota);
        return soma.divide(new BigDecimal(notas.size()), 2, RoundingMode.HALF_UP);
    }

    /**
     * Recebe uma matriz com n linhas e 2 colunas representando nota na primeira coluna e peso na segunda coluna
     * @param notasEPesos
     * @return Valor da média ponderada
     */
    public static BigDecimal mediaPonderada(BigDecimal[][] notasEPesos){


        BigDecimal notasSomadas = new BigDecimal("0");
        BigDecimal pesosSomados = new BigDecimal("0");
        BigDecimal media = new BigDecimal("0");

        //  (n1*peso)+ (nota2*peso) / somaDosPesos
        for (int i = 0; i < notasEPesos.length; i++) {
            notasSomadas = notasSomadas.add( (notasEPesos[i][0]).multiply(notasEPesos[i][1]) );
            pesosSomados = pesosSomados.add(notasEPesos[i][1]);
        }

        media = media.add(notasSomadas.divide(pesosSomados, 2, RoundingMode.HALF_UP));

        return media;
    }


}
