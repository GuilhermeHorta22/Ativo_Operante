package ativo_operante.ativooperante_be.service;

import ativo_operante.ativooperante_be.entities.Usuario;
import ativo_operante.ativooperante_be.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService
{
    @Autowired
    private UsuarioRepository usuarioRepository;

    //retornando todos os usuarios cadastrado
    public List<Usuario> getAll()
    {
        return usuarioRepository.findAll();
    }

    //implementação dos demais metodos do CRUD
    //retorna o usuario procurado pelo id
    public Optional<Usuario> getById(Long id)
    {
        return usuarioRepository.findById(id);
    }

    //retorna um novo usuario salvo
    public Usuario save(Usuario usuario)
    {
        return usuarioRepository.save(usuario);
    }

    //retorna um usuario alterado
    public Usuario update(Usuario usuario)
    {
        if(usuarioRepository.existsById(usuario.getId())) //tem que verificar se o usuario existe
            return usuarioRepository.save(usuario);
        return null;
    }

    //deleta um usuario
    public void delete(Usuario usuario)
    {
        usuarioRepository.delete(usuario);
    }


    //deleta um usuario se ele existir
    public Boolean deleteIfExists(Long id)
    {
        if(usuarioRepository.existsById(id))
        {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

}
