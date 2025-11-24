package com.buccodev.banking_service.dtos.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record CustomerUpdateDto(@NotBlank(message = "the name cannot be empty!")
                                String name,
                                @NotBlank(message = "the phone cannot be empty!")
                                @Pattern(regexp = "^[0-9]+$", message = "Invalid phone")
                                @Size(max = 15, message = "Invalid phone")
                                String phone) {
}
