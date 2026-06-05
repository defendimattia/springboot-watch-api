package it.defendimattia.backenddemo.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.defendimattia.backenddemo.auth.dto.LoginRequest;
import it.defendimattia.backenddemo.auth.dto.RegisterRequest;
import it.defendimattia.backenddemo.auth.dto.RegisterResponse;
import it.defendimattia.backenddemo.security.model.User;
import it.defendimattia.backenddemo.security.jwt.JwtService;
import it.defendimattia.backenddemo.security.model.Role;
import it.defendimattia.backenddemo.security.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.username());

        user.setPassword(passwordEncoder.encode(request.password()));

        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getUsername(),
                savedUser.getRole().name());
    }

    public String login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()));

        String username = authentication.getName();

        String token = jwtService.generateToken(username);

        return token;
    }
}