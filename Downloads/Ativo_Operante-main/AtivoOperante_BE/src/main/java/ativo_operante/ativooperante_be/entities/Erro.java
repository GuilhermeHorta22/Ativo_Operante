package ativo_operante.ativooperante_be.entities;

import jakarta.persistence.Entity;

public class Erro
{
    private String mensagem;

    public Erro(String mensagem)
    {
        this.mensagem = mensagem;
    }

    public void setMensagem(String mensagem)
    {
        this.mensagem = mensagem;
    }
    public String getMensagem()
    {
        return mensagem;
    }
}
