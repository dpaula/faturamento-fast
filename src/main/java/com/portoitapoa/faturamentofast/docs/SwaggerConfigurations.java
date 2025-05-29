package com.portoitapoa.faturamentofast.docs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author Fernando de Lima on
 */
@Configuration
public class SwaggerConfigurations {

    @Value("${info.app.version}")
    private String appVersion;

    @Bean
    public OpenAPI customCofiguration() {
        return new OpenAPI()
            .info(new Info().title("Faturamento Fast API")
                .description("API para envio rápido e integrado de um novo Faturamento Online")
                .version(appVersion)
                .license(new License()
                    .name("")
                    .url("http://unlicense.org")))
            .externalDocs(new ExternalDocumentation()
                .description("Faturamento Fast API Documentação")
                .url("http://teste.portoitapoa.com.br/faturamento-fast/v1/suporte"))
            .components(new Components().addSecuritySchemes("bearer-jwt",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                    .in(SecurityScheme.In.HEADER).name("Authorization")))
            .addSecurityItem(
                new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")));
    }
}
