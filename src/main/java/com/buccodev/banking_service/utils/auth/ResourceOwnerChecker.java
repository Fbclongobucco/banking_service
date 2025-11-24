package com.buccodev.banking_service.utils.auth;

import com.buccodev.banking_service.entities.Customer;
import org.springframework.security.core.Authentication;

public class ResourceOwnerChecker {

    public static boolean verificationById(Long id, Authentication authentication) {
        var principal = authentication.getPrincipal();

        if (!(principal instanceof Customer customerAuthenticated)) {
            return true;
        }

        return !id.equals(customerAuthenticated.getId());
    }
}
