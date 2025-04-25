package ativo_operante.ativooperante_be.repositories;

import ativo_operante.ativooperante_be.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
