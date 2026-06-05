package it.defendimattia.backenddemo.auth.dto;

public record LoginRequest(
        String username,
        String password) {
}
