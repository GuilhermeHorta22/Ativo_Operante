package unoeste.fipp.ativooperante_be.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unoeste.fipp.ativooperante_be.entities.Erro;
import unoeste.fipp.ativooperante_be.entities.Orgaos;
import unoeste.fipp.ativooperante_be.services.OrgaosService;

import java.util.List;


@RestController
@RequestMapping("apis/orgaos")
public class OrgaosRestController {
    @Autowired
    private OrgaosService orgaosService;


    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<Orgaos> orgaosList;
        orgaosList = orgaosService.getAll();
        if (!orgaosList.isEmpty())
            return ResponseEntity.ok(orgaosList);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Erro("Nenhum órgão cadastrado"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getId(@PathVariable(value = "id") Long id) {
        Orgaos orgao = orgaosService.getId(id);
        if (orgao != null)
            return ResponseEntity.ok(orgao);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Erro("Órgão não encontrado"));
    }


    @PostMapping("/adm")
    public ResponseEntity<Object> addOrgao(@RequestBody Orgaos orgao) {
        Orgaos novoOrgao = orgaosService.save(orgao);
        if (novoOrgao != null)
            return ResponseEntity.status(HttpStatus.CREATED).body(novoOrgao);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Erro("Erro ao cadastrar órgão"));
    }

    @PutMapping("/adm")
    public ResponseEntity<Object> uptOrgao(@RequestBody Orgaos orgao) {
        if (orgao.getId() == null || orgao.getId() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Erro("ID do órgão é obrigatório para atualização"));
        }

        // Verifica se o órgão existe antes de tentar atualizar
        Orgaos orgaoExistente = orgaosService.getId(orgao.getId());
        if (orgaoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Erro("Órgão não encontrado para atualização"));
        }

        Orgaos orgaoAtualizado = orgaosService.save(orgao);
        if (orgaoAtualizado != null)
            return ResponseEntity.ok(orgaoAtualizado);
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Erro("Erro ao alterar órgão"));
    }


    @DeleteMapping("/adm/{id}")
    public ResponseEntity<Object> deleteOrgao(@PathVariable(value = "id") Long id) {
        // Verifica se o órgão existe antes de tentar excluir
        Orgaos orgaoExistente = orgaosService.getId(id);
        if (orgaoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Erro("Órgão não encontrado"));
        }

        if (orgaosService.apagarOrgao(id))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Erro("Erro ao apagar o órgão"));
    }

    @GetMapping("/{id}/em-uso")
    public ResponseEntity<Object> verificarEmUso(@PathVariable(value = "id") Long id) {
        boolean emUso = orgaosService.verificarOrgaoEmUso(id);
        return ResponseEntity.ok(emUso);
    }
}