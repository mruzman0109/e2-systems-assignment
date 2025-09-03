package hr.e2systems.services;

import hr.e2systems.entity.Metar;
import hr.e2systems.repository.MetarRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MetarService {

    private final MetarRepository metarRepository;

    public Metar saveMetar(String icao, @NotBlank String data) {
        return new Metar();
    }

    public Optional<Metar> getLatest(String icao) {
        return Optional.empty();
    }
}
