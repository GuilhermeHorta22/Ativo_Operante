package unoeste.fipp.ativooperante_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unoeste.fipp.ativooperante_be.entities.Tipo;

import java.util.List;


@Repository
public interface TipoRepository extends JpaRepository<Tipo, Long> {


    Tipo findByNome(String nome);

    List<Tipo> findByNomeContainingIgnoreCase(String termoBusca);


    @Query("SELECT t FROM Tipo t WHERE t.id NOT IN (SELECT DISTINCT d.tipo.id FROM Denuncia d)")
    List<Tipo> findTiposNaoUtilizados();


    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Denuncia d WHERE d.tipo.id = :tipoId")
    boolean isTipoEmUso(@Param("tipoId") Long tipoId);


    @Query("SELECT t.id, t.nome, COUNT(d) FROM Tipo t LEFT JOIN Denuncia d ON t.id = d.tipo.id GROUP BY t.id, t.nome ORDER BY COUNT(d) DESC")
    List<Object[]> getEstatisticasUsoTipos();


    List<Tipo> findAllByOrderByNomeAsc();


    @Query(value = "SELECT t.tip_id, t.tip_nome, COUNT(d.den_id) as total_denuncias " +
            "FROM tipo t LEFT JOIN denuncia d ON t.tip_id = d.tip_id " +
            "GROUP BY t.tip_id, t.tip_nome " +
            "ORDER BY total_denuncias DESC " +
            "LIMIT :limite", nativeQuery = true)
    List<Object[]> findTiposMaisUtilizados(@Param("limite") int limite);

    @Query("SELECT DISTINCT t FROM Tipo t JOIN Denuncia d ON t.id = d.tipo.id WHERE d.urgencia >= 4")
    List<Tipo> findTiposComDenunciasUrgentes();


    @Query("SELECT t.id, t.nome, AVG(d.urgencia), COUNT(d) " +
            "FROM Tipo t LEFT JOIN Denuncia d ON t.id = d.tipo.id " +
            "GROUP BY t.id, t.nome " +
            "ORDER BY AVG(d.urgencia) DESC NULLS LAST")
    List<Object[]> findTiposWithAverageUrgency();


    @Query("SELECT t, COUNT(d) FROM Tipo t LEFT JOIN Denuncia d ON t.id = d.tipo.id " +
            "WHERE LOWER(t.nome) LIKE LOWER(CONCAT('%', :termo, '%')) " +
            "GROUP BY t.id, t.nome " +
            "ORDER BY COUNT(d) DESC")
    List<Object[]> findTiposByNomeAndOrderByDenunciaCount(@Param("termo") String termo);


    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Tipo t " +
            "WHERE LOWER(t.nome) = LOWER(:nome) AND t.id <> :id")
    boolean existsByNomeIgnoreCaseAndIdNot(@Param("nome") String nome, @Param("id") Long id);


    boolean existsByNomeIgnoreCase(String nome);


    @Query("SELECT DISTINCT t FROM Tipo t JOIN Denuncia d ON t.id = d.tipo.id " +
            "WHERE d.feedBack IS NULL")
    List<Tipo> findTiposWithoutFeedback();


    @Query(value = "SELECT t.tip_id, t.tip_nome, " +
            "COUNT(d.den_id) as total_denuncias, " +
            "AVG(d.den_urgencia) as media_urgencia, " +
            "SUM(CASE WHEN f.fee_id IS NOT NULL THEN 1 ELSE 0 END) as total_feedback, " +
            "MAX(d.den_data) as ultima_denuncia " +
            "FROM tipo t " +
            "LEFT JOIN denuncia d ON t.tip_id = d.tip_id " +
            "LEFT JOIN feedback f ON d.den_id = f.den_id " +
            "GROUP BY t.tip_id, t.tip_nome " +
            "ORDER BY total_denuncias DESC", nativeQuery = true)
    List<Object[]> getDadosDashboardTipos();


    @Query(value = "SELECT t.tip_nome as tipo, o.org_nome as orgao, COUNT(d.den_id) as total " +
            "FROM tipo t " +
            "JOIN denuncia d ON t.tip_id = d.tip_id " +
            "JOIN orgaos o ON d.org_id = o.org_id " +
            "GROUP BY t.tip_nome, o.org_nome " +
            "ORDER BY total DESC", nativeQuery = true)
    List<Object[]> getDistribuicaoDenunciasPorTipoEOrgao();


    @Query(value = "SELECT t.tip_nome, " +
            "EXTRACT(YEAR FROM d.den_data) as ano, " +
            "EXTRACT(MONTH FROM d.den_data) as mes, " +
            "COUNT(d.den_id) as total " +
            "FROM tipo t " +
            "JOIN denuncia d ON t.tip_id = d.tip_id " +
            "GROUP BY t.tip_nome, ano, mes " +
            "ORDER BY t.tip_nome, ano, mes", nativeQuery = true)
    List<Object[]> getEvolucaoTemporalPorTipo();

    @Query("SELECT t FROM Tipo t WHERE LOWER(t.nome) = LOWER(:nome)")
    Tipo findByNomeIgnoreCase(@Param("nome") String nome);
}