package hr.e2systems.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI metarOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("METAR Service API")
                        .description("REST API for subscribing airports and retrieving METAR data")
                        .version("2.0.0"));
    }
}
