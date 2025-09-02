package unoeste.fipp.ativooperante_be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unoeste.fipp.ativooperante_be.entities.Denuncia;
import unoeste.fipp.ativooperante_be.entities.FeedBack;
import unoeste.fipp.ativooperante_be.entities.Usuario;
import unoeste.fipp.ativooperante_be.repositories.DenunciaImagemRepository;
import unoeste.fipp.ativooperante_be.repositories.DenunciaRepository;
import unoeste.fipp.ativooperante_be.repositories.FeedBackRepository;
import unoeste.fipp.ativooperante_be.repositories.UsuarioRepository;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class DenunciaService {
    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DenunciaImagemRepository denunciaImagemRepository;


    public List<Denuncia> getAll() {
        return denunciaRepository.findAll();
    }


    public Denuncia getDenuncia(Long id) {
        return denunciaRepository.findById(id).orElse(null);
    }

    @Transactional
    public Denuncia save(Denuncia denuncia) {
        Denuncia novaDenuncia;
        try {
            if (denuncia.getId() == null || denuncia.getId() == 0) {
                denuncia.setData(LocalDate.now());
            }

            novaDenuncia = denunciaRepository.save(denuncia);
        } catch (Exception e) {
            novaDenuncia = null;
            e.printStackTrace();
        }
        return novaDenuncia;
    }


    @Transactional
    public boolean apagar(Long id) {
        Denuncia denuncia = denunciaRepository.findById(id).orElse(null);
        if (denuncia != null) {
            try {
                if (denunciaImagemRepository.existsByDenunciaId(id)) {
                    denunciaImagemRepository.deleteByDenunciaId(id);
                }

                if (denuncia.getFeedBack() != null) {
                    feedBackRepository.delete(denuncia.getFeedBack());
                }

                denunciaRepository.delete(denuncia);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }


    @Transactional
    public boolean addFeedBack(FeedBack feedBack) {
        try {
            // Verifica se a denúncia existe
            Optional<Denuncia> denunciaOpt = denunciaRepository.findById(feedBack.getDenuncia().getId());
            if (!denunciaOpt.isPresent()) {
                return false;
            }

            Denuncia denuncia = denunciaOpt.get();

            if (denuncia.getFeedBack() != null) {
                // Atualiza o feedback existente
                FeedBack feedBackExistente = denuncia.getFeedBack();
                feedBackExistente.setTexto(feedBack.getTexto());
                feedBackRepository.save(feedBackExistente);
            } else {
                feedBack.setDenuncia(denuncia);
                FeedBack novoFeedBack = feedBackRepository.save(feedBack);
                denuncia.setFeedBack(novoFeedBack);
                denunciaRepository.save(denuncia);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Transactional
    public boolean addFeedBack(Long denunciaId, String texto) {
        try {
            Optional<Denuncia> denunciaOpt = denunciaRepository.findById(denunciaId);
            if (!denunciaOpt.isPresent()) {
                return false; // Denúncia não encontrada
            }

            Denuncia denuncia = denunciaOpt.get();

            if (denuncia.getFeedBack() != null) {
                FeedBack feedBackExistente = denuncia.getFeedBack();
                feedBackExistente.setTexto(texto);
                feedBackRepository.save(feedBackExistente);
            } else {

                FeedBack feedBack = new FeedBack();
                feedBack.setTexto(texto);
                feedBack.setDenuncia(denuncia);
                FeedBack novoFeedBack = feedBackRepository.save(feedBack);
                denuncia.setFeedBack(novoFeedBack);
                denunciaRepository.save(denuncia);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Denuncia> getPorUsuario(Long usuId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuId);
        if (!usuarioOpt.isPresent()) {
            return new ArrayList<>();
        }

        return denunciaRepository.findByUsuarioId(usuId);
    }


    public List<FeedBack> getFeedbacksPorUsuario(Long usuId) {
        return feedBackRepository.findFeedbacksByUsuarioId(usuId);
    }


    public boolean existeDenunciasParaTipo(Long tipoId) {
        return denunciaRepository.countByTipoId(tipoId) > 0;
    }


    public boolean existeDenunciasParaOrgao(Long orgaoId) {
        return denunciaRepository.countByOrgaoId(orgaoId) > 0;
    }


    public List<Denuncia> getDenunciasPorPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        return denunciaRepository.findByDataBetween(dataInicial, dataFinal);
    }


    public List<Denuncia> getDenunciasPorUrgencia(int urgencia) {
        return denunciaRepository.findByUrgencia(urgencia);
    }


    public List<Object[]> getEstatisticasPorTipo() {
        return denunciaRepository.countDenunciasByTipo();
    }

    public List<Object[]> getEstatisticasPorOrgao() {
        return denunciaRepository.countDenunciasByOrgao();
    }


    public boolean temFeedback(Long denunciaId) {
        Denuncia denuncia = denunciaRepository.findById(denunciaId).orElse(null);
        return denuncia != null && denuncia.getFeedBack() != null;
    }

    public List<Denuncia> getDenunciasPorTipo(Long tipoId) {
        return denunciaRepository.findByTipoId(tipoId);
    }


    public List<Denuncia> getDenunciasPorOrgao(Long orgaoId) {
        return denunciaRepository.findByOrgaoId(orgaoId);
    }


    public List<Denuncia> getDenunciasSemFeedback() {
        return denunciaRepository.findDenunciasSemFeedback();
    }


    public long countDenuncias() {
        return denunciaRepository.count();
    }


    public long countDenunciasPorUsuario(Long usuId) {
        return denunciaRepository.countByUsuarioId(usuId);
    }
}