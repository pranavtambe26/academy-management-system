package com.cricketacademy.academymanagementsystem.service;

import com.cricketacademy.academymanagementsystem.dto.RegisterRequest;
import com.cricketacademy.academymanagementsystem.dto.UserResponse;
import com.cricketacademy.academymanagementsystem.entity.Role;
import com.cricketacademy.academymanagementsystem.entity.User;
import com.cricketacademy.academymanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testcoach");
        registerRequest.setPassword("plainPassword123");
        registerRequest.setEmail("testcoach@cricketacademy.com");
        registerRequest.setRole(Role.COACH);
    }

    @Test
    void register_shouldSucceed_whenUsernameAndEmailAreUnique() {
        // Arrange
        when(userRepository.existsByUsername("testcoach")).thenReturn(false);
        when(userRepository.existsByEmail("testcoach@cricketacademy.com")).thenReturn(false);
        when(passwordEncoder.encode("plainPassword123")).thenReturn("hashedPassword");

        User savedUser = User.builder()
                .id(1L)
                .username("testcoach")
                .password("hashedPassword")
                .email("testcoach@cricketacademy.com")
                .role(Role.COACH)
                .build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserResponse response = userService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("testcoach", response.getUsername());
        assertEquals(Role.COACH, response.getRole());
        verify(passwordEncoder).encode("plainPassword123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_whenUsernameAlreadyExists() {
        // Arrange
        when(userRepository.existsByUsername("testcoach")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(registerRequest)
        );

        assertEquals("Username already taken", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByUsername("testcoach")).thenReturn(false);
        when(userRepository.existsByEmail("testcoach@cricketacademy.com")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }
}