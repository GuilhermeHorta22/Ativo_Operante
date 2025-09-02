package unoeste.fipp.ativooperante_be.util;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JWTTokenProvider {
    private static final SecretKey CHAVE = Keys.hmacShaKeyFor(
            "MINHACHAVESECRETA_MINHACHAVESECRETA".getBytes(StandardCharsets.UTF_8));

    static public String getToken(String usuario, String nivel) {
        String jwtToken = Jwts.builder()
                .setSubject(usuario) // Usar o email do usuário como subject
                .setIssuer("localhost:8080")
                .claim("nivel", nivel)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(60L) // Aumentado para 60 minutos
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(CHAVE)
                .compact();
        return jwtToken;
    }

    static public boolean verifyToken(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Jwts.parserBuilder()
                    .setSigningKey(CHAVE)
                    .build()
                    .parseClaimsJws(token).getSignature();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao verificar token: " + e.getMessage());
        }
        return false;
    }

    static public Claims getAllClaimsFromToken(String token) {
        Claims claims = null;
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            claims = Jwts.parserBuilder()
                    .setSigningKey(CHAVE)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Erro ao recuperar as informações (claims): " + e.getMessage());
        }
        return claims;
    }

    // Método para extrair email do token
    static public String getEmailFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    // Método para extrair o nível de acesso do token
    static public String getNivelFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims != null ? claims.get("nivel", String.class) : null;
    }
}