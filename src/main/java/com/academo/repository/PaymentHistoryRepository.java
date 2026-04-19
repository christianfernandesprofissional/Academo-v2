package com.academo.repository;

import com.academo.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Integer> {

    List<PaymentHistory> findAllByUserId(Integer userId);
    Page<PaymentHistory> findAllByUserId(Integer userId, Pageable pageable);
    Optional<PaymentHistory> findFirstByUserIdOrderByCreatedAtDesc(Integer userId);
    Optional<PaymentHistory> findByIdAndUserId(Integer paymentHistoryId, Integer userId);
    Optional<PaymentHistory> findByPaymentId(String paymentId);
}
