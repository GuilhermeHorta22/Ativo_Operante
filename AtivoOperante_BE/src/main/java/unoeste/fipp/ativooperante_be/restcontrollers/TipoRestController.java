package unoeste.fipp.ativooperante_be.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unoeste.fipp.ativooperante_be.entities.Erro;
import unoeste.fipp.ativooperante_be.entities.Tipo;
import unoeste.fipp.ativooperante_be.services.TipoService;

import java.util.List;


@RestController
@RequestMapping("apis/tipo")
public class TipoRestController {
    @Autowired
    private TipoService tipoService;

    @GetMapping()
    public ResponseEntity<Object> getAll(){
        List<Tipo> tipoList = tipoService.getAll();
        if (!tipoList.isEmpty())
            return ResponseEntity.ok(tipoList);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Erro("Nenhum tipo de problema cadastrado"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTipo(@PathVariable(value = "id") Long id){
        Tipo tipo = tipoService.getTipo(id);
        if (tipo != null)
            return ResponseEntity.ok(tipo);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Erro("Tipo de problema não encontrado"));
    }


    @PostMapping("/adm")
    public ResponseEntity<Object> addTipo(@RequestBody Tipo tipo){
        // Validação básica
        if (tipo.getNome() == null || tipo.getNome().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Erro("Nome do tipo de problema é obrigatório"));
        }

        Tipo novoTipo = tipoService.save(tipo);
        if (novoTipo != null)
            return ResponseEntity.status(HttpStatus.CREATED).body(novoTipo);
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Erro("Erro ao cadastrar tipo de problema"));
    }


    @PutMapping("/adm")
    public ResponseEntity<Object> uptTipo(@RequestBody Tipo tipo){
        if (tipo.getId() == null || tipo.getId() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Erro("ID do tipo de problema é obrigatório para atualização"));
        }

        if (tipo.getNome() == null || tipo.getNome().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Erro("Nome do tipo de problema é obrigatório"));
        }

        // Verifica se o tipo existe antes de tentar atualizar
        Tipo tipoExistente = tipoService.getTipo(tipo.getId());
        if (tipoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Erro("Tipo de problema não encontrado para atualização"));
        }

        Tipo tipoAtualizado = tipoService.save(tipo);
        if (tipoAtualizado != null)
            return ResponseEntity.ok(tipoAtualizado);
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Erro("Erro ao alterar tipo de problema"));
    }


    @DeleteMapping("/adm/{id}")
    public ResponseEntity<Object> deleteTipo(@PathVariable(value = "id") Long id){
        // Verifica se o tipo existe antes de tentar excluir
        Tipo tipoExistente = tipoService.getTipo(id);
        if (tipoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Erro("Tipo de problema não encontrado"));
        }

        // Verifica se o tipo está sendo usado em alguma denúncia
        boolean emUso = tipoService.verificarTipoEmUso(id);
        if (emUso) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new Erro("Não é possível excluir este tipo de problema pois está sendo utilizado em denúncias"));
        }

        if (tipoService.apagarTipo(id))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Erro("Erro ao apagar o tipo de problema"));
    }

    @GetMapping("/{id}/em-uso")
    public ResponseEntity<Object> verificarEmUso(@PathVariable(value = "id") Long id) {
        // Verifica se o tipo existe
        Tipo tipoExistente = tipoService.getTipo(id);
        if (tipoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Erro("Tipo de problema não encontrado"));
        }

        boolean emUso = tipoService.verificarTipoEmUso(id);
        return ResponseEntity.ok(emUso);
    }


    @GetMapping("/mais-frequentes")
    public ResponseEntity<Object> getTiposMaisFrequentes() {
        List<Object[]> tiposMaisFrequentes = tipoService.getTiposMaisFrequentes();
        if (!tiposMaisFrequentes.isEmpty())
            return ResponseEntity.ok(tiposMaisFrequentes);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Erro("Não há estatísticas de tipos de problemas disponíveis"));
    }
}