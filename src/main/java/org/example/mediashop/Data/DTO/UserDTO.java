package org.example.mediashop.Data.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

@Value
public class UserDTO {
    @Positive(message = "Id must be greater than zero")
    Long id;

    @NotNull
    @NotBlank
    String username;

    @NotNull
    @NotBlank
    String password;

    @NotNull
    @NotBlank
    @Email
    String email;

    String role;
}
