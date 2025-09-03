package hr.e2systems.unit;

import hr.e2systems.repository.MetarRepository;
import hr.e2systems.services.MetarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class MetarServicePrivateParsingTest {

    private MetarService metarService;
    private Method parseWindMethod;
    private Method parseTemperatureMethod;
    private Method parseVisibilityMethod;

    @BeforeEach
    void setup() throws Exception {
        MetarRepository metarRepository = Mockito.mock(MetarRepository.class);
        metarService = new MetarService(metarRepository);

        // Access private methods via reflection
        parseWindMethod = MetarService.class.getDeclaredMethod("parseWind", String.class);
        parseWindMethod.setAccessible(true);

        parseTemperatureMethod = MetarService.class.getDeclaredMethod("parseTemperature", String.class);
        parseTemperatureMethod.setAccessible(true);

        parseVisibilityMethod = MetarService.class.getDeclaredMethod("parseVisibility", String.class);
        parseVisibilityMethod.setAccessible(true);
    }

    private String invokeParseWind(String raw) throws Exception {
        return (String) parseWindMethod.invoke(metarService, raw);
    }

    private String invokeParseTemperature(String raw) throws Exception {
        return (String) parseTemperatureMethod.invoke(metarService, raw);
    }

    private String invokeParseVisibility(String raw) throws Exception {
        return (String) parseVisibilityMethod.invoke(metarService, raw);
    }

    @Test
    void parseWind_ShouldReturnCorrectWind() throws Exception {
        String raw = "METAR LDZA 121200Z 0902MPS 2000 OVC050 0/M01 Q1020";
        String wind = invokeParseWind(raw);
        assertThat(wind).isEqualTo("0902MPS");
    }

    @Test
    void parseWind_ShouldReturnNullIfNoMatch() throws Exception {
        String raw = "METAR LDZA 121200Z VRB MPS 2000 OVC050 0/M01 Q1020";
        String wind = invokeParseWind(raw);
        assertThat(wind).isNull();
    }

    @Test
    void parseTemperature_ShouldReturnCorrectTemperature() throws Exception {
        String raw = "METAR LDZA 121200Z 0902MPS 2000 OVC050 0/M01 Q1020";
        String temp = invokeParseTemperature(raw);
        assertThat(temp).isEqualTo("0/M01");
    }

    @Test
    void parseTemperature_ShouldReturnNullIfNoMatch() throws Exception {
        String raw = "METAR LDZA 121200Z 0902MPS 2000 OVC050 Q1020";
        String temp = invokeParseTemperature(raw);
        assertThat(temp).isNull();
    }

    @Test
    void parseVisibility_ShouldReturnCorrectVisibility() throws Exception {
        String raw = "METAR LDZA 121200Z 0902MPS 2000 OVC050 0/M01 Q1020";
        String visibility = invokeParseVisibility(raw);
        assertThat(visibility).isEqualTo("2000");
    }

    @Test
    void parseVisibility_ShouldReturnNullIfNoMatch() throws Exception {
        String raw = "METAR LDZA 121200Z 0902MPS OVC050 0/M01 Q1020";
        String visibility = invokeParseVisibility(raw);
        assertThat(visibility).isNull();
    }

    @Test
    void parseAllFieldsFromSingleMetar() throws Exception {
        String raw = "METAR LDZA 121200Z 0902MPS 2000 OVC050 0/M01 Q1020";

        String wind = invokeParseWind(raw);
        String temp = invokeParseTemperature(raw);
        String visibility = invokeParseVisibility(raw);

        assertThat(wind).isEqualTo("0902MPS");
        assertThat(temp).isEqualTo("0/M01");
        assertThat(visibility).isEqualTo("2000");
    }
}
