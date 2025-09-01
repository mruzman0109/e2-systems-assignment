package hr.e2systems.services;

import hr.e2systems.entity.Metar;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public class MetarService {
    public Metar saveMetar(String icao, @NotBlank String data) {
        return new Metar();
    }

    public Optional<Metar> getLatest(String icao) {
        return Optional.empty();
    }
}
