package unoeste.fipp.ativooperante_be.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import unoeste.fipp.ativooperante_be.entities.DenunciaImagem;

import java.util.List;

@Repository
public interface DenunciaImagemRepository extends JpaRepository<DenunciaImagem, Long> {

    DenunciaImagem findByDenunciaId(Long denunciaId);

    boolean existsByDenunciaId(Long denunciaId);

    @Query(value = "SELECT t.tip_id, t.tip_nome, COUNT(di.id) as total " +
            "FROM tipo t " +
            "JOIN denuncia d ON t.tip_id = d.tip_id " +
            "JOIN denuncia_imagem di ON d.den_id = di.denuncia_id " +
            "GROUP BY t.tip_id, t.tip_nome " +
            "ORDER BY total DESC", nativeQuery = true)
    List<Object[]> countImagensPorTipo();

    @Query(value = "SELECT o.org_id, o.org_nome, COUNT(di.id) as total " +
            "FROM orgaos o " +
            "JOIN denuncia d ON o.org_id = d.org_id " +
            "JOIN denuncia_imagem di ON d.den_id = di.denuncia_id " +
            "GROUP BY o.org_id, o.org_nome " +
            "ORDER BY total DESC", nativeQuery = true)
    List<Object[]> countImagensPorOrgao();


    @Query("SELECT di FROM DenunciaImagem di " +
            "JOIN di.denuncia d WHERE d.urgencia >= 4 " +
            "ORDER BY d.urgencia DESC, d.data DESC")
    List<DenunciaImagem> findImagensDenunciasUrgentes();

    @Modifying
    @Transactional
    @Query("DELETE FROM DenunciaImagem di WHERE di.denuncia.id = :denunciaId")
    void deleteByDenunciaId(Long denunciaId);
}