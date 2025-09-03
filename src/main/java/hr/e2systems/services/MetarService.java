package hr.e2systems.services;

import hr.e2systems.entity.Metar;
import hr.e2systems.repository.MetarRepository;
import jakarta.transaction.Transactional;
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
@Transactional
public class MetarService {

    private final MetarRepository metarRepository;

    private static final Pattern OBS_TIME = Pattern.compile("(\\d{6})Z");

    public Metar saveMetar(String icao, @NotBlank String data) {
        Instant now = Instant.now();
        Instant observedAt = parseObservedTime(data).orElse(now);
        Metar m = Metar.builder()
                .icaoCode(icao.toUpperCase())
                .rawText(data)
                .observedAt(observedAt) // get out of rawText will get that soon
                .receivedAt(now)
                .wind(parseWind(data))
                .temperature(parseTemperature(data))
                .visibility(parseVisibility(data))
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

    private String parseWind(String raw) {
        // Primjer: 09002MPS vjetar
        Pattern pattern = Pattern.compile("\\b(\\d{2}\\d{2}MPS)\\b");
        Matcher matcher = pattern.matcher(raw);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String parseTemperature(String raw) {
        // Primjer: 0/M01 temperatura
        Pattern pattern = Pattern.compile("\\b(\\d/M?-?\\d{2})\\b");
        Matcher matcher = pattern.matcher(raw);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String parseVisibility(String raw) {
        // Primjer: 2000 vidljivost
        Pattern pattern = Pattern.compile("\\b(\\d{4})\\b");
        Matcher matcher = pattern.matcher(raw);
        return matcher.find() ? matcher.group(1) : null;
    }
}
