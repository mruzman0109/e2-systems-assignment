package hr.e2systems.services;

import hr.e2systems.entity.Metar;
import hr.e2systems.repository.MetarRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MetarService {

    private final MetarRepository metarRepository;

    public Metar saveMetar(String icao, @NotBlank String data) {
        Instant now = Instant.now();
        Metar m = Metar.builder()
                .icaoCode(icao.toUpperCase())
                .rawText(data)
                .observedAt(now) // get out of rawText will get that soon
                .receivedAt(now)
                .build();

        return metarRepository.save(m);
    }

    public Optional<Metar> getLatest(String icao) {
        return metarRepository.findTopByIcaoCodeOrderByReceivedAtDesc(icao.toUpperCase());
    }
}
