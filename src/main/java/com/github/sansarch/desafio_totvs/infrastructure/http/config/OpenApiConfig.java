package com.github.sansarch.desafio_totvs.infrastructure.http.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Sans-arch",
                        url = "https://github.com/Sans-arch"
                ),
                description = "OpenAPI documentation for Desafio Totvs",
                title = "Desafio Totvs API",
                version = "1.0.0"
        ),
        servers = {
                @Server(
                        description = "Local Environment",
                        url = "http://localhost:8080"
                )
        }
)
public class OpenApiConfig {


}
