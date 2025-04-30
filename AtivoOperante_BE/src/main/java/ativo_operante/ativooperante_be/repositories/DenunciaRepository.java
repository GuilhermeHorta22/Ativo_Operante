package ativo_operante.ativooperante_be.repositories;

import ativo_operante.ativooperante_be.entities.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {
    //findByUsuario -> não sei o motivo disso
}
