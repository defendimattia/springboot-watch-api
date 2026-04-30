package it.defendimattia.backenddemo.dto;

import java.math.BigDecimal;

public record WatchDetailsDTO(
        Integer id,
        String brand,
        String model,
        String caseMaterial,
        String strapMaterial,
        String movementType,
        Short waterResistance,
        BigDecimal caseDiameter,
        BigDecimal caseThickness,
        BigDecimal bandWidth,
        String dialColor,
        String crystalMaterial,
        String complications,
        Short powerReserve,
        Integer price) {

}