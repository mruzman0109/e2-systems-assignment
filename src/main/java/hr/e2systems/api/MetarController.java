package hr.e2systems.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/airport/{icao}/METAR")
public class MetarController {
}
