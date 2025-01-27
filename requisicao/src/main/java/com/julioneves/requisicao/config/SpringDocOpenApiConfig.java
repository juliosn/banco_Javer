package com.julioneves.requisicao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("REST API - Banco JAVER")
                                .description("API para requisição de gerenciamento de conta")
                                .version("v1")
                                .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                                .contact(new Contact().name("Júlio Neves").email("juliodsneves@hotmail.com"))
                );
    }
}
