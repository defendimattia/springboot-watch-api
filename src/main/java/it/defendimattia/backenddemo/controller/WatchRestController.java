package it.defendimattia.backenddemo.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.defendimattia.backenddemo.dto.PaginatedResponse;
import it.defendimattia.backenddemo.dto.WatchCreateDTO;
import it.defendimattia.backenddemo.dto.WatchDetailsDTO;
import it.defendimattia.backenddemo.dto.WatchListDTO;
import it.defendimattia.backenddemo.dto.WatchUpdateDTO;
import it.defendimattia.backenddemo.model.Watch;
import it.defendimattia.backenddemo.service.WatchService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * REST controller for managing luxury watches.
 *
 * <p>
 * Exposes endpoints for CRUD operations and advanced search.
 * Handles HTTP requests and responses, delegating business logic to
 * {@link WatchService}.
 * </p>
 */
@RestController
@RequestMapping("/api/watches")
public class WatchRestController {

    private final WatchService watchService;

    public WatchRestController(WatchService watchService) {
        this.watchService = watchService;
    }

    /**
     * Retrieves all watches.
     *
     * @return a list of watch data as {@link WatchDetailsDTO}
     * @response 200 OK if the request is successful
     */
    @GetMapping
    public PaginatedResponse<WatchListDTO> index(Pageable pageable) {
        return watchService.getAllWatches(pageable);
    }

    /**
     * Retrieves a single watch by its unique ID.
     *
     * @param id the identifier of the watch
     * @return the watch data as a {@link WatchDetailsDTO}
     * @response 200 OK if found
     * @response 404 NOT FOUND if no watch exists with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<WatchDetailsDTO> show(@PathVariable Integer id) {
        WatchDetailsDTO watch = watchService.getWatchById(id);
        return ResponseEntity.ok(watch);
    }

    /**
     * Searches for watches using optional filter criteria.
     *
     * <p>
     * All parameters are optional; if a parameter is null, it is ignored in the
     * search.
     * </p>
     *
     * @param brand           the brand name to filter by
     * @param model           the model name to filter by
     * @param caseMaterial    the case material
     * @param strapMaterial   the strap material
     * @param movementType    the movement type
     * @param waterResistance minimum water resistance in meters
     * @param caseDiameter    case diameter in millimeters
     * @param caseThickness   case thickness in millimeters
     * @param bandWidth       band width in millimeters
     * @param dialColor       dial color
     * @param crystalMaterial crystal material
     * @param complications   complications/features of the watch
     * @param powerReserve    minimum power reserve in hours
     * @param maxPrice        maximum price in USD
     * @return a list of {@link Watch} entities matching the criteria
     * @response 200 OK if the request is successful
     */
    @GetMapping("/search")
    public List<WatchListDTO> searchWatches(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String caseMaterial,
            @RequestParam(required = false) String strapMaterial,
            @RequestParam(required = false) String movementType,
            @RequestParam(required = false) Short waterResistance,
            @RequestParam(required = false) BigDecimal caseDiameter,
            @RequestParam(required = false) BigDecimal caseThickness,
            @RequestParam(required = false) BigDecimal bandWidth,
            @RequestParam(required = false) String dialColor,
            @RequestParam(required = false) String crystalMaterial,
            @RequestParam(required = false) String complications,
            @RequestParam(required = false) Short powerReserve,
            @RequestParam(required = false) Integer maxPrice) {

        return watchService.search(
                brand, model, caseMaterial, strapMaterial, movementType,
                waterResistance, caseDiameter, caseThickness, bandWidth,
                dialColor, crystalMaterial, complications, powerReserve, maxPrice);
    }

    /**
     * Creates a new watch.
     *
     * @param watch the watch data used to create a new resource
     * @return the created watch as {@link WatchDetailsDTO}
     * @response 201 CREATED if successfully created
     * @response 409 CONFLICT if a watch with the same ID already exists
     */
    @PostMapping
    public ResponseEntity<WatchDetailsDTO> createWatch(@Valid @RequestBody WatchCreateDTO watch) {

        WatchDetailsDTO savedWatch = watchService.addWatch(watch);

        return new ResponseEntity<>(savedWatch, HttpStatus.CREATED);
    }

    /**
     * Partially updates an existing watch.
     *
     * <p>
     * Only non-null fields in the request body are updated.
     * </p>
     *
     * @param dto the {@link WatchUpdateDTO} containing updated fields
     * @return the updated {@link WatchDetailsDTO}
     * @response 200 OK if successfully updated
     * @response 400 BAD REQUEST if the ID is missing
     * @response 404 NOT FOUND if the watch does not exist
     */
    @PatchMapping
    public ResponseEntity<WatchDetailsDTO> updateWatchPartial(@Valid @RequestBody WatchUpdateDTO dto) {

        WatchDetailsDTO updatedWatch = watchService.updateWatch(dto);

        return ResponseEntity.ok(updatedWatch);
    }

    /**
     * Deletes a watch by its ID.
     *
     * @param id the identifier of the watch to delete
     * @return 204 NO CONTENT if successfully deleted
     * @response 404 NOT FOUND if no watch exists with the given ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Watch> deleteWatch(@PathVariable Integer id) {

        watchService.deleteWatch(id);

        return ResponseEntity.noContent().build();
    }

}
