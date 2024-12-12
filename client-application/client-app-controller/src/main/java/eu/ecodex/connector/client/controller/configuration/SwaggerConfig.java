package eu.ecodex.connector.client.controller.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto generated Swagger documentation configs.
 */
@Configuration
public class SwaggerConfig {
    @Bean
    GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                             .group("public-apis")
                             .pathsToMatch("/**")
                             .build();
    }

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info().title("Connector Application REST API").version("1.0"));
    }
}
