package com.example.spring_security.controller;

import com.example.spring_security.dto.AuthenticationRequest;
import com.example.spring_security.dto.AuthenticationResponse;
import com.example.spring_security.entity.Product;
import com.example.spring_security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    private String defaultLocale;

    @PreAuthorize( "permitAll" )
    @PostMapping("/authenticate")
    @Operation(
      summary = "Auth Method",
      description = "Método para obtener token de autenticación.",
      tags = { "Auth Methods" },
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Nombre de usuario y contraseña son requeridos.",
        required = true,
        content = @Content(
          mediaType = "application/json",
          schema = @Schema( implementation = AuthenticationRequest.class )
        )
      ),
      responses = {
        @ApiResponse(
          responseCode = "200",
          description = "Token generado con éxito.",
          content = {
            @Content(
              mediaType = "application/json",
              schema = @Schema( implementation = AuthenticationResponse.class )
            )
          }
        ),
        @ApiResponse( responseCode = "400", description = "Petición incorrecta.", content = @Content() ),
        @ApiResponse( responseCode = "500", description = "Error interno del servidor.", content = @Content() )
      }
    )
    public ResponseEntity<AuthenticationResponse> login( @RequestBody @Valid AuthenticationRequest authRequest ) {
        AuthenticationResponse jwtDTO = authenticationService.login( authRequest );
        return ResponseEntity.ok( jwtDTO );
    }

    @PreAuthorize( "permitAll" )
    @GetMapping("/public-access")
    @Operation(
      summary = "Public endpoint.",
      description = "Método para saber si el proyecto está corriendo.",
      tags = {"Public Methods"},
      responses = {
        @ApiResponse(
          responseCode = "200",
          description = "Project is alive."
        ),
        @ApiResponse( responseCode = "500", description = "Error interno del servidor.", content = @Content() )
      }
    )
    public String publicAccess( @Value("#{ systemProperties['user.region'] }") String defaultLocale ) {
      this.defaultLocale = defaultLocale;
      return "This is a public endpoint. DefaultLocale: " + this.defaultLocale;
    }

}
