package hr.e2systems.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
@Schema(description = "Response DTO for METAR data")
public class MetarResponse {
    @Schema(description = "ICAO airport code", example = "LDZA")
    String icaoCode;

    @Schema(description = "Raw METAR string", example = "METAR LDZA 121200Z 09002MPS 2000 OVC050 0/M01 Q1020=")
    String rawText;

    @Schema(description = "Timestamp when the METAR was observed", example = "2025-09-03T12:00:00Z")
    Instant observedAt;

    @Schema(description = "Timestamp when the METAR was received", example = "2025-09-03T12:05:00Z")
    Instant receivedAt;

    @Schema(description = "Wind information", example = "09002MPS")
    String wind;

    @Schema(description = "Temperature", example = "0/M01")
    String temperature;

    @Schema(description = "Overall visibility", example = "2000")
    String visibility;
}
