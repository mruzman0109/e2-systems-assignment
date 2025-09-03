package hr.e2systems.dto.response;

import hr.e2systems.entity.Metar;

public class MetarDecode {
    public static String decode(Metar metar) {
        return String.format("At %s, wind is %s, temperature %s, visibility %s",
                metar.getObservedAt(),
                metar.getWind(),
                metar.getTemperature(),
                metar.getVisibility());
    }
}
