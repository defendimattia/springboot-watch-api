package it.defendimattia.backenddemo.dto;

import java.math.BigDecimal;

public class WatchDetailsDTO {

    private final Integer id;
    private final String brand;
    private final String model;
    private final String caseMaterial;
    private final String strapMaterial;
    private final String movementType;
    private final Short waterResistance;
    private final BigDecimal caseDiameter;
    private final BigDecimal caseThickness;
    private final BigDecimal bandWidth;
    private final String dialColor;
    private final String crystalMaterial;
    private final String complications;
    private final Short powerReserve;
    private final Integer price;

    public WatchDetailsDTO(
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

        this.id = id;
        this.brand = brand;
        this.model = model;
        this.caseMaterial = caseMaterial;
        this.strapMaterial = strapMaterial;
        this.movementType = movementType;
        this.waterResistance = waterResistance;
        this.caseDiameter = caseDiameter;
        this.caseThickness = caseThickness;
        this.bandWidth = bandWidth;
        this.dialColor = dialColor;
        this.crystalMaterial = crystalMaterial;
        this.complications = complications;
        this.powerReserve = powerReserve;
        this.price = price;
    }

    public Integer getId() {
        return this.id;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getModel() {
        return this.model;
    }

    public String getCaseMaterial() {
        return this.caseMaterial;
    }

    public String getStrapMaterial() {
        return this.strapMaterial;
    }

    public String getMovementType() {
        return this.movementType;
    }

    public Short getWaterResistance() {
        return this.waterResistance;
    }

    public BigDecimal getCaseDiameter() {
        return this.caseDiameter;
    }

    public BigDecimal getCaseThickness() {
        return this.caseThickness;
    }

    public BigDecimal getBandWidth() {
        return this.bandWidth;
    }

    public String getDialColor() {
        return this.dialColor;
    }

    public String getCrystalMaterial() {
        return this.crystalMaterial;
    }

    public String getComplications() {
        return this.complications;
    }

    public Short getPowerReserve() {
        return this.powerReserve;
    }

    public Integer getPrice() {
        return this.price;
    }

}