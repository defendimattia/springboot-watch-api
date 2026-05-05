package it.defendimattia.backenddemo.mapper;

import it.defendimattia.backenddemo.dto.WatchCreateDTO;
import it.defendimattia.backenddemo.dto.WatchDetailsDTO;
import it.defendimattia.backenddemo.dto.WatchUpdateDTO;
import it.defendimattia.backenddemo.model.Watch;

public class WatchMapper {

    public static Watch toEntity(WatchCreateDTO dto) {
        Watch watch = new Watch();

        watch.setBrand(dto.brand());
        watch.setModel(dto.model());
        watch.setCaseMaterial(dto.caseMaterial());
        watch.setStrapMaterial(dto.strapMaterial());
        watch.setMovementType(dto.movementType());
        watch.setWaterResistance(dto.waterResistance());
        watch.setCaseDiameter(dto.caseDiameter());
        watch.setCaseThickness(dto.caseThickness());
        watch.setBandWidth(dto.bandWidth());
        watch.setDialColor(dto.dialColor());
        watch.setCrystalMaterial(dto.crystalMaterial());
        watch.setComplications(dto.complications());
        watch.setPowerReserve(dto.powerReserve());
        watch.setPrice(dto.price());

        return watch;
    }

    public static WatchDetailsDTO toDTO(Watch watch) {
        return new WatchDetailsDTO(
                watch.getId(),
                watch.getBrand(),
                watch.getModel(),
                watch.getCaseMaterial(),
                watch.getStrapMaterial(),
                watch.getMovementType(),
                watch.getWaterResistance(),
                watch.getCaseDiameter(),
                watch.getCaseThickness(),
                watch.getBandWidth(),
                watch.getDialColor(),
                watch.getCrystalMaterial(),
                watch.getComplications(),
                watch.getPowerReserve(),
                watch.getPrice());
    }

    public static void updateEntity(WatchUpdateDTO dto, Watch watch) {
        if (dto.brand() != null)
            watch.setBrand(dto.brand());
        if (dto.model() != null)
            watch.setModel(dto.model());
        if (dto.caseMaterial() != null)
            watch.setCaseMaterial(dto.caseMaterial());
        if (dto.strapMaterial() != null)
            watch.setStrapMaterial(dto.strapMaterial());
        if (dto.movementType() != null)
            watch.setMovementType(dto.movementType());
        if (dto.waterResistance() != null)
            watch.setWaterResistance(dto.waterResistance());
        if (dto.caseDiameter() != null)
            watch.setCaseDiameter(dto.caseDiameter());
        if (dto.caseThickness() != null)
            watch.setCaseThickness(dto.caseThickness());
        if (dto.bandWidth() != null)
            watch.setBandWidth(dto.bandWidth());
        if (dto.dialColor() != null)
            watch.setDialColor(dto.dialColor());
        if (dto.crystalMaterial() != null)
            watch.setCrystalMaterial(dto.crystalMaterial());
        if (dto.complications() != null)
            watch.setComplications(dto.complications());
        if (dto.powerReserve() != null)
            watch.setPowerReserve(dto.powerReserve());
        if (dto.price() != null)
            watch.setPrice(dto.price());
    }
}
