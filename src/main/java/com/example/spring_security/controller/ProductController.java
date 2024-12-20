package com.example.spring_security.controller;

import com.example.spring_security.entity.Product;
import com.example.spring_security.repository.ProductRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PreAuthorize( "hasAuthority('READ_ALL_PRODUCTS')" )
    @GetMapping
    @Operation(
      summary = "Get Products",
      description = "Método para traer los Productos guardados.",
      tags = {"Product Methods"},
      responses = {
        @ApiResponse(
          responseCode = "200",
          description = "Productos listados con éxito.",
          content = {
            @Content(
              mediaType = "application/json",
              schema = @Schema( implementation = Product.class )
            )
          }
        ),
        @ApiResponse( responseCode = "400", description = "Bad Request.", content = @Content() ),
        @ApiResponse( responseCode = "500", description = "Internal Error.", content = @Content() )
      }
    )
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productRepository.findAll();

        if( products != null && !products.isEmpty() )
        {
            return ResponseEntity.ok( products );
        }

        return ResponseEntity.notFound().build();
    }

    @PreAuthorize( "hasAuthority('SAVE_ONE_PRODUCT')" )
    @PostMapping
    @Operation(
      summary = "Save Product",
      description = "Método para guardar un Producto.",
      tags = {"Product Methods"},
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Nombre y precio son requeridos",
        required = true,
        content = @Content(
          mediaType = "application/json",
          schema = @Schema( implementation = Product.class )
        )
      ),
      responses = {
        @ApiResponse(
          responseCode = "201",
          description = "Producto guardado con éxito.",
          content = {
            @Content(
              mediaType = "application/json",
              schema = @Schema( implementation = Product.class )
            )
          }
        ),
        @ApiResponse( responseCode = "400", description = "Petición incorrecta.", content = @Content() ),
        @ApiResponse( responseCode = "500", description = "Error interno del servidor.", content = @Content() )
      }
    )
    public ResponseEntity<Product> createOne(@RequestBody @Valid Product product) {
        return ResponseEntity.status( HttpStatus.CREATED ).body( productRepository.save( product ) );
    }

    @ExceptionHandler( Exception.class )
    public ResponseEntity<Map<String, String>> handleGenericException(Exception exception, HttpServletRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put( "localizedMessage", exception.getLocalizedMessage() );
        error.put( "message", exception.getMessage() );
        error.put( "timestamp", new Date().toString() );
        error.put( "url", request.getRequestURL().toString() );
        error.put( "http-method", request.getMethod() );

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if ( exception instanceof AccessDeniedException ) {
            httpStatus = HttpStatus.FORBIDDEN;
        }
        if ( exception instanceof ExpiredJwtException) {
            httpStatus = HttpStatus.NETWORK_AUTHENTICATION_REQUIRED;
        }

        return ResponseEntity.status( httpStatus ).body( error );
    }

}
