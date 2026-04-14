package com.academo.service.user;

import com.academo.controller.dtos.mail.ActivateAccountMailDTO;
import com.academo.controller.dtos.mail.ResetPasswordMailDTO;
import com.academo.controller.dtos.mail.WelcomeMailDTO;
import com.academo.controller.dtos.paymentHistory.PaymentHistoryDTO;
import com.academo.controller.dtos.security.ForgotPasswordDTO;
import com.academo.controller.dtos.security.ResetPasswordDTO;
import com.academo.controller.dtos.user.UserDTO;
import com.academo.model.Profile;
import com.academo.model.User;
import com.academo.model.enums.payment.PaymentStatus;
import com.academo.model.enums.user.PlanType;
import com.academo.model.enums.user.UserRole;
import com.academo.repository.UserRepository;
import com.academo.controller.dtos.security.RegisterDTO;
import com.academo.security.service.TokenService;
import com.academo.service.payment.history.IPaymentHistoryService;
import com.academo.util.exceptions.user.AlreadyActivatedUserException;
import com.academo.util.exceptions.user.ExistingUserException;
import com.academo.util.exceptions.user.UserNotFoundException;
import com.academo.service.mail.IMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class UserServiceImpl implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final LocalDateTime EXPIRATION_ACTIVATION_TOKEN = LocalDateTime.now().plusMinutes(30).atOffset(ZoneOffset.of("-03:00")).toLocalDateTime();

    private final UserRepository userRepository;
    private final IMailService mailService;
    private final TokenService tokenService;
    private final IPaymentHistoryService paymentHistoryService;

    public UserServiceImpl(UserRepository userRepository, IMailService mailService, TokenService tokenService, IPaymentHistoryService paymentHistoryService) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.tokenService = tokenService;
        this.paymentHistoryService = paymentHistoryService;
    }

    @Override
    public void createUser(RegisterDTO registerDTO) {
        if(userRepository.findByEmail(registerDTO.email()).isPresent()) throw new ExistingUserException();
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());
        User user = new User();
        user.setEmail(registerDTO.email());
        user.setPassword(encryptedPassword);
        user.setName(registerDTO.name());
        user.setPlanType(PlanType.FREE);
        user.setRole(UserRole.ROLE_FREE);
        user.setActivationAccountTokenExpiration(EXPIRATION_ACTIVATION_TOKEN);
        Profile profile = new Profile();
        profile.setUser(user);
        user.setProfile(profile);
        User createdUser = userRepository.save(user);
        var token = tokenService.generateActivationToken(createdUser.getId());
        mailService.sendActivationMail(new ActivateAccountMailDTO(createdUser.getName(), createdUser.getEmail(), token));
    }

    @Override
    public UserDTO activateUser(String token) {
        logger.debug("[DEBUG] Token: {}", token);
        Integer userId = Integer.parseInt(tokenService.validateActivationToken(token));
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(!user.getAccountActivated()) {
            user.setAccountActivated(Boolean.TRUE);
            user.setActivationAccountTokenExpiration(LocalDateTime.now());
            mailService.sendWelcomeMail(new WelcomeMailDTO(user.getName(), user.getEmail()));
            return UserDTO.fromUser(userRepository.save(user));
        } else {
            throw new AlreadyActivatedUserException("Usuário já ativado na plataforma!");
        }
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User login(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        paymentHistoryService.verifyExpiredPayments(user.getId());
        User updatedUser = verifyPlan(user);
        if(updatedUser != null) return updatedUser;
        return user;
    }



    @Override
    public UserDTO update(User user) {
        return UserDTO.fromUser(userRepository.save(user));
    }

    @Override
    public void forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        User user = userRepository.findByEmail(forgotPasswordDTO.email()).orElseThrow(UserNotFoundException::new);
        var token = tokenService.generateForgotPasswordToken(user.getId());
        mailService.sendResetPasswordMail(new ResetPasswordMailDTO(user.getName(), user.getEmail(), token));
    }

    @Override
    public void resetPassword(String token, ResetPasswordDTO resetPasswordDTO) {
        Integer userId = Integer.parseInt(tokenService.validateForgotPasswordToken(token));
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        user.setPassword(new BCryptPasswordEncoder().encode(resetPasswordDTO.newPassword()));
        userRepository.save(user);
    }

    private User verifyPlan(User user) {
        if(user.getRole() == UserRole.ROLE_PREMIUM) {
            PaymentHistoryDTO paymentHistoryDTO = paymentHistoryService.findLastPayment(user.getId());
            /*
            Aqui, é feita a verificação se a data de vencimento já passou.
            Isso aconteceu, pois, o usuário pode ter escolhido o cancelar o plano, mas o vencimento do plano ainda não chegou
             */
            if(paymentHistoryDTO.planDueDate().isBefore(LocalDate.now())) {
                user.setRole(UserRole.ROLE_FREE);
                user.setPlanType(PlanType.FREE);
                update(user);
                return user;
            }
        }
        return null;
    }




}
