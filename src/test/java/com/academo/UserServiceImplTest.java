package com.academo;

import com.academo.controller.dtos.paymentHistory.PaymentHistoryDTO;
import com.academo.controller.dtos.security.ForgotPasswordDTO;
import com.academo.controller.dtos.security.RegisterDTO;
import com.academo.controller.dtos.security.ResetPasswordDTO;
import com.academo.controller.dtos.user.UserDTO;
import com.academo.model.User;
import com.academo.model.enums.user.PlanType;
import com.academo.model.enums.user.UserRole;
import com.academo.repository.UserRepository;
import com.academo.security.service.TokenService;
import com.academo.service.mail.IMailService;
import com.academo.service.payment.history.IPaymentHistoryService;
import com.academo.service.user.UserServiceImpl;
import com.academo.util.exceptions.user.AlreadyActivatedUserException;
import com.academo.util.exceptions.user.ExistingUserException;
import com.academo.util.exceptions.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private IMailService mailService;

    @Mock
    private TokenService tokenService;

    @Mock
    private IPaymentHistoryService paymentHistoryService;

    @InjectMocks
    private UserServiceImpl service;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1);
        user.setName("Fabio");
        user.setEmail("fabio@email.com");
        user.setPassword("hashed");
        user.setPlanType(PlanType.FREE);
        user.setRole(UserRole.ROLE_FREE);
        user.setAccountActivated(false);
    }

    @Test
    void shouldThrowWhenCreatingExistingUser() {
        RegisterDTO dto = new RegisterDTO("Fabio", "password123", "fabio@email.com");

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));

        assertThrows(ExistingUserException.class, () -> service.createUser(dto));

        verify(userRepository, never()).save(any());
        verifyNoInteractions(mailService);
    }

    @Test
    void shouldCreateUserAndSendActivationMail() {
        RegisterDTO dto = new RegisterDTO("Fabio", "password123", "fabio@email.com");

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1);
            return u;
        });
        when(tokenService.generateActivationToken(1)).thenReturn("activation-token");

        service.createUser(dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals(dto.email(), captor.getValue().getEmail());
        assertEquals(dto.name(), captor.getValue().getName());
        assertNotNull(captor.getValue().getProfile());
        verify(mailService).sendActivationMail(any());
    }

    @Test
    void shouldActivateUserAndSendWelcomeMail() {
        when(tokenService.validateActivationToken("token")).thenReturn("1");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserDTO result = service.activateUser("token");

        assertNotNull(result);
        verify(mailService).sendWelcomeMail(any());
        verify(userRepository).save(argThat(u -> u.getAccountActivated().equals(Boolean.TRUE)));
    }

    @Test
    void shouldThrowWhenActivatingAlreadyActivatedUser() {
        user.setAccountActivated(true);

        when(tokenService.validateActivationToken("token")).thenReturn("1");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        assertThrows(AlreadyActivatedUserException.class, () -> service.activateUser("token"));

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenFindByIdNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.findById(1));
    }

    @Test
    void shouldForgotPasswordAndSendResetPasswordMail() {
        ForgotPasswordDTO dto = new ForgotPasswordDTO("fabio@email.com");

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        when(tokenService.generateForgotPasswordToken(1)).thenReturn("reset-token");

        service.forgotPassword(dto);

        verify(mailService).sendResetPasswordMail(any());
    }

    @Test
    void shouldResetPassword() {
        ResetPasswordDTO dto = new ResetPasswordDTO("newPassword123", "newPassword123");

        when(tokenService.validateForgotPasswordToken("token")).thenReturn("1");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        service.resetPassword("token", dto);

        verify(userRepository).save(argThat(u -> u.getPassword() != null && !u.getPassword().equals(dto.newPassword())));
    }

    @Test
    void shouldLoginAndDowngradeExpiredPremiumPlan() {
        user.setRole(UserRole.ROLE_PREMIUM);
        user.setPlanType(PlanType.YEARLY_IN_CASH);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(paymentHistoryService.findLastPayment(1)).thenReturn(
                new PaymentHistoryDTO(1, "pay", null, null, LocalDate.now().minusDays(1), null, null)
        );
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = service.login(user.getEmail());

        assertNotNull(result);
        assertEquals(UserRole.ROLE_FREE, result.getRole());
        assertEquals(PlanType.FREE, result.getPlanType());
        verify(paymentHistoryService).verifyExpiredPayments(1);
    }
}
