package com.example.spring_security.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.http.HttpHeaders;

// @Configuration
// Sólo usar @Configuration cuando se configura un @Bean
@OpenAPIDefinition(
  info = @io.swagger.v3.oas.annotations.info.Info(
    title = "Implementación Spring Security",
    description = "Ejemplo de implementación de Spring Security y Swagger.",
    termsOfService = "http://lgsus.com/terms-cond",
    version = "1.0.2",
    contact = @io.swagger.v3.oas.annotations.info.Contact(
      name = "Jesús F",
      url = "http://lgsus.com/contact",
      email = "contact@lgsus.com"
    ),
    license = @io.swagger.v3.oas.annotations.info.License(
      name = "Licencia por LGSus SA",
      url = "http://lgsus.com/licence"
    )
  ),
  servers = {
    @Server(
      description = "Local",
      url = "http://localhost:8081"
    ),
    @Server(
      description = "Develop",
      url = "http://10.0.0.1:8081"
    ),
    @Server(
      description = "QA",
      url = "http://10.0.0.5:8081"
    ),
    @Server(
      description = "Prod",
      url = "https://lgsus-private-api.com"
    )
  },
  security = @io.swagger.v3.oas.annotations.security.SecurityRequirement( name = "My Security Token" )
)
@io.swagger.v3.oas.annotations.security.SecurityScheme(
  name = "My Security Token",
  description = "Token de acceso para el uso de la API",
  type = SecuritySchemeType.HTTP,
  paramName = HttpHeaders.AUTHORIZATION,
  in = SecuritySchemeIn.HEADER,
  scheme = "bearer",
  bearerFormat = "JWT"
)
public class SwaggerConfig {

//    @Bean
    public OpenAPI startSwagger() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()))
                .info(
                        new Info()
                        .title( "AuthAPI" ).version( "1.0,1 SNAPSHOT" )
                        .contact( new Contact().name( "Jesús F" ).email( "jf@test.com" ) )
                        .license( new License().url( "http://lgsus.com" ).name( "LGSus" ) )
                        .termsOfService( "http://lgsus.com/terms" ).description( "Security Sample" )
                )
        ;
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type( SecurityScheme.Type.HTTP )
                .bearerFormat( "JWT" )
                .scheme( "bearer" );
    }

}
