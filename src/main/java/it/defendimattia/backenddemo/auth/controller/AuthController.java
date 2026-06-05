package it.defendimattia.backenddemo.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.defendimattia.backenddemo.auth.service.AuthService;
import it.defendimattia.backenddemo.auth.dto.LoginRequest;
import it.defendimattia.backenddemo.auth.dto.LoginResponse;
import it.defendimattia.backenddemo.auth.dto.RegisterRequest;
import it.defendimattia.backenddemo.auth.dto.RegisterResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody RegisterRequest request) {

        RegisterResponse response = authService.register(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        String token = authService.login(request);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}