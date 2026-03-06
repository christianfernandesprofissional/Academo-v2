package com.academo.model.enums;

public enum CalculationType {
    MEDIA_ARITMETICA(1),
    MEDIA_PONDERADA(2);

    private final Integer codigo;

    CalculationType(Integer codigo){
        this.codigo = codigo;
    }

    public Integer getCodigo(){
        return codigo;
    }

    public static CalculationType fromCodigo(Integer codigo){
        for(CalculationType calculationType : CalculationType.values()){
            if(calculationType.getCodigo().equals(codigo)){
                return calculationType;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
