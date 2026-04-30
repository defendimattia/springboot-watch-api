package it.defendimattia.backenddemo.dto;

public record WatchListDTO(
        Integer id,
        String brand,
        String model,
        Integer price) {
}