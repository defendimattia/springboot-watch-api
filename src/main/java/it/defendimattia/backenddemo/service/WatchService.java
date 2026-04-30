package it.defendimattia.backenddemo.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import it.defendimattia.backenddemo.dto.WatchCreateDTO;
import it.defendimattia.backenddemo.dto.WatchDetailsDTO;
import it.defendimattia.backenddemo.dto.WatchListDTO;
import it.defendimattia.backenddemo.dto.WatchUpdateDTO;
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
    private WatchDetailsDTO mapToDTO(Watch watch) {
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

    /**
     * Retrieves all watches.
     *
     * @return a list of watch data as {@link WatchDetailsDTO}
     */
    public List<WatchListDTO> getAllWatches() {
        return watchRepo.findAll()
                .stream()
                .map(w -> new WatchListDTO(
                        w.getId(),
                        w.getBrand(),
                        w.getModel(),
                        w.getPrice()))
                .toList();
    }

    /**
     * Retrieves a single watch by its unique identifier (ID).
     *
     * <p>
     * Returns the watch data as a {@link WatchDetailsDTO}, ensuring that
     * the internal entity model is not exposed directly to the client.
     * </p>
     *
     * @param id the identifier of the watch
     * @return the watch data as a {@link WatchDetailsDTO}
     * @throws ResponseStatusException if no watch with the given id exists (HTTP
     *                                 404)
     */
    public WatchDetailsDTO getWatchById(Integer id) {
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
     * @return the created watch as {@link WatchDetailsDTO}
     * @throws ResponseStatusException if a watch with the same id already exists
     *                                 (HTTP 409)
     */
    public WatchDetailsDTO addWatch(WatchCreateDTO dto) {

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

        if (watch.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Watch with id " + watch.getId() + " already exists");
        }

        Watch savedWatch = watchRepo.save(watch);

        logger.info("Added watch with ID: {}, brand '{}' and model '{}'", savedWatch.getId(), savedWatch.getBrand(),
                savedWatch.getModel());

        return mapToDTO(savedWatch);
    }

    /**
     * Updates an existing watch. Only non-null fields in the given
     * {@link WatchUpdateDTO}
     * will be applied.
     *
     * @param dto the DTO containing updated data
     * @return the updated {@link WatchDetailsDTO}
     * @throws ResponseStatusException if the watch ID is null (HTTP 400)
     *                                 or if the watch does not exist (HTTP 404)
     */
    public WatchDetailsDTO updateWatch(WatchUpdateDTO dto) {
        if (dto.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is required to update a watch");
        }

        Watch existing = watchRepo.findById(dto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Watch not found with id " + dto.getId()));

        boolean changed = false;

        if (dto.getBrand() != null) {
            existing.setBrand(dto.getBrand());
            changed = true;
        }

        if (dto.getModel() != null) {
            existing.setModel(dto.getModel());
            changed = true;
        }

        if (dto.getCaseMaterial() != null) {
            existing.setCaseMaterial(dto.getCaseMaterial());
            changed = true;
        }

        if (dto.getStrapMaterial() != null) {
            existing.setStrapMaterial(dto.getStrapMaterial());
            changed = true;
        }

        if (dto.getMovementType() != null) {
            existing.setMovementType(dto.getMovementType());
            changed = true;
        }

        if (dto.getWaterResistance() != null) {
            existing.setWaterResistance(dto.getWaterResistance());
            changed = true;
        }

        if (dto.getCaseDiameter() != null) {
            existing.setCaseDiameter(dto.getCaseDiameter());
            changed = true;
        }

        if (dto.getCaseThickness() != null) {
            existing.setCaseThickness(dto.getCaseThickness());
            changed = true;
        }

        if (dto.getBandWidth() != null) {
            existing.setBandWidth(dto.getBandWidth());
            changed = true;
        }

        if (dto.getDialColor() != null) {
            existing.setDialColor(dto.getDialColor());
            changed = true;
        }

        if (dto.getCrystalMaterial() != null) {
            existing.setCrystalMaterial(dto.getCrystalMaterial());
            changed = true;
        }

        if (dto.getComplications() != null) {
            existing.setComplications(dto.getComplications());
            changed = true;
        }

        if (dto.getPowerReserve() != null) {
            existing.setPowerReserve(dto.getPowerReserve());
            changed = true;
        }

        if (dto.getPrice() != null) {
            existing.setPrice(dto.getPrice());
            changed = true;
        }

        watchRepo.save(existing);

        if (changed) {
            logger.info("Updated watch ID: {}", dto.getId());
        } else {
            logger.info("Update called but no changes applied for watch ID: {}", dto.getId());
        }

        return mapToDTO(existing);
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
