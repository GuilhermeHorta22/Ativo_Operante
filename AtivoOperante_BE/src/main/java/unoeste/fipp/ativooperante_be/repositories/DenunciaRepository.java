package unoeste.fipp.ativooperante_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import unoeste.fipp.ativooperante_be.entities.Denuncia;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {


    @Query("SELECT d FROM Denuncia d WHERE d.usuario.id = :usuId")
    List<Denuncia> findByUsuarioId(@Param("usuId") Long usuId);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO feedback (fee_texto, den_id) VALUES (:fee_texto, :den_id)", nativeQuery = true)
    void addFeedBack(@Param("den_id") Long id, @Param("fee_texto") String texto);


    @Query(value = "SELECT t.tip_id, t.tip_nome, COUNT(d.den_id) as total " +
            "FROM tipo t LEFT JOIN denuncia d ON t.tip_id = d.tip_id " +
            "GROUP BY t.tip_id, t.tip_nome " +
            "ORDER BY total DESC", nativeQuery = true)
    List<Object[]> countDenunciasByTipo();

    @Query(value = "SELECT o.org_id, o.org_nome, COUNT(d.den_id) as total " +
            "FROM orgaos o LEFT JOIN denuncia d ON o.org_id = d.org_id " +
            "GROUP BY o.org_id, o.org_nome " +
            "ORDER BY total DESC", nativeQuery = true)
    List<Object[]> countDenunciasByOrgao();

    long countByTipoId(Long tipoId);


    long countByOrgaoId(Long orgaoId);


    long countByUsuarioId(Long usuId);

    List<Denuncia> findByDataBetween(LocalDate dataInicial, LocalDate dataFinal);

    List<Denuncia> findByUrgencia(int urgencia);


    List<Denuncia> findByTipoId(Long tipoId);

    List<Denuncia> findByOrgaoId(Long orgaoId);


    @Query("SELECT d FROM Denuncia d WHERE d.feedBack IS NULL")
    List<Denuncia> findDenunciasSemFeedback();


    @Query("SELECT d FROM Denuncia d WHERE d.feedBack IS NOT NULL")
    List<Denuncia> findDenunciasComFeedback();

    @Query("SELECT d FROM Denuncia d WHERE LOWER(d.titulo) LIKE LOWER(CONCAT('%', :termo, '%')) " +
            "OR LOWER(d.texto) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Denuncia> findByTituloContainingOrTextoContaining(@Param("termo") String termo);


    @Query("SELECT d FROM Denuncia d WHERE d.data >= :dataLimite ORDER BY d.data DESC")
    List<Denuncia> findDenunciasRecentes(@Param("dataLimite") LocalDate dataLimite);


    @Query("SELECT d FROM Denuncia d WHERE d.urgencia >= 4 ORDER BY d.urgencia DESC, d.data DESC")
    List<Denuncia> findDenunciasUrgentes();


    @Query(value = "SELECT EXTRACT(YEAR FROM den_data) as ano, " +
            "EXTRACT(MONTH FROM den_data) as mes, " +
            "COUNT(*) as total FROM denuncia " +
            "GROUP BY ano, mes " +
            "ORDER BY ano DESC, mes DESC", nativeQuery = true)
    List<Object[]> countDenunciasPorMesAno();

    @Query("SELECT d FROM Denuncia d WHERE " +
            "(:tipoId IS NULL OR d.tipo.id = :tipoId) AND " +
            "(:orgaoId IS NULL OR d.orgao.id = :orgaoId) AND " +
            "(:usuarioId IS NULL OR d.usuario.id = :usuarioId) AND " +
            "(:urgencia IS NULL OR d.urgencia = :urgencia) AND " +
            "(:dataInicial IS NULL OR d.data >= :dataInicial) AND " +
            "(:dataFinal IS NULL OR d.data <= :dataFinal) AND " +
            "(:temFeedback IS NULL OR " +
            "(:temFeedback = true AND d.feedBack IS NOT NULL) OR " +
            "(:temFeedback = false AND d.feedBack IS NULL))")
    List<Denuncia> buscarDenunciasComFiltros(
            @Param("tipoId") Long tipoId,
            @Param("orgaoId") Long orgaoId,
            @Param("usuarioId") Long usuarioId,
            @Param("urgencia") Integer urgencia,
            @Param("dataInicial") LocalDate dataInicial,
            @Param("dataFinal") LocalDate dataFinal,
            @Param("temFeedback") Boolean temFeedback
    );


    @Modifying
    @Transactional
    @Query("UPDATE Denuncia d SET d.urgencia = :urgencia WHERE d.id = :id")
    void atualizarUrgencia(@Param("id") Long id, @Param("urgencia") int urgencia);

}