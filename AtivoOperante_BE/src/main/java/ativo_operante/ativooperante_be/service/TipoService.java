package ativo_operante.ativooperante_be.service;

import ativo_operante.ativooperante_be.entities.Tipo;
import ativo_operante.ativooperante_be.repositories.TipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//Essa classe contem as regras de negocio da nossa aplicação
@Service
public class TipoService
{
    @Autowired
    private TipoRepository tipoRepository;

    //retornando uma lista com todos os registro da Tabela tipo do banco de dados
    public List<Tipo> getAll()
    {
        return tipoRepository.findAll();
    }

    //implementação dos demais metodos para CRUD
    //buscando um registro pelo Id no banco de dados
    public Optional<Tipo> getById(Long id)
    {
        return tipoRepository.findById(id);
    }

    //salva um novo registro Tipo no banco de dados
    public Tipo save(Tipo tipo)
    {
        return tipoRepository.save(tipo);
    }

    //altera um registro Tipo existente no banco de dados
    public Tipo update(Tipo tipo)
    {
        if(tipoRepository.existsById(tipo.getId()))
            return tipoRepository.save(tipo);
        return null;
    }

    //deleta um tipo do banco de dados atraves do id
    public void delete(Long id)
    {
        tipoRepository.deleteById(id);
    }

    //deleta um tipo do banco de dados atraves do id, mas apenas se ele existir por isso do return true ou false
    public boolean deleteIfExists(Long id)
    {
        if(tipoRepository.existsById(id))
        {
            tipoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
