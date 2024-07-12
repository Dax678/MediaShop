package org.example.mediashop.Service;

import lombok.AllArgsConstructor;
import org.example.mediashop.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public Boolean checkIfUserExists(Long id) {
        return userRepository.existsById(id);
    }
}
