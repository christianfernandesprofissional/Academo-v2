package com.academo.service.payment.asaas;

import com.academo.controller.PaymentController;
import com.academo.controller.dtos.payment.*;
import com.academo.controller.dtos.payment.enums.BillingType;
import com.academo.controller.dtos.payment.enums.ChargeType;
import com.academo.controller.dtos.payment.enums.SubscriptionCycle;
import com.academo.controller.dtos.paymentHistory.CreatePaymentHistoryDTO;
import com.academo.controller.dtos.paymentHistory.UpdatePaymentHistoryDTO;
import com.academo.model.PaymentHistory;
import com.academo.model.User;
import com.academo.model.enums.PaymentStatus;
import com.academo.model.enums.PlanType;
import com.academo.model.enums.UserRole;
import com.academo.service.payment.IPaymentService;
import com.academo.service.payment.history.IPaymentHistoryService;
import com.academo.service.user.IUserService;
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
import java.util.Map;

@Component
public class AsaasPaymentService implements IPaymentService {

    private static final BigDecimal YEARLY_IN_CASH = BigDecimal.valueOf(179.9);
    private static final BigDecimal MONTHLY_RECURRENT = BigDecimal.valueOf(15.9);
    private static final BigDecimal YEARLY_RECURRENT = BigDecimal.valueOf(149.9);
    private static final BigDecimal IN_INSTALLMENTS = BigDecimal.valueOf(179.9);

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final IPaymentHistoryService paymentHistoryService;
    private final IUserService userService;

    private String asaasUrl;
    private String apiKey;

    private String successUrl = "https://pt.wikipedia.org/wiki/John_von_Neumann"; //Muito provavelmente esta variável estará no meu config.env


    public AsaasPaymentService(@Value("${payment.gateway.url}") String asaasUrl, @Value("${payment.gateway.api-key}") String apiKey, IPaymentHistoryService paymentHistoryService, IUserService userService) {
        this.asaasUrl = asaasUrl;
        this.apiKey = apiKey;
        this.paymentHistoryService = paymentHistoryService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public PaymentLinkDTO createPaymentLink(Integer userId, PaymentOptionsDTO paymentOptionsDTO) {
        PlanTypeDataDTO planTypeData = getPlanTypeData(paymentOptionsDTO);
        CallbackPaymentDTO callbackPaymentDTO = new CallbackPaymentDTO(successUrl, false);
        GetPaymentLinkDTO getPaymentLinkDTO = GetPaymentLinkDTO.fromPaymentOptions(paymentOptionsDTO, planTypeData.value(), callbackPaymentDTO);
        PaymentLinkDTO response = requestPaymentLink(getPaymentLinkDTO);
        CreatePaymentHistoryDTO createPaymentHistoryDTO = new CreatePaymentHistoryDTO(response.id(), response.url(), PaymentStatus.WAITING_PAYMENT, response.value(), planTypeData.planType());
        paymentHistoryService.create(userId, createPaymentHistoryDTO);
        return response;
    }

    @Override
    public void receivePayment(Map<String, Object> body) {
        Map<String, Object> payment = (Map<String, Object>) body.get("payment");
        String paymentId = (String) payment.get("paymentLink");

        PaymentHistory paymentHistory = paymentHistoryService.findByPaymentId(paymentId);
        paymentHistory.setPaymentId(paymentId);
        paymentHistoryService.update(paymentHistory.getId(), new UpdatePaymentHistoryDTO(PaymentStatus.PAID));
        User user = userService.findById(paymentHistory.getUser().getId());
        user.setRole(UserRole.ROLE_PREMIUM);
        user.setPlanType(paymentHistory.getPlanType());
        userService.update(user);
    }


    private PlanTypeDataDTO getPlanTypeData(PaymentOptionsDTO paymentOptionsDTO) {
        if(paymentOptionsDTO.billingType() == BillingType.BOLETO || paymentOptionsDTO.billingType() == BillingType.PIX) {
            return new PlanTypeDataDTO(PlanType.YEARLY_IN_CASH, YEARLY_IN_CASH);
        }
        if(paymentOptionsDTO.chargeType() == ChargeType.INSTALLMENT) {
            return new PlanTypeDataDTO(PlanType.IN_INSTALLMENTS, IN_INSTALLMENTS);
        }
        if(paymentOptionsDTO.subscriptionCycle() == SubscriptionCycle.MONTHLY) {
            return new PlanTypeDataDTO(PlanType.MONTHLY_RECURRENT, MONTHLY_RECURRENT);
        }
        if(paymentOptionsDTO.subscriptionCycle() == SubscriptionCycle.YEARLY) {
            return new PlanTypeDataDTO(PlanType.YEARLY_RECURRENT, YEARLY_RECURRENT);
        }
        throw new PremiumPlanException("Erro ao defenir plano Premium");
    }

    private PaymentLinkDTO requestPaymentLink(GetPaymentLinkDTO getPaymentLinkDTO) {
        RestClient restClient = RestClient.create();
        PaymentLinkDTO paymentLinkDTO;
        try {
            paymentLinkDTO = restClient.post().uri(asaasUrl).header("access_token", apiKey)
                    .contentType(MediaType.APPLICATION_JSON).body(getPaymentLinkDTO).retrieve().body(PaymentLinkDTO.class);
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
