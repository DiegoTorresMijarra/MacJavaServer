package com.example.macjava.config.swagger;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
class SwaggerConfig {

    @Value("${api.version}")
    private String apiVersion;
    private final String linkRepositorio="https://github.com/DiegoTorresMijarra/MacJavaServer";

    // Añadimos la configuración de JWT
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    OpenAPI apiInfo() {
        return new OpenAPI()
                .externalDocs(new ExternalDocumentation()
                        .description("Repositorio")
                        .url(linkRepositorio))
                .info(new Info()
                        .title("API REST MacJavaServer Spring Boot DAW 2023/2024")
                        .version(apiVersion)
                        .description("API para uso de una cadena de restaurantes con Spring Boot 2023/2024")
                        .license(
                                new License()
                                        .name("CC BY-NC-SA 4.0")
                                        .url("https://creativecommons.org/licenses/by-nc-sa/4.0/deed.es")
                        )
                )
                .addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme())
                )
                ;
    }


    @Bean
    GroupedOpenApi httpApi() {
        return GroupedOpenApi.builder()
                .group("https")
                // Algunas rutas son JWT
                .pathsToMatch("/positions/**")
                .pathsToMatch("/workers/**")
                .pathsToMatch("/products/**")
                .pathsToMatch("/orders/**")
                .pathsToMatch("/restaurant/**")
                .pathsToMatch("/clientes/**")
                .pathsToMatch("/storage/**")
                .displayName("API MacJavaServer Spring Boot DAW 2023/2024")
                .build();
    }
}