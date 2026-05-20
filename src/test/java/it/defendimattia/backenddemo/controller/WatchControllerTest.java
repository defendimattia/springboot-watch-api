package it.defendimattia.backenddemo.controller;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.mockito.Mockito;

import it.defendimattia.backenddemo.dto.PaginatedResponse;
import it.defendimattia.backenddemo.dto.WatchDetailsDTO;
import it.defendimattia.backenddemo.dto.WatchListDTO;
import it.defendimattia.backenddemo.service.WatchService;

@WebMvcTest(WatchRestController.class)
class WatchControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private WatchService watchService;

        @Test
        void shouldReturnWatchById() throws Exception {

                // Arrange
                WatchDetailsDTO dto = new WatchDetailsDTO(
                                1,
                                "Rolex",
                                "Submariner",
                                "Steel",
                                "Oystersteel",
                                "Automatic",
                                (short) 300,
                                BigDecimal.valueOf(41.0),
                                BigDecimal.valueOf(12.5),
                                BigDecimal.valueOf(20),
                                "Black",
                                "Sapphire",
                                "Date",
                                (short) 70,
                                10000);

                Mockito.when(watchService.getWatchById(1))
                                .thenReturn(dto);

                // Act + Assert
                mockMvc.perform(get("/api/watches/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.brand").value("Rolex"))
                                .andExpect(jsonPath("$.model").value("Submariner"));
        }

        @Test
        void shouldReturn404WhenWatchNotFound() throws Exception {

                // Arrange
                Mockito.when(watchService.getWatchById(999))
                                .thenThrow(new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Watch not found with id: 999"));

                // Act + Assert
                mockMvc.perform(get("/api/watches/999"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.message").value("Watch not found with id: 999"))
                                .andExpect(jsonPath("$.path").value("/api/watches/999"));
        }

        @Test
        void shouldCreateWatch() throws Exception {

                WatchDetailsDTO response = new WatchDetailsDTO(
                                1,
                                "Rolex",
                                "Submariner",
                                "Steel",
                                "Oystersteel",
                                "Automatic",
                                (short) 300,
                                BigDecimal.valueOf(41.0),
                                BigDecimal.valueOf(12.5),
                                BigDecimal.valueOf(20),
                                "Black",
                                "Sapphire",
                                "Date",
                                (short) 70,
                                10000);

                Mockito.when(watchService.addWatch(Mockito.any()))
                                .thenReturn(response);

                mockMvc.perform(post("/api/watches")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                    "brand": "Rolex",
                                                    "model": "Submariner",
                                                    "caseMaterial": "Steel",
                                                    "strapMaterial": "Oystersteel",
                                                    "movementType": "Automatic",
                                                    "waterResistance": 300,
                                                    "caseDiameter": 41.0,
                                                    "caseThickness": 12.5,
                                                    "bandWidth": 20,
                                                    "dialColor": "Black",
                                                    "crystalMaterial": "Sapphire",
                                                    "complications": "Date",
                                                    "powerReserve": 70,
                                                    "price": 10000
                                                }
                                                """))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.brand").value("Rolex"));
        }

        @Test
        void shouldDeleteWatch() throws Exception {

                Mockito.doNothing().when(watchService).deleteWatch(1);

                mockMvc.perform(delete("/api/watches/1"))
                                .andExpect(status().isNoContent());

                Mockito.verify(watchService).deleteWatch(1);
                verifyNoMoreInteractions(watchService);
        }

        @Test
        void shouldUpdateWatchPartial() throws Exception {

                // Arrange
                WatchDetailsDTO response = new WatchDetailsDTO(
                                1,
                                "Omega",
                                "Seamaster",
                                "Steel",
                                "Oystersteel",
                                "Automatic",
                                (short) 300,
                                BigDecimal.valueOf(41.0),
                                BigDecimal.valueOf(12.5),
                                BigDecimal.valueOf(20),
                                "Black",
                                "Sapphire",
                                "Date",
                                (short) 70,
                                12000);

                Mockito.when(watchService.updateWatch(Mockito.any()))
                                .thenReturn(response);

                // Act + Assert
                mockMvc.perform(patch("/api/watches")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                    "id": 1,
                                                    "brand": "Omega",
                                                    "model": "Seamaster",
                                                    "price": 12000
                                                }
                                                """))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.brand").value("Omega"))
                                .andExpect(jsonPath("$.model").value("Seamaster"))
                                .andExpect(jsonPath("$.price").value(12000));
        }

        @Test
        void shouldReturn400AndValidationErrors() throws Exception {

                mockMvc.perform(post("/api/watches")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                {
                                                    "brand": "",
                                                    "model": "",
                                                    "waterResistance": -10,
                                                    "price": -100
                                                }
                                                """))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.message").value("Validation failed"))
                                .andExpect(jsonPath("$.path").value("/api/watches"))
                                .andExpect(jsonPath("$.method").value("POST"))
                                .andExpect(jsonPath("$.errors.waterResistance")
                                                .value("must be greater than or equal to 0"))
                                .andExpect(jsonPath("$.errors.price")
                                                .value("must be greater than 0"))
                                .andExpect(jsonPath("$.errors.brand").exists())
                                .andExpect(jsonPath("$.errors.model").exists());
        }

        @Test
        void shouldSearchWatches() throws Exception {

                // Arrange
                WatchListDTO watch = new WatchListDTO(
                                1,
                                "Rolex",
                                "Submariner",
                                10000);

                PaginatedResponse<WatchListDTO> response = new PaginatedResponse<>(
                                java.util.List.of(watch),
                                0,
                                10,
                                1,
                                1);

                Mockito.when(watchService.search(
                                Mockito.any(),
                                Mockito.any()))
                                .thenReturn(response);

                // Act + Assert
                mockMvc.perform(get("/api/watches/search")
                                .param("brand", "Rolex")
                                .param("page", "0")
                                .param("size", "10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id").value(1))
                                .andExpect(jsonPath("$.content[0].brand").value("Rolex"))
                                .andExpect(jsonPath("$.content[0].model").value("Submariner"))
                                .andExpect(jsonPath("$.content[0].price").value(10000))
                                .andExpect(jsonPath("$.page").value(0))
                                .andExpect(jsonPath("$.size").value(10))
                                .andExpect(jsonPath("$.totalElements").value(1))
                                .andExpect(jsonPath("$.totalPages").value(1));
        }
}