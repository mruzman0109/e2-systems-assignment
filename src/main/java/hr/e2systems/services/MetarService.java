package hr.e2systems.services;

import hr.e2systems.entity.Metar;
import hr.e2systems.repository.MetarRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MetarService {

    private final MetarRepository metarRepository;

    private static final Pattern OBS_TIME = Pattern.compile("(\\d{6})Z");

    public Metar saveMetar(String icao, @NotBlank String data) {
        Instant now = Instant.now();
        Instant observedAt = parseObservedTime(data).orElse(now);
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

    private Optional<Instant> parseObservedTime(String metar) {
        Matcher matcher = OBS_TIME.matcher(metar);
        if (matcher.find()) {
            String group = matcher.group(1); // e.g. "011200"
            try {
                int day = Integer.parseInt(group.substring(0, 2));
                int hour = Integer.parseInt(group.substring(2, 4));
                int minute = Integer.parseInt(group.substring(4, 6));

                LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
                LocalDateTime obs = LocalDateTime.of(
                        now.getYear(), now.getMonth(), day, hour, minute);
                return Optional.of(obs.toInstant(ZoneOffset.UTC));
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
