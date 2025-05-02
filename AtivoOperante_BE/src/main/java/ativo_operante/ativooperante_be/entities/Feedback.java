package ativo_operante.ativooperante_be.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fee_id")
    private Long id;

    @Column(name = "fee_texto")
    private String texto;

    @OneToOne
    @JoinColumn(name = "den_id", nullable = false)
    private Denuncia denuncia;

    public Feedback(Long id, String texto)
    {
        this.id = id;
        this.texto = texto;
    }
    public Feedback()
    {
        this(0L,"");
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }
    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }
}
