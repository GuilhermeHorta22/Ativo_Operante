package unoeste.fipp.ativooperante_be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import unoeste.fipp.ativooperante_be.entities.Denuncia;
import unoeste.fipp.ativooperante_be.entities.DenunciaImagem;
import unoeste.fipp.ativooperante_be.repositories.DenunciaImagemRepository;
import unoeste.fipp.ativooperante_be.repositories.DenunciaRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
public class ImagemService {

    @Autowired
    private DenunciaImagemRepository denunciaImagemRepository;

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Transactional
    public DenunciaImagem salvarImagem(DenunciaImagem imagem) {
        try {
            return denunciaImagemRepository.save(imagem);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Transactional
    public DenunciaImagem salvarImagem(MultipartFile arquivo, Long denunciaId) throws IOException {
        Optional<Denuncia> denunciaOpt = denunciaRepository.findById(denunciaId);
        if (!denunciaOpt.isPresent()) {
            System.out.println("ImagemService: Denuncia not found with ID: " + denunciaId);
            return null;
        }

        Denuncia denuncia = denunciaOpt.get();

        DenunciaImagem imagemExistente = denunciaImagemRepository.findByDenunciaId(denunciaId);

        DenunciaImagem imagem;
        if (imagemExistente != null) {
            imagem = imagemExistente;
            System.out.println("ImagemService: Updating existing image for Denuncia ID: " + denunciaId);
        } else {
            imagem = new DenunciaImagem();
            imagem.setDenuncia(denuncia);
            System.out.println("ImagemService: Creating new image for Denuncia ID: " + denunciaId);
        }

        imagem.setNome(arquivo.getOriginalFilename());
        imagem.setTipo(arquivo.getContentType());
        imagem.setTamanho(arquivo.getSize());
        imagem.setDados(arquivo.getBytes());

        // Add logging here to confirm types before saving
        System.out.println("ImagemService: Image Data (bytes): " + (imagem.getDados() != null ? imagem.getDados().length : "null") + " bytes. Type: " + (imagem.getDados() != null ? imagem.getDados().getClass().getName() : "null"));
        System.out.println("ImagemService: Image Size (Long): " + imagem.getTamanho() + ". Type: " + (imagem.getTamanho() != null ? imagem.getTamanho().getClass().getName() : "null"));


        try {
            return denunciaImagemRepository.save(imagem);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public DenunciaImagem buscarImagem(Long id) {
        return denunciaImagemRepository.findById(id).orElse(null);
    }


    public DenunciaImagem buscarImagemPorDenuncia(Long denunciaId) {
        return denunciaImagemRepository.findByDenunciaId(denunciaId);
    }


    @Transactional
    public boolean excluirImagem(Long id) {
        try {
            DenunciaImagem imagem = denunciaImagemRepository.findById(id).orElse(null);
            if (imagem != null) {
                denunciaImagemRepository.delete(imagem);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Transactional
    public boolean excluirImagemPorDenuncia(Long denunciaId) {
        try {
            DenunciaImagem imagem = denunciaImagemRepository.findByDenunciaId(denunciaId);
            if (imagem != null) {
                denunciaImagemRepository.delete(imagem);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean denunciaPossuiImagem(Long denunciaId) {
        return denunciaImagemRepository.existsByDenunciaId(denunciaId);
    }


    public List<DenunciaImagem> buscarTodasImagens() {
        return denunciaImagemRepository.findAll();
    }


    public long contarDenunciasComImagens() {
        return denunciaImagemRepository.count();
    }


    public boolean validarImagem(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            return false;
        }

        // Verifica tamanho máximo (5MB)
        long tamanhoMaximo = 5 * 1024 * 1024; // 5MB em bytes
        if (arquivo.getSize() > tamanhoMaximo) {
            return false;
        }

        // Verifica tipos permitidos
        String contentType = arquivo.getContentType();
        if (contentType == null) {
            return false;
        }

        // Lista de tipos MIME de imagens permitidos
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/bmp");
    }


    public List<Object[]> getEstatisticasImagensPorTipo() {
        return denunciaImagemRepository.countImagensPorTipo();
    }


    public List<Object[]> getEstatisticasImagensPorOrgao() {
        return denunciaImagemRepository.countImagensPorOrgao();
    }


    public List<DenunciaImagem> getImagensDenunciasUrgentes() {
        return denunciaImagemRepository.findImagensDenunciasUrgentes();
    }

}