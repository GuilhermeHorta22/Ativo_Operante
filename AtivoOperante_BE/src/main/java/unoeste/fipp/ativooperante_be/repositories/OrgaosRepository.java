package unoeste.fipp.ativooperante_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unoeste.fipp.ativooperante_be.entities.Orgaos;

import java.util.List;

@Repository
public interface OrgaosRepository extends JpaRepository<Orgaos, Long> {


    Orgaos findByNome(String nome);

    List<Orgaos> findByNomeContainingIgnoreCase(String termoBusca);

    @Query("SELECT o FROM Orgaos o WHERE o.id NOT IN (SELECT DISTINCT d.orgao.id FROM Denuncia d)")
    List<Orgaos> findOrgaosNaoUtilizados();


    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Denuncia d WHERE d.orgao.id = :orgaoId")
    boolean isOrgaoEmUso(@Param("orgaoId") Long orgaoId);

    @Query("SELECT o.id, o.nome, COUNT(d) FROM Orgaos o LEFT JOIN Denuncia d ON o.id = d.orgao.id GROUP BY o.id, o.nome ORDER BY COUNT(d) DESC")
    List<Object[]> getEstatisticasUsoOrgaos();


    List<Orgaos> findAllByOrderByNomeAsc();


    @Query(value = "SELECT o.org_id, o.org_nome, COUNT(d.den_id) as total_denuncias " +
            "FROM orgaos o LEFT JOIN denuncia d ON o.org_id = d.org_id " +
            "GROUP BY o.org_id, o.org_nome " +
            "ORDER BY total_denuncias DESC " +
            "LIMIT :limite", nativeQuery = true)
    List<Object[]> findOrgaosMaisUtilizados(@Param("limite") int limite);

    @Query("SELECT DISTINCT o FROM Orgaos o JOIN Denuncia d ON o.id = d.orgao.id WHERE d.urgencia >= 4")
    List<Orgaos> findOrgaosComDenunciasUrgentes();


    @Query("SELECT o.id, o.nome, AVG(d.urgencia), COUNT(d) " +
            "FROM Orgaos o LEFT JOIN Denuncia d ON o.id = d.orgao.id " +
            "GROUP BY o.id, o.nome " +
            "ORDER BY AVG(d.urgencia) DESC NULLS LAST")
    List<Object[]> findOrgaosWithAverageUrgency();

    @Query("SELECT o, COUNT(d) FROM Orgaos o LEFT JOIN Denuncia d ON o.id = d.orgao.id " +
            "WHERE LOWER(o.nome) LIKE LOWER(CONCAT('%', :termo, '%')) " +
            "GROUP BY o.id, o.nome " +
            "ORDER BY COUNT(d) DESC")
    List<Object[]> findOrgaosByNomeAndOrderByDenunciaCount(@Param("termo") String termo);


    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Orgaos o " +
            "WHERE LOWER(o.nome) = LOWER(:nome) AND o.id <> :id")
    boolean existsByNomeIgnoreCaseAndIdNot(@Param("nome") String nome, @Param("id") Long id);

    boolean existsByNomeIgnoreCase(String nome);

    @Query("SELECT DISTINCT o FROM Orgaos o JOIN Denuncia d ON o.id = d.orgao.id " +
            "WHERE d.feedBack IS NULL")
    List<Orgaos> findOrgaosWithoutFeedback();

    @Query(value = "SELECT o.org_id, o.org_nome, " +
            "COUNT(d.den_id) as total_denuncias, " +
            "AVG(d.den_urgencia) as media_urgencia, " +
            "SUM(CASE WHEN f.fee_id IS NOT NULL THEN 1 ELSE 0 END) as total_feedback, " +
            "MAX(d.den_data) as ultima_denuncia " +
            "FROM orgaos o " +
            "LEFT JOIN denuncia d ON o.org_id = d.org_id " +
            "LEFT JOIN feedback f ON d.den_id = f.den_id " +
            "GROUP BY o.org_id, o.org_nome " +
            "ORDER BY total_denuncias DESC", nativeQuery = true)
    List<Object[]> getOrgaosDashboardData();
}