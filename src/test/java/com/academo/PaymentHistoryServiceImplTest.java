package com.academo;

import com.academo.controller.dtos.paymentHistory.CreatePaymentHistoryDTO;
import com.academo.controller.dtos.paymentHistory.PaymentHistoryDTO;
import com.academo.controller.dtos.paymentHistory.UpdatePaymentHistoryDTO;
import com.academo.model.PaymentHistory;
import com.academo.model.User;
import com.academo.model.enums.payment.PaymentStatus;
import com.academo.model.enums.user.PlanType;
import com.academo.repository.PaymentHistoryRepository;
import com.academo.repository.UserRepository;
import com.academo.service.payment.history.PaymentHistoryServiceImpl;
import com.academo.util.exceptions.payment.history.PaymentHistoryNotFoundException;
import com.academo.util.exceptions.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentHistoryServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentHistoryRepository paymentHistoryRepository;

    @Spy
    @InjectMocks
    private PaymentHistoryServiceImpl service;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1);
        user.setName("Fabio");
        user.setEmail("fabio@email.com");
    }

    @Test
    void shouldFindById() {
        PaymentHistory ph = new PaymentHistory();
        ph.setId(10);
        ph.setUser(user);
        ph.setPaymentId("pay_1");
        ph.setStatus(PaymentStatus.WAITING_PAYMENT);
        ph.setValue(new BigDecimal("10.00"));
        ph.setPlanType(PlanType.YEARLY_IN_CASH);
        ph.setPlanDueDate(LocalDate.now().plusDays(30));

        when(paymentHistoryRepository.findByIdAndUserId(10, 1)).thenReturn(Optional.of(ph));

        PaymentHistoryDTO dto = service.findById(10, 1);

        assertNotNull(dto);
        assertEquals(10, dto.paymentHistoryId());
    }

    @Test
    void shouldThrowWhenPaymentHistoryNotFound() {
        when(paymentHistoryRepository.findByIdAndUserId(10, 1)).thenReturn(Optional.empty());

        assertThrows(PaymentHistoryNotFoundException.class, () -> service.findById(10, 1));
    }

    @Test
    void shouldFindAll() {
        PaymentHistory ph = new PaymentHistory();
        ph.setId(10);
        ph.setUser(user);
        ph.setPaymentId("pay_1");
        ph.setStatus(PaymentStatus.WAITING_PAYMENT);
        ph.setValue(new BigDecimal("10.00"));
        ph.setPlanType(PlanType.YEARLY_IN_CASH);
        ph.setPlanDueDate(LocalDate.now().plusDays(30));

        PageRequest pageable = PageRequest.of(0, 10);
        when(paymentHistoryRepository.findAllByUserId(1, pageable)).thenReturn(new PageImpl<>(List.of(ph), pageable, 1));

        Page<PaymentHistoryDTO> page = service.findAll(1, pageable);

        assertEquals(1, page.getTotalElements());
    }

    @Test
    void shouldReturnNullWhenNoLastPayment() {
        when(paymentHistoryRepository.findFirstByUserIdOrderByCreatedAtDesc(1)).thenReturn(Optional.empty());

        PaymentHistoryDTO dto = service.findLastPayment(1);

        assertNull(dto);
    }

    @Test
    void shouldUpdateDueDate() {
        PaymentHistory ph = new PaymentHistory();
        ph.setId(10);
        ph.setPaymentId("pay_1");

        when(paymentHistoryRepository.findByPaymentId("pay_1")).thenReturn(Optional.of(ph));

        service.updateDueDate("pay_1", LocalDate.now().plusDays(1));

        verify(paymentHistoryRepository).save(ph);
    }

    @Test
    void shouldVerifyExpiredPaymentsAndMarkAsExpired() {
        PaymentHistory expiredWaiting = new PaymentHistory();
        expiredWaiting.setId(10);
        expiredWaiting.setUser(user);
        expiredWaiting.setStatus(PaymentStatus.WAITING_PAYMENT);
        expiredWaiting.setPlanDueDate(LocalDate.now().plusDays(30));
        expiredWaiting.setCreatedAt(LocalDateTime.now().minusDays(11));

        PaymentHistory okWaiting = new PaymentHistory();
        okWaiting.setId(11);
        okWaiting.setUser(user);
        okWaiting.setStatus(PaymentStatus.WAITING_PAYMENT);
        okWaiting.setPlanDueDate(LocalDate.now().plusDays(30));
        okWaiting.setCreatedAt(LocalDateTime.now().minusDays(2));

        when(paymentHistoryRepository.findAllByUserId(1)).thenReturn(List.of(expiredWaiting, okWaiting));
        doNothing().when(service).update(eq(10), any(UpdatePaymentHistoryDTO.class));

        service.verifyExpiredPayments(1);

        verify(service).update(eq(10), argThat(dto -> dto.paymentStatus() == PaymentStatus.EXPIRED));
        verify(service, never()).update(eq(11), any());
    }

    @Test
    void shouldCreatePaymentHistory() {
        CreatePaymentHistoryDTO dto = new CreatePaymentHistoryDTO(
                "pay_1",
                "http://url",
                PaymentStatus.WAITING_PAYMENT,
                new BigDecimal("10.00"),
                PlanType.YEARLY_IN_CASH
        );

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        service.create(1, dto);

        verify(paymentHistoryRepository).save(any(PaymentHistory.class));
    }

    @Test
    void shouldThrowWhenCreatingPaymentHistoryWithInvalidUser() {
        CreatePaymentHistoryDTO dto = new CreatePaymentHistoryDTO(
                "pay_1",
                "http://url",
                PaymentStatus.WAITING_PAYMENT,
                new BigDecimal("10.00"),
                PlanType.YEARLY_IN_CASH
        );

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.create(1, dto));

        verify(paymentHistoryRepository, never()).save(any());
    }
}
