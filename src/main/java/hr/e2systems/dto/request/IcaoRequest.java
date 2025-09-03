package hr.e2systems.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Request DTO for ICAO code")
public class IcaoRequest {
    @NotBlank
    @Pattern(regexp = "^[A-Z]{4}$", message = "ICAO must be 4 uppercase letters")
    private String icaoCode;
}
