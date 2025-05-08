package ativo_operante.ativooperante_be.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo")
public class Tipo
{
    @Id //indica que o id é a chave primaria da tabela
    @GeneratedValue(strategy= GenerationType.IDENTITY) //gerando automatico o id pelo proprio banco (auto-incremento)
    @Column(name = "tip_id") //indica qual coluna está sendo referenciada
    private Long id;

    @Column(name = "tip_nome")
    private String nome;

    public Tipo(Long id, String nome)
    {
        this.id = id;
        this.nome = nome;
    }
    //construtor sem parametro
    public Tipo()
    {
        this(0L,"");
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
}
