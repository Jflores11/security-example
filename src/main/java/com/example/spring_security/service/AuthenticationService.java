package com.example.spring_security.service;

import com.example.spring_security.dto.AuthenticationRequest;
import com.example.spring_security.dto.AuthenticationResponse;
import com.example.spring_security.entity.User;
import com.example.spring_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private @Autowired UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public AuthenticationResponse login( AuthenticationRequest authRequest ) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword() );

        System.out.println( List.of( authenticationToken ) );
        authenticationManager.authenticate( authenticationToken );
        System.out.println("Esto debe funcionar");
        User user = userRepository.findByUsername( authRequest.getUsername() ).get();
        System.out.println("Esto NOOO debe funcionar");

        String jwt = jwtService.generateToken( user, generateExtraClaims( user ) );
        return new AuthenticationResponse( jwt );
    }

    private Map<String, Object> generateExtraClaims( User user ) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put( "name", user.getName() );
        extraClaims.put( "role", user.getRole().name() );
        extraClaims.put( "permissions", user.getAuthorities() );
        return extraClaims;
    }
}
