package hr.e2systems.dto.response;

import hr.e2systems.entity.Metar;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Utility class for decoding METAR data")
public class MetarDecode {
    public static String decode(Metar metar) {
        return String.format("At %s, wind is %s, temperature %s, visibility %s",
                metar.getObservedAt(),
                metar.getWind(),
                metar.getTemperature(),
                metar.getVisibility());
    }
}
