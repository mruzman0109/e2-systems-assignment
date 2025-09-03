package hr.e2systems.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.e2systems.dto.request.IcaoRequest;
import hr.e2systems.dto.request.SubscriptionUpdateRequest;
import hr.e2systems.entity.Subscription;
import hr.e2systems.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SubscriptionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        subscriptionRepository.deleteAll();
    }

    @Test
    void createSubscription_ShouldReturn201AndPersist() throws Exception {
        // Given
        IcaoRequest request = new IcaoRequest();
        request.setIcaoCode("LDZA");

        // When & Then
        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.icaoCode").value("LDZA"))
                .andExpect(jsonPath("$.active").value(true));

        // Verify in DB
        assertThat(subscriptionRepository.findAll()).hasSize(1);
        Subscription saved = subscriptionRepository.findAll().get(0);
        assertThat(saved.getIcaoCode()).isEqualTo("LDZA");
        assertThat(saved.isActive()).isTrue();
    }

    @Test
    void listSubscriptions_ShouldReturnAll() throws Exception {
        // Given
        Subscription sub = new Subscription();
        sub.setIcaoCode("LDZA");
        sub.setCreatedAt(Instant.now());
        sub.setActive(true);
        subscriptionRepository.save(sub);

        // When & Then
        mockMvc.perform(get("/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].icaoCode").value("LDZA"));
    }

    @Test
    void getOnlyActiveSubscriptions_ShouldReturnOnlyActive() throws Exception {
        // Given
        Subscription active = new Subscription();
        active.setIcaoCode("LDZA");
        active.setCreatedAt(Instant.now());
        active.setActive(true);

        Subscription inactive = new Subscription();
        inactive.setIcaoCode("EGGW");
        inactive.setCreatedAt(Instant.now());
        inactive.setActive(false);

        subscriptionRepository.save(active);
        subscriptionRepository.save(inactive);

        // When & Then
        mockMvc.perform(get("/subscriptions/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].icaoCode").value("LDZA"));
    }

    @Test
    void getSubscriptionsByPrefix_ShouldReturnMatching() throws Exception {
        // Given
        Subscription sub1 = new Subscription();
        sub1.setIcaoCode("LDZA");
        sub1.setCreatedAt(Instant.now());
        sub1.setActive(true);

        Subscription sub2 = new Subscription();
        sub2.setIcaoCode("LDPL");
        sub2.setCreatedAt(Instant.now());
        sub2.setActive(true);

        Subscription sub3 = new Subscription();
        sub3.setIcaoCode("EGGW");
        sub3.setCreatedAt(Instant.now());
        sub3.setActive(true);

        subscriptionRepository.save(sub1);
        subscriptionRepository.save(sub2);
        subscriptionRepository.save(sub3);

        // When & Then
        mockMvc.perform(get("/subscriptions/LD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].icaoCode").value("LDZA"));
    }

    @Test
    void updateSubscription_ShouldChangeActiveStatus() throws Exception {
        // Given
        Subscription sub = new Subscription();
        sub.setIcaoCode("LDZA");
        sub.setCreatedAt(Instant.now());
        sub.setActive(true);
        subscriptionRepository.save(sub);

        SubscriptionUpdateRequest updateRequest = new SubscriptionUpdateRequest();
        updateRequest.setActive("false");

        // When & Then
        mockMvc.perform(put("/subscriptions/LDZA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.icaoCode").value("LDZA"))
                .andExpect(jsonPath("$.active").value(false));

        // Verify in DB
        Subscription updated = subscriptionRepository.findById("LDZA").orElseThrow();
        assertThat(updated.isActive()).isFalse();
    }

    @Test
    void deleteSubscription_ShouldRemoveFromDB() throws Exception {
        // Given
        Subscription sub = new Subscription();
        sub.setIcaoCode("LDZA");
        sub.setCreatedAt(Instant.now());
        sub.setActive(true);
        subscriptionRepository.save(sub);

        // When & Then
        mockMvc.perform(delete("/subscriptions/LDZA"))
                .andExpect(status().isNoContent());

        // Verify in DB
        assertThat(subscriptionRepository.findById("LDZA")).isEmpty();
    }
}
