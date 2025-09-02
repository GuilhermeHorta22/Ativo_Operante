package unoeste.fipp.ativooperante_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unoeste.fipp.ativooperante_be.entities.FeedBack;

import java.util.List;

@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {


    @Query("SELECT f FROM FeedBack f WHERE f.denuncia.id = :denunciaId")
    FeedBack findByDenunciaId(@Param("denunciaId") Long denunciaId);


    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM FeedBack f WHERE f.denuncia.id = :denunciaId")
    boolean existsByDenunciaId(@Param("denunciaId") Long denunciaId);


    @Query("SELECT f FROM FeedBack f JOIN f.denuncia d WHERE d.usuario.id = :usuarioId ORDER BY d.data DESC")
    List<FeedBack> findFeedbacksByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT f FROM FeedBack f JOIN f.denuncia d WHERE d.tipo.id = :tipoId ORDER BY d.data DESC")
    List<FeedBack> findFeedbacksByTipoId(@Param("tipoId") Long tipoId);


    @Query("SELECT f FROM FeedBack f JOIN f.denuncia d WHERE d.orgao.id = :orgaoId ORDER BY d.data DESC")
    List<FeedBack> findFeedbacksByOrgaoId(@Param("orgaoId") Long orgaoId);

    @Query("SELECT f FROM FeedBack f WHERE LOWER(f.texto) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<FeedBack> findByTextoContainingIgnoreCase(@Param("termo") String termo);

    @Query("SELECT t.id, t.nome, COUNT(f) FROM FeedBack f JOIN f.denuncia d JOIN d.tipo t GROUP BY t.id, t.nome ORDER BY COUNT(f) DESC")
    List<Object[]> countFeedbacksByTipo();

    @Query("SELECT o.id, o.nome, COUNT(f) FROM FeedBack f JOIN f.denuncia d JOIN d.orgao o GROUP BY o.id, o.nome ORDER BY COUNT(f) DESC")
    List<Object[]> countFeedbacksByOrgao();


    @Query("SELECT u.id, u.email, COUNT(f) FROM FeedBack f JOIN f.denuncia d JOIN d.usuario u GROUP BY u.id, u.email ORDER BY COUNT(f) DESC")
    List<Object[]> countFeedbacksByUsuario();

    @Query("SELECT f FROM FeedBack f JOIN f.denuncia d ORDER BY d.data DESC")
    List<FeedBack> findRecentFeedbacks();

    @Query("SELECT f FROM FeedBack f JOIN f.denuncia d WHERE d.urgencia >= 4 ORDER BY d.urgencia DESC, d.data DESC")
    List<FeedBack> findFeedbacksForUrgentDenuncias();



    @Query(value = "SELECT COUNT(*) FROM feedback f " +
            "WHERE f.data_feedback BETWEEN :dataInicio AND :dataFim", nativeQuery = true)
    Long countFeedbacksInPeriod(@Param("dataInicio") String dataInicio, @Param("dataFim") String dataFim);


    /**
     * Busca feedbacks mais recentes para um tipo específico (limite N)
     */
    @Query("SELECT f FROM FeedBack f JOIN f.denuncia d WHERE d.tipo.id = :tipoId ORDER BY d.data DESC")
    List<FeedBack> findRecentFeedbacksByTipoId(@Param("tipoId") Long tipoId, org.springframework.data.domain.Pageable pageable);
}