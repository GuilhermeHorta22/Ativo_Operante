package unoeste.fipp.ativooperante_be.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unoeste.fipp.ativooperante_be.entities.Denuncia;
import unoeste.fipp.ativooperante_be.entities.DenunciaImagem;
import unoeste.fipp.ativooperante_be.entities.Erro;
import unoeste.fipp.ativooperante_be.entities.FeedBack;
import unoeste.fipp.ativooperante_be.services.DenunciaService;
import unoeste.fipp.ativooperante_be.services.ImagemService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("apis/denuncia")
public class DenunciaRestController {
    @Autowired
    private DenunciaService denunciaService;

    @Autowired
    private ImagemService imagemService;

    @GetMapping("/adm")
    public ResponseEntity<Object> getAll() {
        List<Denuncia> denunciaList;
        denunciaList = denunciaService.getAll();
        if (!denunciaList.isEmpty())
            return ResponseEntity.ok(denunciaList);
        else
            return ResponseEntity.badRequest().body(new Erro("Nenhuma Denuncia cadastrada"));
    }


    @PostMapping("/adm/feedback")
    public ResponseEntity<Object> addFeedBack(@RequestBody FeedBack feedback) {
        if (denunciaService.addFeedBack(feedback))
            return ResponseEntity.ok().body(feedback);
        else
            return ResponseEntity.badRequest().body(new Erro("Não foi possível adicionar o feedback"));
    }
    

    @GetMapping("/usuario/{usuId}")
    public ResponseEntity<Object> getPorUsuario(@PathVariable(value = "usuId") Long usuId) {
        List<Denuncia> denunciaList;
        denunciaList = denunciaService.getPorUsuario(usuId);
        if (!denunciaList.isEmpty())
            return ResponseEntity.ok(denunciaList);
        else
            return ResponseEntity.ok().body(new Erro("Usuário não tem nenhuma denúncia"));
    }


    @GetMapping("/feedback/usuario/{usuId}")
    public ResponseEntity<Object> getFeedbacksPorUsuario(@PathVariable(value = "usuId") Long usuId) {
        List<FeedBack> feedbackList = denunciaService.getFeedbacksPorUsuario(usuId);
        if (!feedbackList.isEmpty())
            return ResponseEntity.ok(feedbackList);
        else
            return ResponseEntity.ok().body(new Erro("Usuário não tem nenhum feedback"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDenuncia(@PathVariable(value = "id") Long id) {
        Denuncia denuncia = denunciaService.getDenuncia(id);
        if (denuncia != null)
            return ResponseEntity.ok(denuncia);
        else
            return ResponseEntity.badRequest().body(new Erro("Erro ou ID não encontrado"));
    }


    @PostMapping
    public ResponseEntity<Object> addDenuncia(@RequestBody Denuncia denuncia) {
        Denuncia novaDenuncia = denunciaService.save(denuncia);
        if (novaDenuncia != null)
            return ResponseEntity.ok(novaDenuncia);
        else
            return ResponseEntity.badRequest().body(new Erro("Erro ao cadastrar denúncia"));
    }


    @PutMapping
    public ResponseEntity<Object> uptDenuncia(@RequestBody Denuncia denuncia) {
        Denuncia novaDenuncia = denunciaService.save(denuncia);
        if (novaDenuncia != null)
            return ResponseEntity.ok(novaDenuncia);
        else
            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar denúncia"));
    }


    @DeleteMapping("/adm/{id}")
    public ResponseEntity<Object> deletDenuncia(@PathVariable(value = "id") Long id) {
        if (denunciaService.apagar(id))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.badRequest().body(new Erro("Denúncia não encontrada/Erro ao apagar denúncia"));
    }

    @PostMapping("/{id}/imagem")
    public ResponseEntity<Object> addImagem(@PathVariable Long id, @RequestParam("arquivo") MultipartFile arquivo) {
        try {
            DenunciaImagem novaImagem = imagemService.salvarImagem(arquivo, id);
            if (novaImagem != null) {
                return ResponseEntity.ok().body(new Erro("Imagem adicionada com sucesso"));
            } else {
                return ResponseEntity.badRequest().body(new Erro("Erro ao salvar imagem. Verifique o console para mais detalhes."));
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Erro("Erro ao processar imagem: " + e.getMessage()));
        } catch (Exception e) { // Catch any other exceptions from the service
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Erro("Erro inesperado ao salvar imagem: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> getImagem(@PathVariable Long id) {
        DenunciaImagem imagem = imagemService.buscarImagemPorDenuncia(id);
        if (imagem != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(imagem.getTipo()));
            headers.setContentDispositionFormData("attachment", imagem.getNome());

            return new ResponseEntity<>(imagem.getDados(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/tem-imagem")
    public ResponseEntity<Object> temImagem(@PathVariable Long id) {
        boolean temImagem = imagemService.denunciaPossuiImagem(id);
        return ResponseEntity.ok().body(temImagem);
    }
}