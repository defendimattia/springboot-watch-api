package it.defendimattia.backenddemo.auth.dto;

public record RegisterRequest(
        String username,
        String password) {
}