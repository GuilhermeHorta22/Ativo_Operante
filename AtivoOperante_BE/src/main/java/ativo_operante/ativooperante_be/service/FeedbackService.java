package ativo_operante.ativooperante_be.service;

import ativo_operante.ativooperante_be.entities.Denuncia;
import ativo_operante.ativooperante_be.entities.Feedback;
import ativo_operante.ativooperante_be.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService
{
    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Feedback> getAll()
    {
        return feedbackRepository.findAll();
    }

    public Optional<Feedback> findById(Long id)
    {
        return feedbackRepository.findById(id);
    }

    public Feedback save(Feedback feedback)
    {
        return feedbackRepository.save(feedback);
    }

    public Feedback update(Feedback feedback) {
        Optional<Feedback> existente = feedbackRepository.findById(feedback.getId());
        if (existente.isPresent()) {
            Feedback feedbackExistente = existente.get();

            // Atualiza apenas o texto
            feedbackExistente.setTexto(feedback.getTexto());

            // (Opcional) Se quiser fazer algo com a denúncia associada
            Denuncia denuncia = feedbackExistente.getDenuncia();
            if (denuncia != null) {
                // por exemplo, logar ou atualizar alguma coisa nela
                System.out.println("Denúncia associada: " + denuncia.getId());
            }

            return feedbackRepository.save(feedbackExistente);
        }
        return null;
    }


    public void delete(Long id) {
        Optional<Feedback> feedbackOpt = feedbackRepository.findById(id);
        if (feedbackOpt.isPresent()) {
            Feedback feedback = feedbackOpt.get();

            // Desvincula da denúncia (importante para evitar re-persistência)
            Denuncia denuncia = feedback.getDenuncia();
            if (denuncia != null) {
                denuncia.setFeedback(null); // remove o feedback da denúncia
            }
            feedback.setDenuncia(null); // remove a denúncia do feedback também

            feedbackRepository.delete(feedback); // deleta o feedback
            feedbackRepository.flush(); // força a execução imediata no banco
            //Caso uma classe aponte para outra, precisa tornar NULL antes de DELETE, para evitar recriação de objeto
        }
    }

    public boolean delIsExist(Long id)
    {
        if(feedbackRepository.existsById(id))
        {
            feedbackRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
