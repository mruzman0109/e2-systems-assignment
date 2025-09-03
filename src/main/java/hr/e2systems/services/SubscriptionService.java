package hr.e2systems.services;

import hr.e2systems.dto.request.SubscriptionUpdateRequest;
import hr.e2systems.dto.response.SubscriptionResponse;
import hr.e2systems.entity.Subscription;
import hr.e2systems.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional //dodajem ovo zbog deletea da nebi puklo negdi
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public Subscription subscribe(String icao) {
        Subscription subscription = subscriptionRepository.findById(icao.toUpperCase())
                .orElse(null);
        if (subscription != null) {
            return subscription;
        }
        subscription = Subscription.builder()
                .icaoCode(icao)
                .createdAt(Instant.now())
                .build();
        subscriptionRepository.save(subscription);
        return subscription;
    }

    public void unsubscribe(String icao) {
        subscriptionRepository.deleteById(icao.toUpperCase());
    }


    public List<Subscription> list() {
        return subscriptionRepository.findAll();
    }

    public SubscriptionResponse updateActiveStatus(String upperCase, SubscriptionUpdateRequest active) {
        boolean activeBool = Boolean.parseBoolean(String.valueOf(active.getActive().equals("1")));
        Subscription subscription = subscriptionRepository.findById(upperCase)
                .orElseThrow(() -> new IllegalArgumentException("Subscription with ICAO code " + upperCase + " not found."));
        subscription.setActive(activeBool);
        subscriptionRepository.save(subscription);
        return SubscriptionResponse.builder()
                .icaoCode(subscription.getIcaoCode())
                .active(subscription.isActive())
                .build();
    }
}
