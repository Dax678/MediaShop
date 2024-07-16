package org.example.mediashop.Data.DTO.Mapper;

import org.example.mediashop.Data.DTO.UserDTO;
import org.example.mediashop.Data.Entity.User;

public class UserMapper {
    public static UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRole()
        );
    }

    public static User toUserEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());

        return user;
    }
}
