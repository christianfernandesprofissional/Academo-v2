package com.academo.repository;

import com.academo.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Integer> {

    List<PaymentHistory> findAllByUserId(Integer userId);
    Optional<PaymentHistory> findByIdAndUserId(Integer paymentHistoryId, Integer userId);
}
