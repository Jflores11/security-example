package com.example.spring_security.config.security.filter;

import com.example.spring_security.entity.User;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private UserRepository userRepository;

    private JwtService jwtService;

    @Autowired
    public JwtAuthenticationFilter( UserRepository userRepository, JwtService jwtService ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Obtener el header Authorization
        String authHeader = request.getHeader( "Authorization" );

        if ( Objects.isNull( authHeader ) || !authHeader.startsWith( "Bearer " ) ) {
            filterChain.doFilter( request, response );
            return;
        }

        // Obtener el JWT desde el header (Bearer -> [0], JWT -> [1])
        String jwt = authHeader.split( " " )[1];

        // Obtener el subject desde el JWT
        String subject = jwtService.extractSubject( jwt );

        // Settear objeto Authentication dentro del SecurityContext
        User user = userRepository.findByUsername( subject ).get();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                subject, null, user.getAuthorities() );
        SecurityContextHolder.getContext().setAuthentication( authToken );

        // Ejecutar el resto de filtros
        filterChain.doFilter( request, response );
    }

}
