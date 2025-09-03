package hr.e2systems.api;

import hr.e2systems.dto.request.MetarRequest;
import hr.e2systems.dto.response.MetarResponse;
import hr.e2systems.entity.Metar;
import hr.e2systems.exceptions.MetarNotFoundException;
import hr.e2systems.services.MetarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/airport/{icao}/METAR")
public class MetarController {
    @Autowired
    private MetarService metarService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MetarResponse ingest(@PathVariable String icao,
                                @Valid @RequestBody MetarRequest req) {
        var m = metarService.saveMetar(icao, req.getData());
        return MetarResponse.builder()
                .icaoCode(m.getIcaoCode())
                .rawText(m.getRawText())
                .observedAt(m.getObservedAt())
                .receivedAt(m.getReceivedAt())
                .build();
    }

    @GetMapping
    public MetarResponse latest(@PathVariable String icao) {
        Metar m = metarService.getLatest(icao)
                .orElseThrow(() -> new MetarNotFoundException(icao));
        return MetarResponse.builder()
                .icaoCode(m.getIcaoCode())
                .rawText(m.getRawText())
                .observedAt(m.getObservedAt())
                .receivedAt(m.getReceivedAt())
                .build();
        //return metarService.getLatest(icao).orElseThrow(() -> new NoSuchElementException("No METAR stored for " + icao));
    }
}
