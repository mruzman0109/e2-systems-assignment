package hr.e2systems.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request DTO for METAR data")
public class MetarRequest {
    @NotBlank
    private String data; //čisti podaci
}
