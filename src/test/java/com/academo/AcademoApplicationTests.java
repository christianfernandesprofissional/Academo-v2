package com.academo;

import com.academo.service.storage.google.DriveService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class AcademoApplicationTests {

    @Test
    void contextLoads() {
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public DriveService driveService() {
            return Mockito.mock(DriveService.class);
        }

        @Bean
        public JavaMailSender javaMailSender() {
            return Mockito.mock(JavaMailSender.class);
        }
    }

}
