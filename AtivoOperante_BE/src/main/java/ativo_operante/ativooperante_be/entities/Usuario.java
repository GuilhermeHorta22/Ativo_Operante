package ativo_operante.ativooperante_be.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usu_id")
    private Long id;

    @Column(name = "usu_cpf")
    private Integer cpf;

    @Column(name = "usu_email")
    private String email;

    @Column(name = "usu_senha")
    private Integer senha;

    @Column(name = "usu_nivel")
    private Integer nivel;

    public Usuario(Long id, Integer cpf, String email, Integer senha, Integer nivel)
    {
        this.id = id;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.nivel = nivel;
    }
    public Usuario()
    {
        this(0L,null,"",null,null);
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCpf() {
        return cpf;
    }
    public void setCpf(Integer cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSenha() {
        return senha;
    }
    public void setSenha(Integer senha) {
        this.senha = senha;
    }

    public Integer getNivel() {
        return nivel;
    }
    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }
}
