package com.jobapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank @Size(min = 6) String newPassword,

        @NotBlank String userType) {}
