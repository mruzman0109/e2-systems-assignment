package hr.e2systems.services;

import hr.e2systems.entity.Subscription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionService {
    public Subscription subscribe(@NotBlank @Pattern(regexp = "^[A-Z]{4}$", message = "ICAO must be 4 uppercase letters") String icaoCode) {
        return new Subscription();
    }

    public void unsubscribe(String icao) {

    }

    public List<Subscription> list() {
        return new ArrayList<>();
    }
}
