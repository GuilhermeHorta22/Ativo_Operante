package unoeste.fipp.ativooperante_be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unoeste.fipp.ativooperante_be.entities.Usuario;

import java.util.List;

/**
 * Repositório para acesso aos dados de usuários
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Usuario findByEmail(String email);


    Usuario findByCpf(String cpf);


    List<Usuario> findByNivel(int nivel);

    List<Usuario> findByEmailContainingIgnoreCaseOrCpfContaining(String email, String cpf);


    boolean existsByEmail(String email);


    boolean existsByCpf(String cpf);


    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u " +
            "WHERE u.email = :email AND u.id <> :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u " +
            "WHERE u.cpf = :cpf AND u.id <> :id")
    boolean existsByCpfAndIdNot(@Param("cpf") String cpf, @Param("id") Long id);

    @Query("SELECT u.nivel, COUNT(u) FROM Usuario u GROUP BY u.nivel ORDER BY u.nivel")
    List<Object[]> countUsuariosByNivel();

    @Query("SELECT u.id, u.email, COUNT(d) as total FROM Usuario u " +
            "LEFT JOIN Denuncia d ON u.id = d.usuario.id " +
            "GROUP BY u.id, u.email " +
            "ORDER BY total DESC")
    List<Object[]> countDenunciasByUsuario();


    @Query("SELECT DISTINCT u FROM Usuario u JOIN Denuncia d ON u.id = d.usuario.id " +
            "WHERE d.feedBack IS NULL")
    List<Usuario> findUsuariosWithPendingFeedback();


    @Query(value = "SELECT u.usu_id, u.usu_email, COUNT(d.den_id) as total_denuncias " +
            "FROM usuario u " +
            "LEFT JOIN denuncia d ON u.usu_id = d.usu_id " +
            "GROUP BY u.usu_id, u.usu_email " +
            "ORDER BY total_denuncias DESC " +
            "LIMIT :limite", nativeQuery = true)
    List<Object[]> findUsuariosMaisAtivos(@Param("limite") int limite);


    @Query("SELECT u, COUNT(d) as total FROM Usuario u " +
            "LEFT JOIN Denuncia d ON u.id = d.usuario.id " +
            "WHERE u.nivel = :nivel " +
            "GROUP BY u.id " +
            "ORDER BY total DESC")
    List<Object[]> findUsuariosByNivelWithDenunciaCount(@Param("nivel") int nivel);


    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha")
    Usuario findByEmailAndSenha(@Param("email") String email, @Param("senha") String senha);


    @Query("SELECT CASE WHEN u.nivel = 1 THEN true ELSE false END FROM Usuario u WHERE u.id = :id")
    boolean isAdmin(@Param("id") Long id);


    @Query("SELECT DISTINCT u FROM Usuario u JOIN Denuncia d ON u.id = d.usuario.id " +
            "WHERE d.urgencia >= 4")
    List<Usuario> findUsuariosWithUrgentDenuncias();


    @Query(value = "SELECT u.usu_id, u.usu_email, u.usu_nivel, " +
            "COUNT(d.den_id) as total_denuncias, " +
            "AVG(d.den_urgencia) as media_urgencia, " +
            "SUM(CASE WHEN f.fee_id IS NOT NULL THEN 1 ELSE 0 END) as total_feedback, " +
            "MAX(d.den_data) as ultima_denuncia " +
            "FROM usuario u " +
            "LEFT JOIN denuncia d ON u.usu_id = d.usu_id " +
            "LEFT JOIN feedback f ON d.den_id = f.den_id " +
            "GROUP BY u.usu_id, u.usu_email, u.usu_nivel " +
            "ORDER BY total_denuncias DESC", nativeQuery = true)
    List<Object[]> getDadosDashboardUsuarios();

    @Query(value = "SELECT u.usu_id, u.usu_email, t.tip_nome, COUNT(d.den_id) as total " +
            "FROM usuario u " +
            "JOIN denuncia d ON u.usu_id = d.usu_id " +
            "JOIN tipo t ON d.tip_id = t.tip_id " +
            "GROUP BY u.usu_id, u.usu_email, t.tip_nome " +
            "ORDER BY u.usu_email, total DESC", nativeQuery = true)
    List<Object[]> getDistribuicaoTiposPorUsuario();


    @Query("SELECT u FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)")
    Usuario findByEmailIgnoreCase(@Param("email") String email);

    @Query("SELECT COUNT(u) > 0 FROM Usuario u")
    boolean existsAnyUsuario();
}