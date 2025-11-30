package com.uteq.auth.service.impl;

import com.uteq.auth.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    @Override
    public String generarToken(Long usuarioId, String correoMatricula, String rolNombre) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(usuarioId.toString())
                .claim("correoMatricula", correoMatricula)
                .claim("rol", rolNombre)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public String generarRefreshToken(Long usuarioId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpiration);

        return Jwts.builder()
                .setSubject(usuarioId.toString())
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public String obtenerIdDelToken(String token) {
        return obtenerClaimsDelToken(token).getSubject();
    }

    @Override
    public String obtenerCorreoDelToken(String token) {
        return (String) obtenerClaimsDelToken(token).get("correoMatricula");
    }

    @Override
    public String obtenerRolDelToken(String token) {
        return (String) obtenerClaimsDelToken(token).get("rol");
    }

    @Override
    public Claims obtenerClaimsDelToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Error al extraer claims del JWT: {}", e.getMessage());
            throw new RuntimeException("Token JWT inválido");
        }
    }

    @Override
    public Boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expirado: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("Token JWT no soportado: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.error("Token JWT malformado: {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            log.error("Firma de JWT inválida: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("Token JWT vacío: {}", e.getMessage());
            return false;
        }
    }
}
