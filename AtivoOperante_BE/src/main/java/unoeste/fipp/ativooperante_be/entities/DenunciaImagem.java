package unoeste.fipp.ativooperante_be.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "denuncia_imagem")
public class DenunciaImagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "denuncia_id", unique = true)
    @JsonBackReference
    private Denuncia denuncia;

    @Column(name = "nome")
    private String nome;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "tamanho")
    private Long tamanho;

    // Remove @Lob here
    @Column(name = "dados", columnDefinition = "BYTEA")
    private byte[] dados;

    @Column(name = "data_upload")
    private LocalDateTime dataUpload = LocalDateTime.now();

    public DenunciaImagem() {
    }

    public DenunciaImagem(Denuncia denuncia, String nome, String tipo, Long tamanho, byte[] dados) {
        this.denuncia = denuncia;
        this.nome = nome;
        this.tipo = tipo;
        this.tamanho = tamanho;
        this.dados = dados;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }

    public byte[] getDados() {
        return dados;
    }

    public void setDados(byte[] dados) {
        this.dados = dados;
    }

    public LocalDateTime getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(LocalDateTime dataUpload) {
        this.dataUpload = dataUpload;
    }
}