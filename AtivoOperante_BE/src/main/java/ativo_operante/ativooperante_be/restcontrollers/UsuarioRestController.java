package ativo_operante.ativooperante_be.restcontrollers;

import ativo_operante.ativooperante_be.dto.AuthRequest;
import ativo_operante.ativooperante_be.dto.AuthResponse;
import ativo_operante.ativooperante_be.entities.Erro;
import ativo_operante.ativooperante_be.entities.Usuario;
import ativo_operante.ativooperante_be.repositories.UsuarioRepository;
import ativo_operante.ativooperante_be.service.UsuarioService;
import ativo_operante.ativooperante_be.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("apis/usuario")
public class UsuarioRestController
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

    @PostMapping("/register")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody AuthRequest authRequest) {
        Optional<Usuario> existente = usuarioService.findByEmail(authRequest.getEmail());
        if (existente.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já existe");
        }

        Usuario novo = new Usuario();
        novo.setEmail(authRequest.getEmail());
        novo.setSenha(authRequest.getSenha());
        novo.setNivel(1); // padrão
        novo.setCpf(0L);  // ou receber no DTO

        usuarioRepository.save(novo);
        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request)
    {
        // Busca o usuário pelo email
        List<Usuario> usuarios = usuarioService.getAll();
        Usuario usuarioEncontrado = usuarios.stream()
                .filter(u -> u.getEmail().equals(request.getEmail()))
                .findFirst()
                .orElse(null);

        if (usuarioEncontrado == null) {
            return ResponseEntity.status(401).body(new Erro("Email ou senha inválidos"));
        }

        // Verificar a senha sem criptografia
        if (usuarioEncontrado.getSenha() != request.getSenha()) {
            return ResponseEntity.status(401).body(new Erro("Email ou senha inválidos"));
        }

        // Geração do token
        String token = JwtUtil.generateToken(usuarioEncontrado.getEmail(), usuarioEncontrado.getNivel());

        AuthResponse response = new AuthResponse(token, usuarioEncontrado.getNivel());
        return ResponseEntity.ok(response);
    }
}
