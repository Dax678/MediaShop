package org.example.mediashop.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.mediashop.Data.DTO.RegisterUserDTO;
import org.example.mediashop.Data.Entity.User;
import org.example.mediashop.Payload.Request.AuthRequest;
import org.example.mediashop.Payload.Response.AuthResponse;
import org.example.mediashop.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final UserService authenticationService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUserDTO registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.status(HttpStatus.OK).body(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authenticationService.authenticate(authRequest);

        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    @GetMapping("/roleTest/guest")
    public ResponseEntity<String> roleTest() {
        return ResponseEntity.status(HttpStatus.OK).body("Guest Test Successfully");
    }

    @GetMapping("/roleTest/user")
    public ResponseEntity<String> userRoleTest() {
        return ResponseEntity.status(HttpStatus.OK).body("User Role Test Successful");
    }

    @GetMapping("/roleTest/admin")
    public ResponseEntity<String> adminRoleTest() {
        return ResponseEntity.status(HttpStatus.OK).body("Admin Role Test Successful");
    }
}



