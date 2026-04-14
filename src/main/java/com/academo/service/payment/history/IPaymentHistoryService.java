package com.academo.service.payment.history;

import com.academo.controller.dtos.paymentHistory.CreatePaymentHistoryDTO;
import com.academo.controller.dtos.paymentHistory.PaymentHistoryDTO;
import com.academo.controller.dtos.paymentHistory.UpdatePaymentHistoryDTO;
import com.academo.model.PaymentHistory;

import java.time.LocalDate;
import java.util.List;

public interface IPaymentHistoryService {

    PaymentHistoryDTO findById(Integer paymentHistoryId, Integer userId);
    PaymentHistory findByPaymentId(String paymentId);
    List<PaymentHistoryDTO> findAll(Integer userId);
    PaymentHistoryDTO findLastPayment(Integer userId);
    void updateDueDate(String paymentId, LocalDate dueDate);
    void create(Integer userId, CreatePaymentHistoryDTO createPaymentHistoryDTO);
    void update(Integer paymentHistoryId, UpdatePaymentHistoryDTO updatePaymentHistoryDTO);

}
