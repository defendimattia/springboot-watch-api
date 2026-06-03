package it.defendimattia.backenddemo.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.defendimattia.backenddemo.auth.dto.RegisterRequest;
import it.defendimattia.backenddemo.auth.dto.RegisterResponse;
import it.defendimattia.backenddemo.security.model.User;
import it.defendimattia.backenddemo.security.model.Role;
import it.defendimattia.backenddemo.security.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}