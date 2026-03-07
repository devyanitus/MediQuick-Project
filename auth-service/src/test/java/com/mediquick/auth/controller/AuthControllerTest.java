package com.mediquick.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediquick.auth.entity.User;
import com.mediquick.auth.repository.UserRepository;
import com.mediquick.auth.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @DisplayName("Register should return bad request when user already exists")
    void register_shouldReturnBadRequest_whenUserAlreadyExists() throws Exception {
        User user = new User("john@example.com", "password123");

        when(userRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(user));

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User already registered"));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Register should save user and return success when user does not exist")
    void register_shouldSaveUserAndReturnSuccess() throws Exception {
        User user = new User("john@example.com", "password123");

        when(userRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Login should return bad request when user not found")
    void login_shouldReturnBadRequest_whenUserNotFound() throws Exception {
        String requestBody =
                "{\n" +
                        "  \"email\": \"john@example.com\",\n" +
                        "  \"password\": \"password123\"\n" +
                        "}";

        when(userRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User not found"));

        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Login should return bad request when password is invalid")
    void login_shouldReturnBadRequest_whenPasswordInvalid() throws Exception {
        User user = new User("john@example.com", "encodedPassword");

        String requestBody =
                "{\n" +
                        "  \"email\": \"john@example.com\",\n" +
                        "  \"password\": \"wrongPassword\"\n" +
                        "}";

        when(userRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword"))
                .thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid password"));

        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Login should return token when credentials are valid")
    void login_shouldReturnToken_whenCredentialsValid() throws Exception {
        User user = new User("john@example.com", "encodedPassword");

        String requestBody =
                "{\n" +
                        "  \"email\": \"john@example.com\",\n" +
                        "  \"password\": \"password123\"\n" +
                        "}";

        when(userRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword"))
                .thenReturn(true);
        when(jwtService.generateToken("john@example.com"))
                .thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));

        verify(jwtService).generateToken("john@example.com");
    }
}