package ativo_operante.ativooperante_be.restcontrollers.admin;

import ativo_operante.ativooperante_be.entities.Erro;
import ativo_operante.ativooperante_be.entities.Usuario;
import ativo_operante.ativooperante_be.repositories.UsuarioRepository;
import ativo_operante.ativooperante_be.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("apis/admin/usuario")
public class UsuarioAdminController
{
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<Object> getAll()
    {
        List<Usuario> usuarioList = usuarioService.getAll();
        if(!usuarioList.isEmpty())
            return ResponseEntity.ok(usuarioList);
        return ResponseEntity.badRequest().body(new Erro("Erro ao listas os usuarios cadastrado"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsuario(@PathVariable Long id)
    {
        Usuario usuario = usuarioService.getById(id).orElse(null);
        if(usuario != null)
            return ResponseEntity.ok(usuario);
        return ResponseEntity.badRequest().body("Usuario nao Encontrado");
    }

    @PostMapping
    public ResponseEntity<Object> addUsuario(@RequestBody Usuario usuario)
    {
        try
        {
            Usuario novoUsuario = usuarioService.save(usuario);
            return ResponseEntity.ok(novoUsuario);
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body("Erro ao adicionar um novo usuario");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delUsuario(@PathVariable Long id)
    {
        Usuario usuario = usuarioService.getById(id).orElse(null);
        if(usuario != null)
        {
            usuarioService.delete(usuario);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().body("Erro ao apagar o usuario");
    }

    @PutMapping
    public ResponseEntity<Object> updateUsuario(@RequestBody Usuario usuario)
    {
        try
        {
            Usuario usuarioAlterado = usuarioService.save(usuario);
            return ResponseEntity.ok(usuarioAlterado);
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body("Erro ao alterar o usuario");
        }
    }
}
