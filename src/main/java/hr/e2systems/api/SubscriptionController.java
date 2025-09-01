package hr.e2systems.api;

import hr.e2systems.dto.request.IcaoRequest;
import hr.e2systems.entity.Subscription;
import hr.e2systems.services.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    @Autowired
    private SubscriptionService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Subscription create(@Valid @RequestBody IcaoRequest req) {
        return service.subscribe(req.getIcaoCode());
    }

    @GetMapping
    public List<Subscription> list() {
        return service.list();
    }

    @DeleteMapping("/{icao}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String icao) {
        service.unsubscribe(icao);
    }
}