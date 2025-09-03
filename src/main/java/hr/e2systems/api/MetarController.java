package hr.e2systems.api;

import hr.e2systems.dto.request.MetarRequest;
import hr.e2systems.dto.response.MetarDecode;
import hr.e2systems.dto.response.MetarResponse;
import hr.e2systems.entity.Metar;
import hr.e2systems.exceptions.MetarNotFoundException;
import hr.e2systems.services.MetarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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



    /**Koristi se metoda niže - može se obrisati
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
   **/
    @GetMapping
    public MetarResponse getMetar(@PathVariable String icao,
                                        @RequestParam(required = false) List<String> fields) {
        Metar metar = metarService.getLatest(icao).orElse(null);
        if(metar == null) throw new MetarNotFoundException(icao);
        if (fields == null || fields.isEmpty()) return MetarResponse.builder()
                .icaoCode(metar.getIcaoCode())
                .rawText(metar.getRawText())
                .observedAt(metar.getObservedAt())
                .receivedAt(metar.getReceivedAt())
                .wind(metar.getWind())
                .temperature(metar.getTemperature())
                .visibility(metar.getVisibility())
                .build();
        MetarResponse.MetarResponseBuilder builder = MetarResponse.builder();

        if (fields.contains("icaoCode")) builder.icaoCode(metar.getIcaoCode());
        if (fields.contains("rawText")) builder.rawText(metar.getRawText());
        if (fields.contains("observedAt")) builder.observedAt(metar.getObservedAt());
        if (fields.contains("receivedAt")) builder.receivedAt(metar.getReceivedAt());
        if (fields.contains("wind")) builder.wind(metar.getWind());
        if (fields.contains("temperature")) builder.temperature(metar.getTemperature());
        if (fields.contains("visibility")) builder.visibility(metar.getVisibility());

        return builder.build();
    }

    @GetMapping("/decoded")
    public String getDecodedMetar(@PathVariable String icao) {
        Metar metar = metarService.getLatest(icao).orElse(null);
        if(metar == null) throw new MetarNotFoundException(icao);
        return MetarDecode.decode(metar);
    }
}
