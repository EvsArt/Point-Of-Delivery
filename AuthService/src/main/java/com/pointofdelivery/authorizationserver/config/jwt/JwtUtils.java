package com.pointofdelivery.authorizationserver.config.jwt;

import com.pointofdelivery.authorizationserver.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.expirationMs}")
    private long jwtExpirationMs;

    public String generateToken(Authentication authentication){
        User userPrincipal = (User) authentication.getPrincipal();
        Date curDate = new Date();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(curDate)
                .setExpiration(new Date(curDate.getTime() + jwtExpirationMs))
                .signWith(getKey(jwtSecret), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String jwt){
        try {
            log.info("VALIDATING TOKEN {}", jwt);
            Jwts.parser().setSigningKey(getKey(jwtSecret)).parseClaimsJws(jwt);
            log.info("TOKEN IS VALID");
            return true;
        } catch (MalformedJwtException | IllegalArgumentException e){
            log.error(e.getMessage());
        }

        return false;
    }

    public String getUsernameFromJwtToken(String jwt){
        return Jwts.parser().setSigningKey(getKey(jwtSecret)).parseClaimsJws(jwt).getBody().getSubject();
    }

    private Key getKey(String jwtSecret){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
