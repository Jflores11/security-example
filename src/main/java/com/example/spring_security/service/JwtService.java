package com.example.spring_security.service;

import com.example.spring_security.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import io.jsonwebtoken.security.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value( "${security.jwt.expiration-minutes}" )
    private long EXPIRATION_MINUTES;

    @Value( "${security.jwt.key}" )
    private String KEY;
    public String generateToken( User user, Map<String, Object> stringObjectMap ) {
        Date issuedAt = new Date( System.currentTimeMillis() );
        Date expiredAt = new Date( issuedAt.getTime() + ( EXPIRATION_MINUTES * 60 * 1000 ) );

        return Jwts.builder()
                .claims( stringObjectMap )
                .subject( user.getUsername() )
                .issuedAt( issuedAt )
                .expiration( expiredAt )
                .signWith( generateSecureKey() )
                .compact();
    }

    private Key generateSecureKey() {
        System.out.println( "Shhhh! Secret key: " + new String( Decoders.BASE64.decode( KEY ) ) );
        return Keys.hmacShaKeyFor( Decoders.BASE64.decode( KEY ) );
    }

    public String extractSubject( String jwt ) {
        return getClaims( jwt ).getSubject();
    }

    // Valida que el JWT tenga un formato válido, que no haya expirado y que la firma sea válida, que coincida
    // con la firma que genera el backend con generateSecureKey().
    private Claims getClaims( String jwt ) {
        return Jwts.parser().verifyWith( ( SecretKey ) generateSecureKey() )
                .build().parseSignedClaims( jwt ).getPayload();
    }
}
