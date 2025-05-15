package ativo_operante.ativooperante_be.service;

import ativo_operante.ativooperante_be.entities.Denuncia;
import ativo_operante.ativooperante_be.entities.Feedback;
import ativo_operante.ativooperante_be.entities.Usuario;
import ativo_operante.ativooperante_be.repositories.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DenunciaService
{
    @Autowired
    private DenunciaRepository denunciaRepository;

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

    public boolean addFeedBack(Feedback feedBack){
        try {
            denunciaRepository.addFeedback(feedBack.getId(), feedBack.getTexto());
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public List<Denuncia> getAllByUsuario(Long id) {
        return denunciaRepository.findAllByUsuario(new Usuario(id,0L,"",0,0));
    }
}
