package ativo_operante.ativooperante_be.repositories;

import ativo_operante.ativooperante_be.entities.Orgao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgaoRepository extends JpaRepository<Orgao, Long> {
}
