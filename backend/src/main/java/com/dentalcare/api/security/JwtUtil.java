package com.dentalcare.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // La clave secreta se inyecta desde application.properties 
    @Value("${jwt.secret}")
    private String secret;

    // Tiempo de expiracion en milisegundos (1 hora) desde application.properties
    @Value("${jwt.expiration.ms}")
    private long expirationMs;

    /**
     * Construye la clave criptografica a partir del secreto configurado.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Genera un JWT firmado con los datos del usuario autenticado.
     *
     * @param username identificador del sujeto del token
     * @param rol      nombre del rol para autorizacion en el cliente
     * @return JWT en formato compacto (header.payload.signature)
     */
    public String generateToken(String username, String rol) {
        return Jwts.builder()
                .subject(username)
                .claim("rol", rol)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrae el username (subject) del token.
     */
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Valida que el token tenga firma correcta y no haya expirado.
     *
     * @return true si el token es valido
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token invalido, expirado o malformado
            return false;
        }
    }

    /**
     * Parsea y verifica la firma del token. Lanza excepcion si es invalido.
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}