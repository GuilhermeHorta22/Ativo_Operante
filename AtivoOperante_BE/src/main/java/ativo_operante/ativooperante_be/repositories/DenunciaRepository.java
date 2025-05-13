package ativo_operante.ativooperante_be.repositories;

import ativo_operante.ativooperante_be.entities.Denuncia;
import ativo_operante.ativooperante_be.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO feedback (fee_texto, den_id) VALUES (:fee_texto, :den_id)",nativeQuery = true)
    public void addFeedBack(@Param("den_id") Long id, @Param("fee_texto") String texto );

    public List<Denuncia> findAllByUsuario(Usuario usuario);
}
