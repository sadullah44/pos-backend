package com.example.pos_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // <--- BURASI: Spring'e "Arka planda iş yapabilirsin" diyoruz.
public class PosBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PosBackendApplication.class, args);
	}

}
