package it.defendimattia.backenddemo.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import it.defendimattia.backenddemo.dto.WatchSearchDTO;
import it.defendimattia.backenddemo.model.Watch;

public final class WatchSearchSpecification {

    private WatchSearchSpecification() {
    }

    public static Specification<Watch> build(
            WatchSearchDTO filters) {

        return Specification.where(WatchSpecification.brandContains(filters.brand()))
                .and(WatchSpecification.modelContains(filters.model()))
                .and(WatchSpecification.caseMaterialContains(filters.caseMaterial()))
                .and(WatchSpecification.strapMaterialContains(filters.strapMaterial()))
                .and(WatchSpecification.movementTypeContains(filters.movementType()))
                .and(WatchSpecification.waterResistanceGreaterThanEqual(filters.waterResistance()))
                .and(WatchSpecification.caseDiameterGreaterThanEqual(filters.caseDiameter()))
                .and(WatchSpecification.caseDiameterLessThan(
                        filters.caseDiameter() == null
                                ? null
                                : filters.caseDiameter().add(BigDecimal.ONE)))
                .and(WatchSpecification.caseThicknessGreaterThanEqual(filters.caseThickness()))
                .and(WatchSpecification.caseThicknessLessThanEqual(filters.caseThickness()))
                .and(WatchSpecification.bandWidthGreaterThanEqual(filters.bandWidth()))
                .and(WatchSpecification.bandWidthLessThanEqual(filters.bandWidth()))
                .and(WatchSpecification.dialColorContains(filters.dialColor()))
                .and(WatchSpecification.crystalMaterialContains(filters.crystalMaterial()))
                .and(WatchSpecification.complicationsContains(filters.complications()))
                .and(WatchSpecification.powerReserveGreaterThanEqual(filters.powerReserve()))
                .and(WatchSpecification.priceLessThanEqual(filters.maxPrice()));
    }
}
