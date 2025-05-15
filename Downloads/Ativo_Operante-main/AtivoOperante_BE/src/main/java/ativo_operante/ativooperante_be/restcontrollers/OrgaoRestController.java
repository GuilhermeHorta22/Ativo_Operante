package ativo_operante.ativooperante_be.restcontrollers;

import ativo_operante.ativooperante_be.entities.Erro;
import ativo_operante.ativooperante_be.entities.Orgao;
import ativo_operante.ativooperante_be.service.OrgaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("apis/orgaos")
public class OrgaoRestController
{
    @Autowired
    OrgaoService orgaoService;

    @GetMapping
    public ResponseEntity<Object> getAll()
    {
        List<Orgao> orgaoList;
        orgaoList = orgaoService.getAll();
        if(!orgaoList.isEmpty())
            return ResponseEntity.ok(orgaoList);
        return ResponseEntity.badRequest().body(new Erro("Erro ao listar os orgaos cadastrado"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrgaoId(@PathVariable Long id)
    {
        Orgao orgao = orgaoService.getById(id).orElse(null);
        if(orgao != null)
            return ResponseEntity.ok(orgao);
        else
            return ResponseEntity.badRequest().body("Orgao não encontrado");
    }

    @PostMapping
    public ResponseEntity<Object> addOrgao(@RequestBody Orgao orgao)
    {
        try
        {
            Orgao novoOrgao = orgaoService.save(orgao);
            return ResponseEntity.ok(novoOrgao);
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body("Erro ao adicionar um novo orgao");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delOrgao(@PathVariable Long id)
    {
        Orgao orgao = orgaoService.getById(id).orElse(null);
        if(orgao != null)
        {
            orgaoService.delete(orgao.getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().body("Erro ao excluir um orgao");
    }

    @PutMapping
    public ResponseEntity<Object> updateOrgao(@RequestBody Orgao orgao)
    {
        try
        {
            Orgao orgaoAlterado = orgaoService.update(orgao);
            return ResponseEntity.ok(orgaoAlterado);
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body("Erro ao alterar um orgao");
        }
    }
}
