package com.platzi.market.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

    private static final String KEY = "pl4tz1";

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                //Se añade el usuario
                .setSubject(userDetails.getUsername())
                //Se añade la fecha inicio
                .setIssuedAt(new Date())
                //Se añade la fecha de expiración, en este caso 10h (1000ms * 60s * 60m * 10h)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                //Se añade la encriptación
                .signWith(SignatureAlgorithm.HS256, KEY)
                //Se compacta para ser retornada
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private Claims getClaims(String token) {
        //Se añade la llave de la firma y cuando esta se verifique se obtienen los claims y finalmente el cuerpo.
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }
}
