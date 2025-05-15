package ativo_operante.ativooperante_be.repositories;

import ativo_operante.ativooperante_be.entities.Denuncia;
import ativo_operante.ativooperante_be.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {
    public List<Denuncia> findAllByUsuario(Usuario usuario);//-> seria para eu conseguir buscar uma denuncia que tenha um usuario com aquele id
    @Query(value = "INSERT INTO feedback(fee_texto,den_id) VALUES (:fee_texto, :den_id)", nativeQuery = true)
    public boolean addFeedback(@Param("den_id") Long id, @Param("fee_texto") String texto);
}
