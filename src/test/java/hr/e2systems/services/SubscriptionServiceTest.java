package hr.e2systems.services;

import hr.e2systems.entity.Subscription;
import hr.e2systems.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void subscribe_ShouldCreateNewSubscription_WhenNotExists() {
        String icaoCode = "LDZA";

        when(subscriptionRepository.findById(icaoCode)).thenReturn(Optional.empty());
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Subscription result = subscriptionService.subscribe(icaoCode);

        assertThat(result.getIcaoCode()).isEqualTo("LDZA");
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.isActive()).isTrue();

        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void subscribe_ShouldReturnExistingSubscription_WhenAlreadyExists() {
        String icaoCode = "LDZA";
        Subscription existing = new Subscription();
        existing.setIcaoCode(icaoCode);
        existing.setCreatedAt(Instant.now());
        existing.setActive(true);

        when(subscriptionRepository.findById(icaoCode)).thenReturn(Optional.of(existing));

        Subscription result = subscriptionService.subscribe(icaoCode);

        assertThat(result).isEqualTo(existing);
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void getAllSubscriptions_ShouldReturnList() {
        Subscription sub1 = new Subscription();
        sub1.setIcaoCode("LDZA");
        sub1.setCreatedAt(Instant.now());
        sub1.setActive(true);

        when(subscriptionRepository.findByActiveTrue()).thenReturn(List.of(sub1));

        List<Subscription> result = subscriptionService.getOnlyActiveSubscriptions();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIcaoCode()).isEqualTo("LDZA");
    }

    @Test
    void unsubscribe_ShouldDeleteById() {
        String icaoCode = "LDZA";

        subscriptionService.unsubscribe(icaoCode);

        verify(subscriptionRepository, times(1)).deleteById("LDZA");
    }
}