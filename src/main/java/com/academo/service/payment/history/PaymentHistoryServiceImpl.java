package com.academo.service.payment.history;

import com.academo.controller.dtos.paymentHistory.CreatePaymentHistoryDTO;
import com.academo.controller.dtos.paymentHistory.PaymentHistoryDTO;
import com.academo.controller.dtos.paymentHistory.UpdatePaymentHistoryDTO;
import com.academo.model.PaymentHistory;
import com.academo.model.User;
import com.academo.repository.PaymentHistoryRepository;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.payment.history.PaymentHistoryNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentHistoryServiceImpl implements IPaymentHistoryService {

    private final IUserService userService;
    private final PaymentHistoryRepository paymentHistoryRepository;

    public PaymentHistoryServiceImpl(IUserService userService, PaymentHistoryRepository paymentHistoryRepository) {
        this.userService = userService;
        this.paymentHistoryRepository = paymentHistoryRepository;
    }


    @Override
    public PaymentHistoryDTO findById(Integer paymentHistoryId, Integer userId) {
        return PaymentHistoryDTO.fromPaymentHistory(paymentHistoryRepository.findByIdAndUserId(paymentHistoryId, userId).orElseThrow(PaymentHistoryNotFoundException::new));
    }

    @Override
    public List<PaymentHistoryDTO> findAll(Integer userId) {
        return paymentHistoryRepository.findAllByUserId(userId).stream().map(PaymentHistoryDTO::fromPaymentHistory).toList();
    }

    @Override
    public void create(Integer userId, CreatePaymentHistoryDTO createPaymentHistoryDTO) {
        User user = userService.findById(userId);
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setUser(user);
        paymentHistory.setPaymentId(createPaymentHistoryDTO.paymentId());
        paymentHistory.setStatus(createPaymentHistoryDTO.paymentStatus());
        paymentHistory.setValue(createPaymentHistoryDTO.value());
        paymentHistoryRepository.save(paymentHistory);
    }

    @Override
    public void update(Integer paymentHistoryId, UpdatePaymentHistoryDTO updatePaymentHistoryDTO) {
        PaymentHistory paymentHistory = paymentHistoryRepository.findById(paymentHistoryId).orElseThrow(PaymentHistoryNotFoundException::new);
        paymentHistory.setStatus(updatePaymentHistoryDTO.paymentStatus());
        paymentHistory.setValue(updatePaymentHistoryDTO.value());
        paymentHistoryRepository.save(paymentHistory);
    }
}
