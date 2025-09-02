package unoeste.fipp.ativooperante_be.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import unoeste.fipp.ativooperante_be.entities.Erro;
import unoeste.fipp.ativooperante_be.entities.Usuario;
import unoeste.fipp.ativooperante_be.services.UsuarioService;
import unoeste.fipp.ativooperante_be.util.JWTTokenProvider;

@RestController
@RequestMapping("acesso")
public class AcessoRestController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("autenticar/{email}/{senha}")
    public ResponseEntity<Object> autenticar(@PathVariable String email, @PathVariable String senha) {
        Usuario usuario = usuarioService.autenticar(email, senha);
        if (usuario != null) {
            String token = JWTTokenProvider.getToken(usuario.getEmail(), String.valueOf(usuario.getNivel()));

            JwtResponse response = new JwtResponse();
            response.setToken(token);
            response.setId(usuario.getId());
            response.setEmail(usuario.getEmail());
            response.setNivel(usuario.getNivel());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(new Erro("Usuário não cadastrado ou senha inválida"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioService.autenticar(loginRequest.getEmail(), loginRequest.getSenha());
        if (usuario != null) {
            // Gerar token JWT
            String token = JWTTokenProvider.getToken(usuario.getEmail(), String.valueOf(usuario.getNivel()));

            JwtResponse response = new JwtResponse();
            response.setToken(token);
            response.setId(usuario.getId());
            response.setEmail(usuario.getEmail());
            response.setNivel(usuario.getNivel());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(new Erro("Credenciais inválidas"));
        }
    }


}