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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/register")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody AuthRequest authRequest) {
        Optional<Usuario> existente = usuarioService.findByEmail(authRequest.getEmail());
        if (existente.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já existe");
        }

        Usuario novo = new Usuario();
        novo.setEmail(authRequest.getEmail());
        novo.setSenha(authRequest.getSenha()); // Idealmente use criptografia
        novo.setNivel(2); // Por padrão um cidadão
        novo.setCpf(0L);  // Ajuste conforme o DTO

        usuarioRepository.save(novo);
        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        List<Usuario> usuarios = usuarioService.getAll();
        Usuario usuarioEncontrado = usuarios.stream()
                .filter(u -> u.getEmail().equals(request.getEmail()))
                .findFirst()
                .orElse(null);

        if (usuarioEncontrado == null || usuarioEncontrado.getSenha() != (request.getSenha())) {
            return ResponseEntity.status(401).body(new Erro("Email ou senha inválidos"));
        }

        String token = JwtUtil.generateToken(usuarioEncontrado.getEmail(), usuarioEncontrado.getNivel());

        AuthResponse response = new AuthResponse(token, usuarioEncontrado.getNivel());
        return ResponseEntity.ok(response);
    }
}
/* Esse aqui estava no UsuarioController
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
 */