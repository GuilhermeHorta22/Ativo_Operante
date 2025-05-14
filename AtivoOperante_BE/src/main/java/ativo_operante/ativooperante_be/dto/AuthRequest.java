package ativo_operante.ativooperante_be.dto;

public class AuthRequest {
    public String email;
    public int senha;

    public String getEmail() {
        return this.email;  // Retorna o valor real do campo email
    }

    public int getSenha() {
        return this.senha;  // Retorna o valor real do campo senha
    }

}