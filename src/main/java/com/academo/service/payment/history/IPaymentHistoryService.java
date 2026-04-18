package com.academo.service.payment.history;

import com.academo.controller.dtos.paymentHistory.CreatePaymentHistoryDTO;
import com.academo.controller.dtos.paymentHistory.PaymentHistoryDTO;
import com.academo.controller.dtos.paymentHistory.UpdatePaymentHistoryDTO;
import com.academo.model.PaymentHistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface IPaymentHistoryService {

    PaymentHistoryDTO findById(Integer paymentHistoryId, Integer userId);
    PaymentHistory findByPaymentId(String paymentId);
    Page<PaymentHistoryDTO> findAll(Integer userId, Pageable pageable);
    PaymentHistoryDTO findLastPayment(Integer userId);
    void updateDueDate(String paymentId, LocalDate dueDate);
    void verifyExpiredPayments(Integer userId);
    void cancelPlan(Integer userId);
    void create(Integer userId, CreatePaymentHistoryDTO createPaymentHistoryDTO);
    void update(Integer paymentHistoryId, UpdatePaymentHistoryDTO updatePaymentHistoryDTO);

}
