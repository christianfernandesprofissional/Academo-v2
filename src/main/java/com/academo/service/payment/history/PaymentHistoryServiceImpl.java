package com.academo.service.payment.history;

import com.academo.controller.dtos.paymentHistory.CreatePaymentHistoryDTO;
import com.academo.controller.dtos.paymentHistory.PaymentHistoryDTO;
import com.academo.controller.dtos.paymentHistory.UpdatePaymentHistoryDTO;
import com.academo.model.PaymentHistory;
import com.academo.model.User;
import com.academo.model.enums.payment.PaymentStatus;
import com.academo.repository.PaymentHistoryRepository;
import com.academo.repository.UserRepository;
import com.academo.service.user.IUserService;
import com.academo.util.exceptions.payment.history.PaymentHistoryNotFoundException;
import com.academo.util.exceptions.user.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class PaymentHistoryServiceImpl implements IPaymentHistoryService {

    private final UserRepository userRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    public PaymentHistoryServiceImpl(UserRepository userRepository, PaymentHistoryRepository paymentHistoryRepository) {
        this.userRepository = userRepository;
        this.paymentHistoryRepository = paymentHistoryRepository;
    }


    @Override
    public PaymentHistoryDTO findById(Integer paymentHistoryId, Integer userId) {
        return PaymentHistoryDTO.fromPaymentHistory(paymentHistoryRepository.findByIdAndUserId(paymentHistoryId, userId).orElseThrow(PaymentHistoryNotFoundException::new));
    }

    @Override
    public Page<PaymentHistoryDTO> findAll(Integer userId, Pageable pageable) {
        return paymentHistoryRepository.findAllByUserId(userId, pageable).map(PaymentHistoryDTO::fromPaymentHistory);
    }

    @Override
    public PaymentHistoryDTO findLastPayment(Integer userId) {
        return paymentHistoryRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
                .map(PaymentHistoryDTO::fromPaymentHistory)
                .orElse(null);
    }

    @Override
    public void updateDueDate(String paymentId, LocalDate dueDate) {
        PaymentHistory paymentHistory = findByPaymentId(paymentId);
        paymentHistory.setPlanDueDate(dueDate);
        paymentHistoryRepository.save(paymentHistory);
    }

    @Override
    public void verifyExpiredPayments(Integer userId) {
        List<PaymentHistory> allPayments = paymentHistoryRepository.findAllByUserId(userId);
        for(PaymentHistory p : allPayments) {
            if(p.getCreatedAt().plusDays(10).isBefore(LocalDateTime.now()) && p.getStatus() == PaymentStatus.WAITING_PAYMENT) {
                update(p.getId(), new UpdatePaymentHistoryDTO(PaymentStatus.EXPIRED, p.getPlanDueDate()));
            }
        }
    }

    @Override
    public void cancelPlan(Integer userId) {
        PaymentHistoryDTO paymentHistoryDTO = findLastPayment(userId);
        update(paymentHistoryDTO.paymentHistoryId(), new UpdatePaymentHistoryDTO(PaymentStatus.CANCELED, paymentHistoryDTO.planDueDate()));

    }

    @Override
    public PaymentHistory findByPaymentId(String paymentId) {
        return paymentHistoryRepository.findByPaymentId(paymentId).orElseThrow(PaymentHistoryNotFoundException::new);
    }

    @Override
    public void create(Integer userId, CreatePaymentHistoryDTO createPaymentHistoryDTO) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setUser(user);
        paymentHistory.setPaymentId(createPaymentHistoryDTO.paymentId());
        paymentHistory.setStatus(createPaymentHistoryDTO.paymentStatus());
        paymentHistory.setValue(createPaymentHistoryDTO.value());
        paymentHistory.setUrl(createPaymentHistoryDTO.url());
        paymentHistory.setPlanType(createPaymentHistoryDTO.planType());
        paymentHistory.setPlanDueDate(LocalDate.now().plusDays(30));
        paymentHistoryRepository.save(paymentHistory);
    }

    @Override
    public void update(Integer paymentHistoryId, UpdatePaymentHistoryDTO updatePaymentHistoryDTO) {
        PaymentHistory paymentHistory = paymentHistoryRepository.findById(paymentHistoryId).orElseThrow(PaymentHistoryNotFoundException::new);
        paymentHistory.setStatus(updatePaymentHistoryDTO.paymentStatus());
        paymentHistory.setPlanDueDate(updatePaymentHistoryDTO.planDueDate());
        paymentHistoryRepository.save(paymentHistory);
    }
}
