package ativo_operante.ativooperante_be.restcontrollers.cidadao;

import ativo_operante.ativooperante_be.entities.Erro;
import ativo_operante.ativooperante_be.entities.Usuario;
import ativo_operante.ativooperante_be.repositories.UsuarioRepository;
import ativo_operante.ativooperante_be.service.UsuarioService;
import ativo_operante.ativooperante_be.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("apis/cidadao/usuario")
public class UsuarioCidadaoController
{
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsuario(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Long userId = JwtUtil.getIdFromToken(token.replace("Bearer ", ""));

        if (userId !=id) {
            return ResponseEntity.status(403).body("Você só pode acessar seu próprio perfil");
        }

        return usuarioService.getById(id)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Usuário não encontrado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delUsuario(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Long userId = JwtUtil.getIdFromToken(token.replace("Bearer ", ""));

        if (!userId.equals(id)) {
            return ResponseEntity.status(403).body("Você só pode excluir sua própria conta");
        }

        return usuarioService.getById(id).map(usuario -> {
            usuarioService.delete(usuario);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.badRequest().body("Usuário não encontrado"));
    }

    @PutMapping
    public ResponseEntity<Object> updateUsuario(@RequestBody Usuario usuario, @RequestHeader("Authorization") String token) {
        Long userId = JwtUtil.getIdFromToken(token.replace("Bearer ", ""));

        if (!usuario.getId().equals(userId)) {
            return ResponseEntity.status(403).body("Você só pode atualizar seus próprios dados");
        }

        try {
            Usuario usuarioAlterado = usuarioService.save(usuario);
            return ResponseEntity.ok(usuarioAlterado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao alterar o usuário");
        }
    }

}
