package hr.e2systems.api;

import hr.e2systems.dto.request.IcaoRequest;
import hr.e2systems.dto.request.SubscriptionUpdateRequest;
import hr.e2systems.dto.response.SubscriptionResponse;
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

    @GetMapping("/active")
    public List<Subscription> getOnlyActiveSubscriptions() {
        return service.getOnlyActiveSubscriptions();
    }

    @GetMapping("/{prefix}")
    public List<Subscription> getByPrefix(@PathVariable String prefix) {
        return service.getSubscriptionsByPrefix(prefix);
    }

    @GetMapping("/active/{prefix}")
    public List<Subscription> getActiveByPrefix(@PathVariable String prefix) {
        return service.getActiveSubscriptionsByPrefix(prefix);
    }



    @DeleteMapping("/{icao}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String icao) {
        service.unsubscribe(icao);
    }
    @PutMapping("/{icao}")
    public SubscriptionResponse updateSubscription(@PathVariable String icao,
                                                   @RequestBody SubscriptionUpdateRequest request) {
        return service.updateActiveStatus(icao.toUpperCase(), request);
    }
}