package ativo_operante.ativooperante_be.restcontrollers.cidadao;

import ativo_operante.ativooperante_be.entities.Denuncia;
import ativo_operante.ativooperante_be.entities.Erro;
import ativo_operante.ativooperante_be.entities.Feedback;
import ativo_operante.ativooperante_be.service.DenunciaService;
import ativo_operante.ativooperante_be.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("apis/cidadao/denuncia")
public class DenunciaCidadaoController
{
    @Autowired
    DenunciaService denunciaService;

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


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delDenuncia(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        Long userId = JwtUtil.getIdFromToken(token.replace("Bearer ", ""));

        Optional<Denuncia> denunciaOpt = denunciaService.getById(id);
        if (denunciaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Denúncia não encontrada");
        }

        Denuncia denuncia = denunciaOpt.get();
        if (!denuncia.getUsuario().getId().equals(userId)) {
            return ResponseEntity.status(403).body("Você não tem permissão para excluir essa denúncia");
        }

        denunciaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Object> updateDenuncia(@RequestBody Denuncia denuncia, @RequestHeader("Authorization") String token) {
        Long userId = JwtUtil.getIdFromToken(token.replace("Bearer ", ""));

        Optional<Denuncia> denunciaOpt = denunciaService.getById(denuncia.getId());
        if (denunciaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Denúncia não encontrada");
        }

        if (!denunciaOpt.get().getUsuario().getId().equals(userId)) {
            return ResponseEntity.status(403).body("Você não tem permissão para alterar essa denúncia");
        }

        try {
            Denuncia denunciaAlterada = denunciaService.update(denuncia);
            return ResponseEntity.ok(denunciaAlterada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao alterar a denúncia");
        }
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
