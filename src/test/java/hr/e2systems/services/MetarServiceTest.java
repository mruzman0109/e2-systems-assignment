package hr.e2systems.services;

import hr.e2systems.entity.Metar;
import hr.e2systems.repository.MetarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MetarServiceTest {

    @Mock
    private MetarRepository metarRepository;

    @InjectMocks
    private MetarService metarService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveMetar_ShouldStoreMetarCorrectly() {
        String icaoCode = "LDZA";
        String rawMetar = "METAR LDZA 121200Z 0902MPS 2000 OVC050 0/M01 Q1020=";

        when(metarRepository.save(any(Metar.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Metar result = metarService.saveMetar(icaoCode, rawMetar);

        assertThat(result.getIcaoCode()).isEqualTo("LDZA");
        assertThat(result.getRawText()).isEqualTo(rawMetar);
        assertThat(result.getObservedAt()).isNotNull();
        assertThat(result.getReceivedAt()).isNotNull();
        assertThat(result.getWind()).isEqualTo("0902MPS");
        assertThat(result.getTemperature()).isEqualTo("0/M01");
        assertThat(result.getVisibility()).isEqualTo("2000");

        verify(metarRepository, times(1)).save(any(Metar.class));
    }

    @Test
    void getLatestMetar_ShouldReturnMetar_WhenExists() {
        String icaoCode = "LDZA";
        Metar metar = new Metar();
        metar.setIcaoCode(icaoCode);
        metar.setObservedAt(Instant.now());

        when(metarRepository.findTopByIcaoCodeOrderByReceivedAtDesc(icaoCode)).thenReturn(Optional.of(metar));

        Optional<Metar> result = metarService.getLatest(icaoCode);

        assertThat(result).isPresent();
        assertThat(result.get().getIcaoCode()).isEqualTo(icaoCode);
    }

    @Test
    void getLatestMetar_ShouldReturnEmpty_WhenNotExists() {
        String icaoCode = "LDZA";

        when(metarRepository.findTopByIcaoCodeOrderByReceivedAtDesc(icaoCode)).thenReturn(Optional.empty());

        Optional<Metar> result = metarService.getLatest(icaoCode);

        assertThat(result).isEmpty();
    }
}
