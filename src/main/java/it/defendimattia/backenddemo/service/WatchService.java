package it.defendimattia.backenddemo.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import it.defendimattia.backenddemo.dto.WatchCreateDTO;
import it.defendimattia.backenddemo.dto.WatchResponseDTO;
import it.defendimattia.backenddemo.model.Watch;
import it.defendimattia.backenddemo.repository.WatchRepository;
import it.defendimattia.backenddemo.specification.WatchSpecification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service layer for managing luxury watches.
 * 
 * <p>
 * Provides operations for CRUD (Create, Read, Update, Delete) and search
 * functionality.
 * Handles business logic and validation related to watches.
 * </p>
 */
@Service
public class WatchService {

    @Autowired
    private WatchRepository watchRepo;

    private static final Logger logger = LoggerFactory.getLogger(WatchService.class);

    /**
     * Maps a Watch entity to a WatchResponseDTO.
     *
     * <p>
     * This method is used internally to separate the persistence model
     * (entity) from the API response model (DTO), ensuring that the
     * database structure is not exposed directly to external clients.
     * </p>
     */
    private WatchResponseDTO mapToDTO(Watch watch) {
        return new WatchResponseDTO(
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

    /**
     * Retrieves all watches.
     *
     * @return a list of watch data as {@link WatchResponseDTO}
     */
    public List<WatchResponseDTO> getAllWatches() {
        return watchRepo.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    /**
     * Retrieves a single watch by its unique identifier (ID).
     *
     * <p>
     * Returns the watch data as a {@link WatchResponseDTO}, ensuring that
     * the internal entity model is not exposed directly to the client.
     * </p>
     *
     * @param id the identifier of the watch
     * @return the watch data as a {@link WatchResponseDTO}
     * @throws ResponseStatusException if no watch with the given id exists (HTTP
     *                                 404)
     */
    public WatchResponseDTO getWatchById(Integer id) {
        Watch watch = watchRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Watch not found with id " + id));

        return mapToDTO(watch);
    }

    /**
     * Searches for watches matching the provided criteria.
     *
     * <p>
     * Each parameter is optional; null values are ignored in the search.
     * </p>
     *
     * @param brand           the brand name to search for
     * @param model           the model name to search for
     * @param caseMaterial    the case material
     * @param strapMaterial   the strap material
     * @param movementType    the type of movement
     * @param waterResistance minimum water resistance in meters
     * @param caseDiameter    case diameter in millimeters
     * @param caseThickness   case thickness in millimeters
     * @param bandWidth       band width in millimeters
     * @param dialColor       dial color
     * @param crystalMaterial crystal material
     * @param complications   complications of the watch
     * @param powerReserve    minimum power reserve in hours
     * @param maxPrice        maximum price in USD
     * @return a list of {@link Watch} entities matching the criteria
     */
    public List<Watch> search(String brand,
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
            Integer maxPrice) {

        Specification<Watch> spec = Specification.where(WatchSpecification.brandContains(brand))
                .and(WatchSpecification.modelContains(model))
                .and(WatchSpecification.caseMaterialContains(caseMaterial))
                .and(WatchSpecification.strapMaterialContains(strapMaterial))
                .and(WatchSpecification.movementTypeContains(movementType))
                .and(WatchSpecification.waterResistanceGreaterThanEqual(waterResistance))
                .and(WatchSpecification.caseDiameterGreaterThanEqual(caseDiameter))
                .and(WatchSpecification
                        .caseDiameterLessThan(caseDiameter == null ? null : caseDiameter.add(BigDecimal.ONE)))
                .and(WatchSpecification.caseThicknessGreaterThanEqual(caseThickness))
                .and(WatchSpecification.caseThicknessLessThanEqual(caseThickness))
                .and(WatchSpecification.bandWidthGreaterThanEqual(bandWidth))
                .and(WatchSpecification.bandWidthLessThanEqual(bandWidth))
                .and(WatchSpecification.dialColorContains(dialColor))
                .and(WatchSpecification.crystalMaterialContains(crystalMaterial))
                .and(WatchSpecification.complicationsContains(complications))
                .and(WatchSpecification.powerReserveGreaterThanEqual(powerReserve))
                .and(WatchSpecification.priceLessThanEqual(maxPrice));

        return watchRepo.findAll(spec);
    }

    /**
     * Adds a new watch to the repository.
     *
     * @param dto the data used to create the watch
     * @return the created watch as {@link WatchResponseDTO}
     * @throws ResponseStatusException if a watch with the same id already exists (HTTP 409)
     */
    public WatchResponseDTO addWatch(WatchCreateDTO dto) {

        Watch watch = new Watch();

        watch.setBrand(dto.getBrand());
        watch.setModel(dto.getModel());
        watch.setCaseMaterial(dto.getCaseMaterial());
        watch.setStrapMaterial(dto.getStrapMaterial());
        watch.setMovementType(dto.getMovementType());
        watch.setWaterResistance(dto.getWaterResistance());
        watch.setCaseDiameter(dto.getCaseDiameter());
        watch.setCaseThickness(dto.getCaseThickness());
        watch.setBandWidth(dto.getBandWidth());
        watch.setDialColor(dto.getDialColor());
        watch.setCrystalMaterial(dto.getCrystalMaterial());
        watch.setComplications(dto.getComplications());
        watch.setPowerReserve(dto.getPowerReserve());
        watch.setPrice(dto.getPrice());

        if (watch.getId() != null && watchRepo.existsById(watch.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Watch with id " + watch.getId() + " already exists");
        }

        Watch savedWatch = watchRepo.save(watch);

        logger.info("Added watch with ID: {}, brand '{}' and model '{}'", savedWatch.getId(), savedWatch.getBrand(),
                savedWatch.getModel());

        return mapToDTO(savedWatch);
    }

    /**
     * Updates an existing watch. Only non-null fields in the given {@link Watch}
     * will be updated.
     *
     * @param watch the watch with updated data
     * @return the updated {@link Watch} entity
     * @throws ResponseStatusException if the watch ID is null (HTTP 400)
     *                                 or if the watch does not exist (HTTP 404)
     */
    public Watch updateWatch(Watch watch) {
        if (watch.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is required to update a watch");
        }

        Watch existing = watchRepo.findById(watch.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Watch not found with id " + watch.getId()));

        boolean changed = false;

        if (watch.getBrand() != null) {
            existing.setBrand(watch.getBrand());
            changed = true;
        }

        if (watch.getModel() != null) {
            existing.setModel(watch.getModel());
            changed = true;
        }

        if (watch.getCaseMaterial() != null) {
            existing.setCaseMaterial(watch.getCaseMaterial());
            changed = true;
        }

        if (watch.getStrapMaterial() != null) {
            existing.setStrapMaterial(watch.getStrapMaterial());
            changed = true;
        }

        if (watch.getMovementType() != null) {
            existing.setMovementType(watch.getMovementType());
            changed = true;
        }

        if (watch.getWaterResistance() != null) {
            existing.setWaterResistance(watch.getWaterResistance());
            changed = true;
        }

        if (watch.getCaseDiameter() != null) {
            existing.setCaseDiameter(watch.getCaseDiameter());
            changed = true;
        }

        if (watch.getCaseThickness() != null) {
            existing.setCaseThickness(watch.getCaseThickness());
            changed = true;
        }

        if (watch.getBandWidth() != null) {
            existing.setBandWidth(watch.getBandWidth());
            changed = true;
        }

        if (watch.getDialColor() != null) {
            existing.setDialColor(watch.getDialColor());
            changed = true;
        }

        if (watch.getCrystalMaterial() != null) {
            existing.setCrystalMaterial(watch.getCrystalMaterial());
            changed = true;
        }

        if (watch.getComplications() != null) {
            existing.setComplications(watch.getComplications());
            changed = true;
        }

        if (watch.getPowerReserve() != null) {
            existing.setPowerReserve(watch.getPowerReserve());
            changed = true;
        }

        if (watch.getPrice() != null) {
            existing.setPrice(watch.getPrice());
            changed = true;
        }

        Watch saved = watchRepo.save(existing);

        if (changed) {
            logger.info("Updated watch ID: {}", saved.getId());
        } else {
            logger.info("Update called but no changes applied for watch ID: {}", saved.getId());
        }

        return existing;
    }

    /**
     * Deletes a watch by its ID.
     *
     * @param id the identifier of the watch to delete
     * @throws ResponseStatusException if no watch with the given id exists (HTTP
     *                                 404)
     */
    public void deleteWatch(Integer id) {
        Watch existing = watchRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Watch not found with id " + id));

        watchRepo.delete(existing);

        logger.info("Deleted watch with ID: {}, brand '{}' and model '{}'", existing.getId(), existing.getBrand(),
                existing.getModel());
    }

}
