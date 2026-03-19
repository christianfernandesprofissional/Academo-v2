package com.academo.controller.dtos.payment;

import com.academo.controller.dtos.payment.enums.BillingType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreditCardBillingDTO(
        @NotNull(message = "É obrigatório informar o tipo de pagamento")
        BillingType billingType,
        @NotEmpty(message = "É obrigatório informar o valor do pagamento")
        @Pattern(regexp = "^\\d{1,3}(\\.\\d{3})*,\\d{2}$")
        String value,
        @Future(message = "A data de vencimento deve ser no futuro")
        LocalDate dueDate,
        @Max(value = 12, message = "O máximo de parcelas é 12")
        Integer installmentCount,
        @Pattern(regexp = "^\\d{1,3}(\\.\\d{3})*,\\d{2}$")
        String totalValue,
        @Pattern(regexp = "^\\d{1,3}(\\.\\d{3})*,\\d{2}$")
        String installmentValue
) {
}
