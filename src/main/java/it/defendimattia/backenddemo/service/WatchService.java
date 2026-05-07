package it.defendimattia.backenddemo.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import it.defendimattia.backenddemo.dto.PaginatedResponse;
import it.defendimattia.backenddemo.dto.WatchCreateDTO;
import it.defendimattia.backenddemo.dto.WatchDetailsDTO;
import it.defendimattia.backenddemo.dto.WatchListDTO;
import it.defendimattia.backenddemo.dto.WatchUpdateDTO;
import it.defendimattia.backenddemo.mapper.WatchMapper;
import it.defendimattia.backenddemo.model.Watch;
import it.defendimattia.backenddemo.repository.WatchRepository;
import it.defendimattia.backenddemo.specification.WatchSpecification;
import it.defendimattia.backenddemo.validator.PageableValidator;

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

        private final WatchRepository watchRepo;
        private static final Logger logger = LoggerFactory.getLogger(WatchService.class);

        public WatchService(WatchRepository watchRepo) {
                this.watchRepo = watchRepo;
        }

        /**
         * Retrieves all watches.
         *
         * @return a list of watch data as {@link WatchDetailsDTO}
         */
        public PaginatedResponse<WatchListDTO> getAllWatches(Pageable pageable) {

                Pageable safePageable = PageableValidator.sanitize(
                                pageable,
                                List.of("id", "brand", "model", "price"));

                Page<Watch> page = watchRepo.findAll(safePageable);

                List<WatchListDTO> content = page.getContent()
                                .stream()
                                .map(w -> new WatchListDTO(
                                                w.getId(),
                                                w.getBrand(),
                                                w.getModel(),
                                                w.getPrice()))
                                .toList();

                return new PaginatedResponse<>(
                                content,
                                page.getNumber(),
                                page.getSize(),
                                page.getTotalElements(),
                                page.getTotalPages());
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
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Watch not found with id " + id));

                return WatchMapper.toDTO(watch);
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
         * @return a paginated list of matching watches
         */
        public PaginatedResponse<WatchListDTO> search(
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
                        Integer maxPrice,
                        Pageable pageable) {

                Specification<Watch> spec = Specification.where(WatchSpecification.brandContains(brand))
                                .and(WatchSpecification.modelContains(model))
                                .and(WatchSpecification.caseMaterialContains(caseMaterial))
                                .and(WatchSpecification.strapMaterialContains(strapMaterial))
                                .and(WatchSpecification.movementTypeContains(movementType))
                                .and(WatchSpecification.waterResistanceGreaterThanEqual(waterResistance))
                                .and(WatchSpecification.caseDiameterGreaterThanEqual(caseDiameter))
                                .and(WatchSpecification
                                                .caseDiameterLessThan(caseDiameter == null ? null
                                                                : caseDiameter.add(BigDecimal.ONE)))
                                .and(WatchSpecification.caseThicknessGreaterThanEqual(caseThickness))
                                .and(WatchSpecification.caseThicknessLessThanEqual(caseThickness))
                                .and(WatchSpecification.bandWidthGreaterThanEqual(bandWidth))
                                .and(WatchSpecification.bandWidthLessThanEqual(bandWidth))
                                .and(WatchSpecification.dialColorContains(dialColor))
                                .and(WatchSpecification.crystalMaterialContains(crystalMaterial))
                                .and(WatchSpecification.complicationsContains(complications))
                                .and(WatchSpecification.powerReserveGreaterThanEqual(powerReserve))
                                .and(WatchSpecification.priceLessThanEqual(maxPrice));

                Pageable safePageable = PageableValidator.sanitize(
                                pageable,
                                List.of("id", "brand", "model", "price"));

                Page<Watch> page = watchRepo.findAll(spec, safePageable);

                List<WatchListDTO> content = page.getContent()
                                .stream()
                                .map(w -> new WatchListDTO(w.getId(), w.getBrand(), w.getModel(), w.getPrice()))
                                .toList();

                return new PaginatedResponse<>(
                                content,
                                page.getNumber(),
                                page.getSize(),
                                page.getTotalElements(),
                                page.getTotalPages());
        }

        /**
         * Adds a new watch to the repository.
         *
         * @param dto the data used to create the watch
         * @return the created watch as {@link WatchDetailsDTO}
         */
        public WatchDetailsDTO addWatch(WatchCreateDTO dto) {

                Watch watch = WatchMapper.toEntity(dto);
                Watch savedWatch = watchRepo.save(watch);

                logger.info("Added watch with ID: {}, brand '{}' and model '{}'", savedWatch.getId(),
                                savedWatch.getBrand(),
                                savedWatch.getModel());

                return WatchMapper.toDTO(savedWatch);
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
                if (dto.id() == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is required to update a watch");
                }

                Watch existing = watchRepo.findById(dto.id())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Watch not found with id " + dto.id()));

                WatchMapper.updateEntity(dto, existing);
                watchRepo.save(existing);

                logger.info("Updated watch ID: {}", existing.getId());

                return WatchMapper.toDTO(existing);
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
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Watch not found with id " + id));

                watchRepo.delete(existing);

                logger.info("Deleted watch with ID: {}, brand '{}' and model '{}'", existing.getId(),
                                existing.getBrand(),
                                existing.getModel());
        }

}
