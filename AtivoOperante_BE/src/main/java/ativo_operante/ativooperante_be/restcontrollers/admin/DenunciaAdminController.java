package ativo_operante.ativooperante_be.restcontrollers.admin;

import ativo_operante.ativooperante_be.entities.Denuncia;
import ativo_operante.ativooperante_be.entities.Erro;
import ativo_operante.ativooperante_be.entities.Feedback;
import ativo_operante.ativooperante_be.service.DenunciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("apis/admin/denuncia")
public class DenunciaAdminController
{
    @Autowired
    DenunciaService denunciaService;

    //listando todas as denuncias para retornar
    @GetMapping
    public ResponseEntity<Object> getAll()
    {
        List<Denuncia> denunciaList;
        denunciaList = denunciaService.getAll(); //colocando todas as denuncias dentro da lista
        if(!denunciaList.isEmpty())
            return ResponseEntity.ok(denunciaList);
        return ResponseEntity.badRequest().body(new Erro("Erro ao listar as denuncias"));
    }

    //buscando um id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id)
    {
        Denuncia denuncia = denunciaService.getById(id).orElse(null);
        if(denuncia != null)
            return ResponseEntity.ok(denuncia);
        return ResponseEntity.badRequest().body("Não foi encontrado o id!");
    }

    //adicionando uma nova denuncia
    @PostMapping
    public ResponseEntity<Object> addDenuncia(@RequestBody Denuncia denuncia)
    {
        try
        {
            Denuncia novaDenuncia = denunciaService.save(denuncia);
            return ResponseEntity.ok(novaDenuncia);
        }
        catch(Exception e)
        {
            e.printStackTrace(); // Para ver o erro exato no console
            return ResponseEntity.badRequest().body("Erro ao adicionar a nova denúncia: " + e.getMessage());
        }
    }


    //deletando uma denuncia
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delDenuncia(@PathVariable Long id)
    {
        Denuncia denuncia = denunciaService.getById(id).orElse(null);
        if(denuncia != null)
        {
            denunciaService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().body("Erro ao deletar uma denuncia");
    }

    //alterando uma denuncia já existente
    @PutMapping
    public ResponseEntity<Object> updateDenuncia(@RequestBody Denuncia denuncia)
    {
        try
        {
            Denuncia denunciaAlterada = denunciaService.update(denuncia);
            return ResponseEntity.ok(denunciaAlterada);
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body("Erro ao alterar a denuncia");
        }
    }

    @GetMapping("admin/add-feedback/{id}/{texto}")
    public ResponseEntity<Object> addFeedBack(@PathVariable Long id, @PathVariable String texto)
    {
        if(denunciaService.addFeedBack(new Feedback(id,texto)))

            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.badRequest().body("Não foi possível adicionar o feedback");
        
    }

    @GetMapping("usuario/{id}")
    public ResponseEntity<Object> getAllByUsuario(@PathVariable Long id)
    {
        List<Denuncia> denunciaList;
        denunciaList=denunciaService.getAllByUsuario(id);
        if (!denunciaList.isEmpty())
            return ResponseEntity.ok(denunciaList);
        else
            return ResponseEntity.badRequest().body(
                    new Erro("Nenhuma denuncia cadastrada para esse usuário"));
    }
}
