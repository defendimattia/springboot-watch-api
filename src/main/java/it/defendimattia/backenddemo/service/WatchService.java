package it.defendimattia.backenddemo.service;

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
import it.defendimattia.backenddemo.dto.WatchSearchDTO;
import it.defendimattia.backenddemo.dto.WatchUpdateDTO;
import it.defendimattia.backenddemo.mapper.WatchMapper;
import it.defendimattia.backenddemo.model.Watch;
import it.defendimattia.backenddemo.repository.WatchRepository;
import it.defendimattia.backenddemo.specification.WatchSearchSpecification;
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
         * Retrieves a watch by ID or throws a 404 exception if not found.
         *
         * @param id the watch identifier
         * @return the existing watch entity
         * @throws ResponseStatusException if the watch does not exist
         */
        private Watch findWatchOrThrow(Integer id) {

                return watchRepo.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Watch not found with id: " + id));
        }

        /**
         * Retrieves all watches.
         *
         * @return a paginated of watch data as {@link WatchListDTO}
         */
        public PaginatedResponse<WatchListDTO> getAllWatches(Pageable pageable) {

                Pageable safePageable = PageableValidator.sanitize(
                                pageable,
                                List.of("id", "brand", "model", "price"));

                Page<Watch> page = watchRepo.findAll(safePageable);

                List<WatchListDTO> content = page.getContent()
                                .stream()
                                .map(WatchMapper::toListDTO)
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

                Watch watch = findWatchOrThrow(id);

                return WatchMapper.toDTO(watch);
        }

        /**
         * Searches for watches matching the provided filter criteria.
         *
         * <p>
         * Filtering parameters are encapsulated inside {@link WatchSearchDTO}.
         * Pagination and sorting are handled through {@link Pageable}.
         * </p>
         *
         * @param filters  the search filters
         * @param pageable pagination and sorting information
         * @return a paginated list of matching watches
         */
        public PaginatedResponse<WatchListDTO> search(
                        WatchSearchDTO filters,
                        Pageable pageable) {

                Pageable safePageable = PageableValidator.sanitize(
                                pageable,
                                List.of("id", "brand", "model", "price"));

                Specification<Watch> spec = WatchSearchSpecification.build(filters);

                Page<Watch> page = watchRepo.findAll(spec, safePageable);

                List<WatchListDTO> content = page.getContent()
                                .stream()
                                .map(WatchMapper::toListDTO)
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

                Watch existing = findWatchOrThrow(dto.id());

                WatchMapper.updateEntity(dto, existing);
                watchRepo.save(existing);

                logger.info("Updated watch with ID: {}", existing.getId());

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
                Watch existing = findWatchOrThrow(id);

                watchRepo.delete(existing);

                logger.info("Deleted watch with ID: {}, brand '{}' and model '{}'", existing.getId(),
                                existing.getBrand(),
                                existing.getModel());
        }
}
