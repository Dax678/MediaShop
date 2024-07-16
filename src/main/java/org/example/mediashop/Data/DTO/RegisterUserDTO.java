package org.example.mediashop.Data.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDTO {
    private String email;

    private String username;

    private String password;

    private String firstName;

    private String lastName;
}
