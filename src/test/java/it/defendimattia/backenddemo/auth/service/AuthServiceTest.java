package it.defendimattia.backenddemo.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.defendimattia.backenddemo.auth.dto.LoginRequest;
import it.defendimattia.backenddemo.auth.dto.RegisterRequest;
import it.defendimattia.backenddemo.auth.dto.RegisterResponse;
import it.defendimattia.backenddemo.security.jwt.JwtService;
import it.defendimattia.backenddemo.security.model.Role;
import it.defendimattia.backenddemo.security.model.User;
import it.defendimattia.backenddemo.security.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

        @Mock
        private UserRepository userRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private AuthenticationManager authenticationManager;

        @Mock
        private JwtService jwtService;

        @InjectMocks
        private AuthService authService;

        @Test
        void register_ShouldCreateUser_WhenUsernameDoesNotExist() {

                // Arrange
                RegisterRequest request = new RegisterRequest("prova", "password123");

                when(userRepository.findByUsername("prova"))
                                .thenReturn(Optional.empty());

                when(passwordEncoder.encode("password123"))
                                .thenReturn("encodedPassword");

                User savedUser = new User();
                savedUser.setId(1);
                savedUser.setUsername("prova");
                savedUser.setPassword("encodedPassword");
                savedUser.setRole(Role.USER);

                when(userRepository.save(any(User.class)))
                                .thenReturn(savedUser);

                // Act
                RegisterResponse response = authService.register(request);

                // Assert
                assertEquals("prova", response.username());
                assertEquals("USER", response.role());

                verify(userRepository).findByUsername("prova");
                verify(passwordEncoder).encode("password123");
                verify(userRepository).save(any(User.class));
        }

        @Test
        void register_ShouldThrowException_WhenUsernameAlreadyExists() {

                // Arrange
                RegisterRequest request = new RegisterRequest("prova", "password123");

                User existingUser = new User();
                existingUser.setUsername("prova");

                when(userRepository.findByUsername("prova"))
                                .thenReturn(Optional.of(existingUser));

                // Act + Assert
                RuntimeException exception = assertThrows(
                                RuntimeException.class,
                                () -> authService.register(request));

                assertEquals(
                                "Username already exists",
                                exception.getMessage());

                verify(userRepository).findByUsername("prova");

                verify(passwordEncoder, never()).encode(anyString());

                verify(userRepository, never()).save(any(User.class));
        }

        @Test
        void login_ShouldReturnToken_WhenCredentialsAreValid() {

                // Arrange
                LoginRequest request = new LoginRequest("prova", "password123");

                Authentication authentication = mock(Authentication.class);

                when(authenticationManager.authenticate(any(
                                UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(authentication);

                when(authentication.getName())
                                .thenReturn("prova");

                when(jwtService.generateToken("prova"))
                                .thenReturn("jwt-token");

                // Act
                String token = authService.login(request);

                // Assert
                assertEquals("jwt-token", token);

                verify(authenticationManager).authenticate(any(
                                UsernamePasswordAuthenticationToken.class));

                verify(jwtService).generateToken("prova");
        }

        @Test
        void login_ShouldThrowException_WhenCredentialsAreInvalid() {

                // Arrange
                LoginRequest request = new LoginRequest("prova", "wrongPassword");

                when(authenticationManager.authenticate(any(
                                UsernamePasswordAuthenticationToken.class)))
                                .thenThrow(new BadCredentialsException("Bad credentials"));

                // Act + Assert
                assertThrows(
                                BadCredentialsException.class,
                                () -> authService.login(request));

                verify(authenticationManager).authenticate(any(
                                UsernamePasswordAuthenticationToken.class));

                verify(jwtService, never()).generateToken(anyString());
        }
}
