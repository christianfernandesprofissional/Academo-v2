package com.academo.service.payment.asaas;

import com.academo.controller.PaymentController;
import com.academo.controller.dtos.payment.CallbackPaymentDTO;
import com.academo.controller.dtos.payment.GetPaymentLinkDTO;
import com.academo.controller.dtos.payment.PaymentLinkDTO;
import com.academo.controller.dtos.payment.PaymentOptionsDTO;
import com.academo.controller.dtos.payment.enums.BillingType;
import com.academo.controller.dtos.payment.enums.ChargeType;
import com.academo.controller.dtos.payment.enums.SubscriptionCycle;
import com.academo.controller.dtos.paymentHistory.CreatePaymentHistoryDTO;
import com.academo.controller.dtos.paymentHistory.PaymentHistoryDTO;
import com.academo.model.enums.PaymentStatus;
import com.academo.service.payment.IPaymentService;
import com.academo.service.payment.history.IPaymentHistoryService;
import com.academo.util.exceptions.payment.PaymentLinkException;
import com.academo.util.exceptions.payment.PremiumPlanException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class AsaasPaymentService implements IPaymentService {

    private static final Double YEARLY_IN_CASH = 179.9;
    private static final Double MONTHLY_RECURRENT = 15.9;
    private static final Double YEARLY_RECURRENT = 149.9;
    private static final Double IN_INSTALLMENTS = 179.9;

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final IPaymentHistoryService paymentHistoryService;

    private String asaasUrl;
    private String apiKey;

    private String successUrl = "https://pt.wikipedia.org/wiki/John_von_Neumann"; //Muito provavelmente esta variável estará no meu config.env


    public AsaasPaymentService(@Value("${payment.gateway.url}") String asaasUrl, @Value("${payment.gateway.api-key}") String apiKey, IPaymentHistoryService paymentHistoryService) {
        this.asaasUrl = asaasUrl;
        this.apiKey = apiKey;
        this.paymentHistoryService = paymentHistoryService;
    }


    @Override
    @Transactional
    public PaymentLinkDTO createPaymentLink(Integer userId, PaymentOptionsDTO paymentOptionsDTO) {
        log.debug("PaymentOptionsDTO: " + paymentOptionsDTO);
        BigDecimal price = BigDecimal.valueOf(getPlanPrice(paymentOptionsDTO));
        log.debug("Price: " + price);
        CallbackPaymentDTO callbackPaymentDTO = new CallbackPaymentDTO(successUrl, false);
        log.debug("CallbackPaymentDTO: " + callbackPaymentDTO);
        GetPaymentLinkDTO getPaymentLinkDTO = GetPaymentLinkDTO.fromPaymentOptions(paymentOptionsDTO, price, callbackPaymentDTO);
        log.debug("GetPaymentLinkDTO: " + getPaymentLinkDTO);
        PaymentLinkDTO response = requestPaymentLink(getPaymentLinkDTO);
        CreatePaymentHistoryDTO createPaymentHistoryDTO = new CreatePaymentHistoryDTO(response.id(), response.url(), PaymentStatus.WAITING_PAYMENT, response.value());
        paymentHistoryService.create(userId, createPaymentHistoryDTO);
        return response;
    }

    private Double getPlanPrice(PaymentOptionsDTO paymentOptionsDTO) {
        if(paymentOptionsDTO.billingType() == BillingType.BOLETO || paymentOptionsDTO.billingType() == BillingType.PIX) {
            return YEARLY_IN_CASH;
        }
        if(paymentOptionsDTO.chargeType() == ChargeType.INSTALLMENT) {
            return IN_INSTALLMENTS;
        }
        if(paymentOptionsDTO.subscriptionCycle() == SubscriptionCycle.MONTHLY) {
            return MONTHLY_RECURRENT;
        }
        if(paymentOptionsDTO.subscriptionCycle() == SubscriptionCycle.YEARLY) {
            return YEARLY_RECURRENT;
        }
        throw new PremiumPlanException("Erro ao defenir plano Premium");
    }

    private PaymentLinkDTO requestPaymentLink(GetPaymentLinkDTO getPaymentLinkDTO) {
        RestClient restClient = RestClient.create();
        log.debug("Rest Client: " + restClient);
        PaymentLinkDTO paymentLinkDTO;
        try {
            paymentLinkDTO = restClient.post().uri(asaasUrl).header("access_token", apiKey)
                    .contentType(MediaType.APPLICATION_JSON).body(getPaymentLinkDTO).retrieve().body(PaymentLinkDTO.class);
            log.debug("PaymentLinkDTO: " + paymentLinkDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PaymentLinkException("Erro ao gerar link de pagamento: " + e.getMessage());
        }
        if(paymentLinkDTO == null) {
            throw new PaymentLinkException("Erro ao gerar link de pagamento. PaymentLinkDTO É NULL");
        }
        return paymentLinkDTO;
    }
}
