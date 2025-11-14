package com.buccodev.banking_service.dtos.account;

import com.buccodev.banking_service.entities.PixType;
import jakarta.validation.constraints.NotNull;

public record UpdatePixDto(@NotNull(message = "the pix type cannot be null!") PixType pixType) {
}
