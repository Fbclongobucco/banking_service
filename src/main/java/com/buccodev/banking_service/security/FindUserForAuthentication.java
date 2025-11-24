package com.buccodev.banking_service.security;

import com.buccodev.banking_service.exceptions.customer.ResourceNotFoundException;
import com.buccodev.banking_service.repositories.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class FindUserForAuthentication {

    private final CustomerRepository customerRepository;

    public FindUserForAuthentication(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public UserDetails findUserForAuthentication(String login) {
        return customerRepository.findByEmail(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
