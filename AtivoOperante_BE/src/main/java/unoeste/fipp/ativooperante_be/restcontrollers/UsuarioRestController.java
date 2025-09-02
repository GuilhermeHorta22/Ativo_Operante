package unoeste.fipp.ativooperante_be.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unoeste.fipp.ativooperante_be.entities.Erro;
import unoeste.fipp.ativooperante_be.entities.Usuario;
import unoeste.fipp.ativooperante_be.services.UsuarioService;

import java.util.List;


@RestController
@RequestMapping("apis/usuario")
public class UsuarioRestController {
    @Autowired
    private UsuarioService usuarioService;


    @GetMapping("/adm")
    public ResponseEntity<Object> getAll() {
        List<Usuario> usuarioList = usuarioService.getAll();
        if (!usuarioList.isEmpty())
            return ResponseEntity.ok(usuarioList);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Erro("Nenhum usuário cadastrado"));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsuario(@PathVariable(value = "id") Long id) {
        Usuario usuario = usuarioService.getId(id);
        if (usuario != null) {
            // Remove a senha do objeto antes de retornar
            usuario.setSenha("*****");
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Erro("Usuário não encontrado"));
        }
    }


    @PostMapping("/cadastro")
    public ResponseEntity<Object> cadastrarCidadao(@RequestBody Usuario usuario) {
        // Validações básicas
        if (usuario.getCpf() == null || usuario.getCpf().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Erro("CPF é obrigatório"));
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Erro("E-mail é obrigatório"));
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Erro("Senha é obrigatória"));
        }

        // Validar formato de CPF e e-mail
        if (!isValidCPF(usuario.getCpf())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Erro("CPF inválido"));
        }

        if (!isValidEmail(usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Erro("E-mail inválido"));
        }

        if (usuarioService.existeUsuarioComCPF(usuario.getCpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Erro("CPF já cadastrado"));
        }

        if (usuarioService.existeUsuarioComEmail(usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Erro("E-mail já cadastrado"));
        }

        usuario.setNivel(2);

        Usuario novoUsuario = usuarioService.save(usuario);
        if (novoUsuario != null) {
            novoUsuario.setSenha("*****");
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Erro("Erro ao cadastrar usuário"));
        }
    }


    @PostMapping("/adm")
    public ResponseEntity<Object> addUsuario(@RequestBody Usuario usuario) {
        if (usuario.getCpf() == null || usuario.getCpf().trim().isEmpty() ||
                usuario.getEmail() == null || usuario.getEmail().trim().isEmpty() ||
                usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Erro("CPF, e-mail e senha são obrigatórios"));
        }

        if (!isValidCPF(usuario.getCpf())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Erro("CPF inválido"));
        }

        if (!isValidEmail(usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Erro("E-mail inválido"));
        }

        if (usuarioService.existeUsuarioComCPF(usuario.getCpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Erro("CPF já cadastrado"));
        }

        if (usuarioService.existeUsuarioComEmail(usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Erro("E-mail já cadastrado"));
        }

        if (usuario.getNivel() != 1 && usuario.getNivel() != 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Erro("Nível deve ser 1 (administrador) ou 2 (cidadão)"));
        }

        Usuario novoUsuario = usuarioService.save(usuario);
        if (novoUsuario != null) {
            // Remove a senha do objeto antes de retornar
            novoUsuario.setSenha("*****");
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Erro("Erro ao cadastrar usuário"));
        }
    }


    @PutMapping
    public ResponseEntity<Object> uptUsuario(@RequestBody Usuario usuario) {
        if (usuario.getId() == null || usuario.getId() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Erro("ID do usuário é obrigatório para atualização"));
        }

        Usuario usuarioExistente = usuarioService.getId(usuario.getId());
        if (usuarioExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Erro("Usuário não encontrado para atualização"));
        }

        if (!usuario.getCpf().equals(usuarioExistente.getCpf()) &&
                usuarioService.existeUsuarioComCPF(usuario.getCpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Erro("CPF já cadastrado para outro usuário"));
        }

        if (!usuario.getEmail().equals(usuarioExistente.getEmail()) &&
                usuarioService.existeUsuarioComEmail(usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Erro("E-mail já cadastrado para outro usuário"));
        }


        if (usuarioExistente.getEmail().equals("admin@pm.br") && usuario.getNivel() != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new Erro("Não é permitido alterar o nível do usuário administrador predefinido"));
        }

        Usuario usuarioAtualizado = usuarioService.save(usuario);
        if (usuarioAtualizado != null) {
            usuarioAtualizado.setSenha("*****");
            return ResponseEntity.ok(usuarioAtualizado);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Erro("Erro ao alterar usuário"));
        }
    }


    @DeleteMapping("/adm/{id}")
    public ResponseEntity<Object> deleteUsuario(@PathVariable(value = "id") Long id) {
        Usuario usuarioExistente = usuarioService.getId(id);
        if (usuarioExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Erro("Usuário não encontrado"));
        }

        if (usuarioExistente.getEmail().equals("admin@pm.br")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new Erro("Não é permitido excluir o usuário administrador predefinido"));
        }

        if (usuarioService.usuarioPossuiDenuncias(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Erro("Não é possível excluir o usuário pois ele possui denúncias registradas"));
        }

        if (usuarioService.apagar(id))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Erro("Erro ao apagar o usuário"));
    }

    private boolean isValidCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11)
            return false;

        boolean todosDigitosIguais = true;
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                todosDigitosIguais = false;
                break;
            }
        }
        if (todosDigitosIguais)
            return false;

        return true;
    }


    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}