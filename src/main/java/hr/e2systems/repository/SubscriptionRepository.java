package hr.e2systems.repository;

import hr.e2systems.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
    List<Subscription> findByActiveTrue();
    List<Subscription> findByIcaoCodeStartingWithIgnoreCase(String prefix);
    List<Subscription> findByActiveTrueAndIcaoCodeStartingWithIgnoreCase(String prefix);
}
