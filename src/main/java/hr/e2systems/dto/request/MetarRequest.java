package hr.e2systems.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MetarRequest {
    @NotBlank
    private String data; //čisti podaci
}
