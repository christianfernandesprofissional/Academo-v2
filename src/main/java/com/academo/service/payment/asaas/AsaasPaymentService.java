package com.academo.service.payment.asaas;

import com.academo.controller.dtos.payment.CallbackPaymentDTO;
import com.academo.controller.dtos.payment.GetPaymentLinkDTO;
import com.academo.controller.dtos.payment.PaymentLinkDTO;
import com.academo.controller.dtos.payment.PaymentOptionsData;
import com.academo.controller.dtos.payment.enums.BillingType;
import com.academo.controller.dtos.payment.enums.ChargeType;
import com.academo.controller.dtos.payment.enums.SubscriptionCycle;
import com.academo.service.payment.IPaymentService;
import com.academo.util.exceptions.payment.PaymentLinkException;
import com.academo.util.exceptions.payment.PremiumPlanException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AsaasPaymentService implements IPaymentService {

    private static final Double ANUAL_A_VISTA = 179.9;
    private static final Double RECORRENTE_MENSAL = 15.9;
    private static final Double RECORRENTE_ANUAL = 149.9;
    private static final Double PARCELADO = 179.9;

    private String asaasUrl;
    private String apiKey;

    private String successUrl = "URL DE DIRECIONAMENTO"; //Muito provavelmente esta variável estará no meu config.env

    public AsaasPaymentService(@Value("${payment.gateway.url}") String asaasUrl, @Value("${payment.gateway.api-key}") String apiKey) {
        this.asaasUrl = asaasUrl;
        this.apiKey = apiKey;
    }


    @Override
    public PaymentLinkDTO createPaymentLink(PaymentOptionsData paymentOptionsData) {
        Double price = getPlanPrice(paymentOptionsData);
        CallbackPaymentDTO callbackPaymentDTO = new CallbackPaymentDTO(successUrl, true);
        GetPaymentLinkDTO getPaymentLinkDTO = GetPaymentLinkDTO.fromPaymentOptions(paymentOptionsData, price, callbackPaymentDTO);
        return requestPaymentLink(getPaymentLinkDTO);
    }

    private Double getPlanPrice(PaymentOptionsData paymentOptionsData) {
        if(paymentOptionsData.billingType() == BillingType.BOLETO || paymentOptionsData.billingType() == BillingType.PIX) {
            return ANUAL_A_VISTA;
        }
        if(paymentOptionsData.chargeType() == ChargeType.INSTALLMENT) {
            return PARCELADO;
        }
        if(paymentOptionsData.subscriptionCycle() == SubscriptionCycle.MONTHLY) {
            return RECORRENTE_MENSAL;
        }
        if(paymentOptionsData.subscriptionCycle() == SubscriptionCycle.YEARLY) {
            return RECORRENTE_ANUAL;
        }
        throw new PremiumPlanException("Erro ao defenir plano Premium");
    }

    private PaymentLinkDTO requestPaymentLink(GetPaymentLinkDTO getPaymentLinkDTO) {
        RestClient restClient = RestClient.create();
        PaymentLinkDTO paymentLinkDTO;
        try {
            paymentLinkDTO = restClient.post().uri(asaasUrl + "/paymentLinks").header("access_token", apiKey)
                    .contentType(MediaType.APPLICATION_JSON).body(getPaymentLinkDTO).retrieve().body(PaymentLinkDTO.class);

        } catch (Exception e) {
            throw new PaymentLinkException("Erro ao gerar link de pagamento");
        }
        if(paymentLinkDTO == null) {
            throw new PaymentLinkException("Erro ao gerar link de pagamento");
        }
        return paymentLinkDTO;
    }
}
