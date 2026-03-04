//package com.mediquick.auth.controller;
//
//import com.mediquick.auth.entity.User;
//import com.mediquick.auth.repository.UserRepository;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final UserRepository userRepository;
//
//    public AuthController(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @PostMapping("/register")
//    public String register(@RequestBody User user) {
//
//        userRepository.save(user);
//        return "User registered successfully";
//    }
//}

package com.mediquick.auth.controller;

import com.mediquick.auth.dto.LoginRequest;
import com.mediquick.auth.entity.User;
import com.mediquick.auth.repository.UserRepository;
import com.mediquick.auth.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtService.generateToken(user.getEmail());
    }
}