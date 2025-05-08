package ativo_operante.ativooperante_be.repositories;

import ativo_operante.ativooperante_be.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
