package ativo_operante.ativooperante_be.service;

import ativo_operante.ativooperante_be.entities.Denuncia;
import ativo_operante.ativooperante_be.entities.Feedback;
import ativo_operante.ativooperante_be.entities.Usuario;
import ativo_operante.ativooperante_be.repositories.DenunciaRepository;

import ativo_operante.ativooperante_be.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DenunciaService
{
    @Autowired
    private DenunciaRepository denunciaRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;  // Injetando o repositório de Feedback

    //retorna todas as denuncias registrada no banco
    public List<Denuncia> getAll()
    {
        return denunciaRepository.findAll();
    }

    //retorna apenas uma denuncia pelo seu id
    public Optional<Denuncia> getById(Long id)
    {
        return denunciaRepository.findById(id);
    }

    //salva informações no banco
    public Denuncia save(Denuncia denuncia)
    {
        return denunciaRepository.save(denuncia);
    }

    //alterando informações já existentes no banco
    public Denuncia update(Denuncia denuncia)
    {
        if(denunciaRepository.existsById(denuncia.getId()))
            return denunciaRepository.save(denuncia);
        return null;
    }

    //deletando uma denuncia
    public void delete(Long id)
    {
        denunciaRepository.deleteById(id);
    }

    //deletando uma denuncia se ela existir
    public boolean deleteExistId(Long id)
    {
        if(denunciaRepository.existsById(id))
        {
            denunciaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean addFeedBack(Feedback feedback) {
        try {
            // Associando o feedback à denúncia
            Denuncia denuncia = denunciaRepository.findById(feedback.getDenuncia().getId()).orElse(null);
            if (denuncia != null) {
                feedback.setDenuncia(denuncia);  // Associando a denúncia ao feedback
                feedbackRepository.save(feedback);  // Salvando o feedback no repositório de Feedback
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();  // Logando erro para depuração
            return false;
        }
    }


    public List<Denuncia> getAllByUsuario(Long id) {
        return denunciaRepository.findAllByUsuario(new Usuario(id,0L,"",0,0));
    }
}
