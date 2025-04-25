package ativo_operante.ativooperante_be.restcontrollers;

import ativo_operante.ativooperante_be.entities.Erro;
import ativo_operante.ativooperante_be.entities.Tipo;
import ativo_operante.ativooperante_be.service.TipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //indica que essa classe é controller e vai responser as requisições
@RequestMapping("apis/tipo") //indica o caminho base para os endPoints da classe
public class TipoRestController
{
    @Autowired
    TipoService tipoService;
    @GetMapping
    public ResponseEntity<Object> getAll()
    {
        List<Tipo> tipoList;
        tipoList = tipoService.getAll();
        if(!tipoList.isEmpty()) //verificando se existe algum registro no banco de tipos
            return ResponseEntity.ok(tipoList);
        return ResponseEntity.badRequest().body(new Erro("Erro ao listar os tipos cadastrado"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTipoId(@PathVariable Long id)
    {
        //buscando pelo id, se caso ele não existir vira null
        Tipo tipo = tipoService.getById(id).orElse(null);
        if(tipo == null)
            return ResponseEntity.badRequest().body("Tipo não encontrado");
        else
            return ResponseEntity.ok(tipo);
    }

    @PostMapping
    public ResponseEntity<Object> addTipo(@RequestBody Tipo tipo)
    {
        try
        {
            Tipo novoTipo = tipoService.save(tipo);
            return ResponseEntity.ok(novoTipo);
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body("Erro ao adicionar um novo tipo");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delTipo(@PathVariable Long id)
    {
        Tipo tipo = tipoService.getById(id).orElse(null);
        if(tipo != null)
        {
            tipoService.delete(id); //deletando o id caso tenha acho ele
            return ResponseEntity.noContent().build(); //apenas retorna se deu certo a exclusão
        }
        else
            return ResponseEntity.badRequest().body("Erro ao deletar um tipo");
    }

    @PutMapping
    public ResponseEntity<Object> updateTipo(@RequestBody Tipo tipo)
    {
        Tipo novoTipo = null;
        try
        {
            Tipo tipoAlterado = tipoService.save(tipo);
            return ResponseEntity.ok(tipoAlterado);
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body("Erro ao alterar o tipo");
        }
    }
}
