package org.example.mediashop.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.mediashop.Configuration.Security.JwtUtils;
import org.example.mediashop.Data.DTO.RegisterUserDTO;
import org.example.mediashop.Data.Entity.ERole;
import org.example.mediashop.Data.Entity.User;
import org.example.mediashop.Data.Entity.UserDetails;
import org.example.mediashop.Data.Entity.UserDetailsImpl;
import org.example.mediashop.Payload.Request.AuthRequest;
import org.example.mediashop.Payload.Response.AuthResponse;
import org.example.mediashop.Repository.UserDetailsRepository;
import org.example.mediashop.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserDetailsRepository userDetailsRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    public Boolean checkIfUserExists(Long id) {
        return userRepository.existsById(id);
    }

    @Transactional
    public User signup(RegisterUserDTO registerUserDTO) {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(registerUserDTO.getFirstName());
        userDetails.setLastName(registerUserDTO.getLastName());

        UserDetails savedUserDetails = userDetailsRepository.save(userDetails);

        User user = new User();
        user.setUsername(registerUserDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        user.setEmail(registerUserDTO.getEmail());
        user.setRole(ERole.USER.getRoleNameWithPrefix());
        user.setUserDetails(savedUserDetails);

        return userRepository.save(user);
    }

    public AuthResponse authenticate(AuthRequest authRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new AuthResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }
}
