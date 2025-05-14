package ativo_operante.ativooperante_be.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "segredo_super_secreto"; // Chave secreta (pode ser configurada em propriedades ou env)
    private static final long EXPIRATION_TIME = 86400000; // 1 dia em milissegundos

    // Gera o token JWT
    public static String generateToken(String email, int nivel) {
        return Jwts.builder()
                .setSubject(email)
                .claim("nivel", nivel)  // Adiciona o nível de acesso ao token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // Define a data de expiração
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // Valida o token JWT e retorna os detalhes
    public static Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Extrai o email do token JWT
    public static String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // Verifica se o token está expirado
    public static boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Verifica se o token é válido
    public static boolean validateToken(String token, String email) {
        return (email.equals(extractEmail(token)) && !isTokenExpired(token));
    }
}