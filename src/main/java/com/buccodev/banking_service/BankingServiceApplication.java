package com.buccodev.banking_service;

import com.buccodev.banking_service.repositories.CustomerRepository;
import com.buccodev.banking_service.services.CustomerService;
import com.buccodev.banking_service.utils.dto.customer.CustomerRequestDto;
import com.buccodev.banking_service.utils.num_generate.AccountNumGenerator;
import com.buccodev.banking_service.utils.num_generate.CardNumGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankingServiceApplication {


    public static void main(String[] args) {
		SpringApplication.run(BankingServiceApplication.class, args);
	}
}
