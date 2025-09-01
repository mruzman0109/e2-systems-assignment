package hr.e2systems.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class MetarResponse {
    String icaoCode;
    String rawText;
    Instant observedAt;
    Instant receivedAt;
}
