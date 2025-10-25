package com.generation.blogpessoal.configuration;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springBlogPessoalOpenAPI(){
        final String securitySchemeName = "jwt_auth";

        SecurityScheme jwtScheme = new SecurityScheme()
            .name(securitySchemeName)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");
            
        return new OpenAPI()
            .info(new Info()
                .title("Projeto Blog Pessoal")
                .description("Projeto Blog Pessoal - Cintia Dourado")
                .version("v0.0.1")
                .license(new License()
                    .name("Cintia Dourado")
                    .url("https://github.com/cdouradom"))
                .contact(new Contact()
                    .name("Cintia Dourado")
                    .url("https://github.com/cdouradom")
                    .email("cdouradom@gmail.com")))

            .externalDocs(new ExternalDocumentation()
                .description("Github")
                .url("https://github.com/cdouradom/Blog-Pessoal/blob/main/README.md"))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName, jwtScheme))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
        
    @Bean
	OpenApiCustomizer customerGlobalHeaderOpenApiCustomiser() {

		return openApi -> {
			openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {

				ApiResponses apiResponses = operation.getResponses();

				apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
				apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido!"));
				apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído!"));
				apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição!"));
				apiResponses.addApiResponse("401", createApiResponse("Acesso Não Autorizado!"));
				apiResponses.addApiResponse("403", createApiResponse("Acesso Proibido!"));
				apiResponses.addApiResponse("404", createApiResponse("Objeto Não Encontrado!"));
				apiResponses.addApiResponse("500", createApiResponse("Erro na Aplicação!"));

			}));
		};
	}

	private ApiResponse createApiResponse(String message) {

		return new ApiResponse().description(message);

	}
	
	private SecurityScheme createSecurityScheme() {
	    return new SecurityScheme()
	        .name("jwt_auth")
	        .type(SecurityScheme.Type.HTTP)
	        .scheme("bearer")
	        .bearerFormat("JWT")
	        .description("Insira apenas o token JWT (a palavra 'Bearer' será adicionada automaticamente)");
	}
}
