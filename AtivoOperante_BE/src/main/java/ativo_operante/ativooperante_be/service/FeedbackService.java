package ativo_operante.ativooperante_be.service;

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

    public Feedback update(Feedback feedback)
    {
        if(feedbackRepository.existsById(feedback.getId()))
            return feedbackRepository.save(feedback);
        return null;
    }

    public void delete(Long id)
    {
        feedbackRepository.deleteById(id);
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
