package unoeste.fipp.ativooperante_be.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class FeedBack {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "fee_id")
    private Long id;
    @Column(name = "fee_texto")
    private String texto;

    @OneToOne
    @JoinColumn(name = "den_id",unique = true)
    @JsonBackReference
    private Denuncia denuncia;

    public FeedBack(Long id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public FeedBack() {
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
