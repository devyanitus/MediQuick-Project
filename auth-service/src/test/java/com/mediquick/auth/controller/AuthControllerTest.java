package com.mediquick.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediquick.auth.dto.LoginRequest;
import com.mediquick.auth.dto.RegisterRequest;
import com.mediquick.auth.entity.User;
import com.mediquick.auth.repository.UserRepository;
import com.mediquick.auth.security.JwtAuthFilter;
import com.mediquick.auth.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtService jwtService;

    // ✅ important for security bypass
    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    private User testUser;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@gmail.com");
        testUser.setPassword("encodedPassword");
        testUser.setName("Test User");

        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@gmail.com");
        registerRequest.setPassword("password123");
        registerRequest.setName("Test User");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("password123");
    }

    // ================= REGISTER =================

    @Test
    @WithMockUser
    void register_Success() throws Exception {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(testUser);

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("User registered successfully"));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @WithMockUser
    void register_EmailAlreadyExists() throws Exception {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(testUser));

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("User already registered"));

        verify(userRepository, never()).save(any(User.class));
    }

    // ================= LOGIN =================

    @Test
    @WithMockUser
    void login_Success() throws Exception {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(testUser));

        when(passwordEncoder.matches("password123", "encodedPassword"))
                .thenReturn(true);

        when(jwtService.generateToken("test@gmail.com"))
                .thenReturn("mock.jwt.token");

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock.jwt.token"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.name").value("Test User"));

        verify(jwtService, times(1)).generateToken("test@gmail.com");
    }

    @Test
    @WithMockUser
    void login_UserNotFound() throws Exception {

        when(userRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());

        LoginRequest badRequest = new LoginRequest();
        badRequest.setEmail("unknown@gmail.com");
        badRequest.setPassword("password123");

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("User not found"));
    }

    @Test
    @WithMockUser
    void login_InvalidPassword() throws Exception {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(testUser));

        when(passwordEncoder.matches("wrongpassword", "encodedPassword"))
                .thenReturn(false);

        LoginRequest badRequest = new LoginRequest();
        badRequest.setEmail("test@gmail.com");
        badRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Invalid password"));
    }
}