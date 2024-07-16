package org.example.mediashop.Data.DTO;

import lombok.Value;

@Value
public class UserDTO {
    Long id;

    String username;

    String password;

    String email;

    String role;
}
