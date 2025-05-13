package ativo_operante.ativooperante_be.service;

import ativo_operante.ativooperante_be.entities.Orgao;
import ativo_operante.ativooperante_be.repositories.OrgaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrgaoService
{
    @Autowired
    private OrgaoRepository orgaoRepository;

    //retorna uma lista com todos os orgaos existentes
    public List<Orgao> getAll()
    {
        return orgaoRepository.findAll();
    }

    //implementação dos demais metodos para o CRUD
    //busca um orgao atraves do seu id
    public Optional<Orgao> getById(Long id)
    {
        return orgaoRepository.findById(id);
    }

    //salva um novo orgao
    public Orgao save(Orgao orgao)
    {
        return orgaoRepository.save(orgao);
    }

    //altera um novo orgao
    /*public Orgao update(Orgao orgao)
    {
        if(orgaoRepository.existsById(orgao.getId()))
            return orgaoRepository.save(orgao);
        return null;
    }*/
    public Orgao update(Orgao orgao)
    {
        if(!orgaoRepository.existsById(orgao.getId()))
            throw new RuntimeException("Órgão não encontrado para atualização");
        return orgaoRepository.save(orgao);
    }


    //deleta um orgao
    public void delete(Long id)
    {
        orgaoRepository.deleteById(id);
    }

    //delata um orgao, mas só se ele existir
    public Boolean deleteIfExists(Long id)
    {
        if(orgaoRepository.existsById(id))
        {
            orgaoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
