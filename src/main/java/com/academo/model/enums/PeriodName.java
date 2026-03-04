package com.academo.model.enums;

public enum PeriodName {
    P1(1),
    P2(2),
    P3(3);

    private final Integer codigo;

    PeriodName(Integer codigo){
        this.codigo = codigo;
    }

    public Integer getCodigo(){
        return codigo;
    }

    public static PeriodName fromCodigo(Integer codigo){
        for(PeriodName periodName : PeriodName.values()){
            if(periodName.getCodigo().equals(codigo)){
                return periodName;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }


}
