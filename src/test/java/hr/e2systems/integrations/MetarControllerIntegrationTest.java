package hr.e2systems.integrations;


import com.fasterxml.jackson.databind.ObjectMapper;
import hr.e2systems.dto.request.MetarRequest;
import hr.e2systems.entity.Metar;
import hr.e2systems.repository.MetarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MetarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MetarRepository metarRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        // Clean database before each test
        metarRepository.deleteAll();
    }

    @Test
    void ingestMetar_ShouldStoreMetarAndReturn201() throws Exception {
        // Given
        MetarRequest request = new MetarRequest();
        request.setData("METAR LDZA 121200Z 0902MPS OVC050 10/M01 Q1020");

        // When & Then
        mockMvc.perform(post("/airport/LDZA/METAR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.icaoCode").value("LDZA"))
                .andExpect(jsonPath("$.rawText").value("METAR LDZA 121200Z 0902MPS OVC050 10/M01 Q1020"));

        // Verify data is saved in the database
        assertThat(metarRepository.findAll()).hasSize(1);
        Metar savedMetar = metarRepository.findAll().get(0);
        assertThat(savedMetar.getIcaoCode()).isEqualTo("LDZA");
        assertThat(savedMetar.getRawText()).contains("METAR LDZA");
    }

    @Test
    void getMetar_ShouldReturnLatestMetar() throws Exception {
        // Given
        Metar metar = new Metar();
        metar.setIcaoCode("LDZA");
        metar.setRawText("METAR LDZA 121200Z 0902MPS OVC050 10/M01 Q1020");
        metar.setObservedAt(Instant.now());
        metar.setReceivedAt(Instant.now());
        metar.setWind("0902MPS");
        metar.setTemperature("10/M01");
        metar.setVisibility("OVC050");
        metarRepository.save(metar);

        // When & Then
        mockMvc.perform(get("/airport/LDZA/METAR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.icaoCode").value("LDZA"))
                .andExpect(jsonPath("$.wind").value("0902MPS"))
                .andExpect(jsonPath("$.temperature").value("10/M01"))
                .andExpect(jsonPath("$.visibility").value("OVC050"));
    }

    @Test
    void getMetar_ShouldReturn404_WhenNoMetarFound() throws Exception {
        mockMvc.perform(get("/airport/LDZA/METAR"))
                .andExpect(status().isNotFound());
    }


    @Test
    void getDecodedMetar_ShouldReturnDecodedString() throws Exception {
        // Given
        Metar metar = new Metar();
        metar.setIcaoCode("LDZA");
        metar.setRawText("METAR LDZA 121200Z 0902MPS OVC050 10/M01 Q1020");
        metar.setObservedAt(Instant.now());
        metar.setReceivedAt(Instant.now());
        metar.setWind("0902MPS");
        metar.setTemperature("10/M01");
        metar.setVisibility("OVC050");
        metarRepository.save(metar);

        // When & Then
        mockMvc.perform(get("/airport/LDZA/METAR/decoded"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("wind is 0902MPS")));
    }
}