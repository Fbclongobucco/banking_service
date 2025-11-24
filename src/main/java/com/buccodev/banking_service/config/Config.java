package com.buccodev.banking_service.config;


import com.buccodev.banking_service.entities.PixType;
import com.buccodev.banking_service.entities.Roles;
import com.buccodev.banking_service.services.AccountService;
import com.buccodev.banking_service.services.CustomerService;
import com.buccodev.banking_service.dtos.account.UpdatePixDto;
import com.buccodev.banking_service.dtos.customer.CustomerRequestDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Config implements CommandLineRunner {

    private final CustomerService customerService;


    public Config(CustomerService customerService) {
        this.customerService = customerService;

    }

    @Override
    public void run(String... args) throws Exception {



      customerService.createCustomer(new CustomerRequestDto("Fabrício Longo Bucco", "longobucco@gmail.com",
                "12345678345", "123456778", "21989389933"));
       customerService.createCustomer(new CustomerRequestDto("Everton Longo Bucco", "neneco@gmail.com",
                "12322255527", "123456778", "21989878278"));
       customerService.createCustomer(new CustomerRequestDto("Stefani Longo Bucco", "tete@gmail.com",
                "12277222282", "123456778", "21989334322"));

    }
}
