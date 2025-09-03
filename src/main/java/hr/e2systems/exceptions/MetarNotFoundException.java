package hr.e2systems.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MetarNotFoundException extends RuntimeException {
    public MetarNotFoundException(String icao) {
        super("No METAR found for airport: " + icao);
    }
}
