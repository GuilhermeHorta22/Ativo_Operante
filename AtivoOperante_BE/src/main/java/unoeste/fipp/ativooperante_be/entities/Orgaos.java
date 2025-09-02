package unoeste.fipp.ativooperante_be.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "orgaos")
public class Orgaos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_id")
    private Long id;
    @Column(name = "org_nome")
    private String nome;

    public Orgaos() {
        this(0L,"");
    }

    public Orgaos(Long id, String nome) {
        this.id = id;
        this.nome = nome;
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
