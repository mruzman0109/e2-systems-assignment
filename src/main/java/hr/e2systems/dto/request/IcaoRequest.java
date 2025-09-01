package hr.e2systems.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class IcaoRequest {
    @NotBlank
    @Pattern(regexp = "^[A-Z]{4}$", message = "ICAO must be 4 uppercase letters")
    private String icaoCode;
}
