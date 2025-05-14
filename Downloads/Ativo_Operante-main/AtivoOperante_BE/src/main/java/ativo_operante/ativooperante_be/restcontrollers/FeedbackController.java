package ativo_operante.ativooperante_be.restcontrollers;

import ativo_operante.ativooperante_be.entities.Erro;
import ativo_operante.ativooperante_be.entities.Feedback;
import ativo_operante.ativooperante_be.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("apis/feedback")
public class FeedbackController
{
    @Autowired
    FeedbackService feedbackService;

    @GetMapping
    public ResponseEntity<Object> getAll()
    {
        List <Feedback> feedbackList;
        feedbackList = feedbackService.getAll();
        if(!feedbackList.isEmpty())
            return ResponseEntity.ok(feedbackList);
        return ResponseEntity.badRequest().body(new Erro("Erro ao listar os feedbacks!"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id)
    {
        Feedback feedback = feedbackService.findById(id).orElse(null);
        if(feedback != null)
            return ResponseEntity.ok(feedback);
        return ResponseEntity.badRequest().body("Não foi encontrado o feedback com esse id!");
    }

    @PostMapping
    public ResponseEntity<Object> addFeedback(@RequestBody Feedback feedback)
    {
        try
        {
            Feedback novoFeedback = feedbackService.save(feedback);
            return ResponseEntity.ok(novoFeedback);
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body("Erro ao adicionar um novo feedback!");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delFeedback(@PathVariable Long id)
    {
        Feedback feedback = feedbackService.findById(id).orElse(null);
        if(feedback != null)
        {
            feedbackService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().body("Erro ao excluir um feedback!");
    }

    @PutMapping
    public ResponseEntity<Object> updateFeedback(@RequestBody Feedback feedback)
    {
        try
        {
            Feedback feedbackAlterado = feedbackService.update(feedback);
            return ResponseEntity.ok(feedbackAlterado);
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body("Erro ao atualizar um feedback!");
        }
    }
}
