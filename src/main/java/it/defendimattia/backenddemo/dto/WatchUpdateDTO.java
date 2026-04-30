package it.defendimattia.backenddemo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * DTO used for partially updating a Watch.
 * 
 * <p>
 * All fields are optional; only non-null values will be applied.
 * Includes validation rules for incoming API requests.
 * </p>
 */
public record WatchUpdateDTO(
        Integer id,

        @Size(max = 250, message = "brand must be max 250 characters long") String brand,

        @Size(max = 250, message = "model must be max 250 characters long") String model,

        @Size(max = 250, message = "case material must be max 250 characters long") String caseMaterial,

        @Size(max = 250, message = "strap material must be max 250 characters long") String strapMaterial,

        @Size(max = 250, message = "movement type must be max 250 characters long") String movementType,

        @PositiveOrZero Short waterResistance,

        @Positive(message = "case diameter must be greater than 0") @Digits(integer = 2, fraction = 1) BigDecimal caseDiameter,

        @Positive(message = "case thickness must be greater than 0") @Digits(integer = 2, fraction = 2) BigDecimal caseThickness,

        @Positive(message = "band width must be greater than 0") @Digits(integer = 2, fraction = 0) BigDecimal bandWidth,

        @Size(max = 250, message = "dial color must be max 250 characters long") String dialColor,

        @Size(max = 250, message = "crystal material must be max 250 characters long") String crystalMaterial,

        @Size(max = 400, message = "complications must be max 400 characters long") String complications,

        @Positive Short powerReserve,

        @Positive Integer price) {

}
