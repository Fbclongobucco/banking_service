package com.buccodev.banking_service.dtos.customer;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record CustomerRequestDto(@NotBlank(message = "the name cannot be empty!") String name,
                                 @Email(message = "the email is invalid!")
                                 @NotBlank(message = "the email cannot be empty!")
                                 String email,
                                 @Size(min = 11, max = 11, message = "Invalid CPF")
                                 String cpf,
                                 @NotBlank(message = "the password cannot be empty!")
                                 @Size(min = 6, message = "the password must have at least 6 characters")
                                 String password,
                                 @NotBlank(message = "the phone cannot be empty!")
                                 @NotBlank(message = "Phone is required")
                                 @Pattern(regexp = "^[0-9]+$", message = "Invalid phone")
                                 @Size(max = 15, message = "Invalid phone")
                                 String phone) {
}
