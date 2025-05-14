package ativo_operante.ativooperante_be.dto;

public class AuthResponse {
    public String token;
    public int nivel;

    public AuthResponse(String token, int nivel) {
        this.token = token;
        this.nivel = nivel;
    }
}