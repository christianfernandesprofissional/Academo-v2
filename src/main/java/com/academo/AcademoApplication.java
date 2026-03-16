package com.academo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AcademoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademoApplication.class, args);
	}

}
