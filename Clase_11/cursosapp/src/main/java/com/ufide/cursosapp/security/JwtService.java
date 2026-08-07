package com.ufide.cursosapp.security;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;

@Component
public class JwtService {
    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expiration;

    private SecretKey key() {return Keys.hmacShaKeyFor(secret.getBytes());}

    public String generarToken(String username, String rol) { //estos se extraen con @Authenticationprincipal UserDetails o con Authentication auth
        Date ahora = new Date();
        Date expira = new Date(ahora.getTime() + expiration);

        return Jwts.builder().subject(username)
        .claim("rol", rol)
        .issuedAt(ahora)
        .expiration(expira)
        .signWith(key())
        .compact();
    }

    private <T> T extraerClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
        return resolver.apply(claims);
    }

    public String extraerUserName(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    public String extraerRol(String token) {
        return extraerClaim(token, claim -> claim.get("rol", String.class));
    }

    public boolean estaExpirado(String token) {
        return extraerClaim(token, Claims::getExpiration).before(new Date()); //new Date crea una nueva fecha
    }

    public boolean esValido(String token, String username) {
        try {
            String usernameToken = extraerUserName(token);
            return usernameToken.equals(username) && !estaExpirado(token);
        } catch (Exception e) {
            return false;
        }
    }

}
