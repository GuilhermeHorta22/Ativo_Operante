package unoeste.fipp.ativooperante_be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unoeste.fipp.ativooperante_be.entities.Orgaos;
import unoeste.fipp.ativooperante_be.repositories.DenunciaRepository;
import unoeste.fipp.ativooperante_be.repositories.OrgaosRepository;

import java.util.List;
import java.util.Optional;


@Service
public class OrgaosService {
    @Autowired
    private OrgaosRepository orgaosRepository;

    @Autowired
    private DenunciaRepository denunciaRepository;


    public List<Orgaos> getAll() {
        return orgaosRepository.findAll();
    }


    public Orgaos getId(Long id) {
        return orgaosRepository.findById(id).orElse(null);
    }


    public Orgaos getByNome(String nome) {
        return orgaosRepository.findByNome(nome);
    }

    @Transactional
    public Orgaos save(Orgaos orgao) {
        Orgaos novoOrgao;
        try {
            if (orgao.getNome() == null || orgao.getNome().trim().isEmpty()) {
                return null;
            }

            if (orgao.getId() == null || orgao.getId() == 0) {
                Orgaos orgaoExistente = orgaosRepository.findByNome(orgao.getNome().trim());
                if (orgaoExistente != null) {
                    return null;
                }
            } else {
                Optional<Orgaos> orgaoAtualOpt = orgaosRepository.findById(orgao.getId());
                if (orgaoAtualOpt.isPresent()) {
                    Orgaos orgaoAtual = orgaoAtualOpt.get();
                    if (!orgaoAtual.getNome().equals(orgao.getNome())) {
                        Orgaos orgaoExistente = orgaosRepository.findByNome(orgao.getNome().trim());
                        if (orgaoExistente != null && !orgaoExistente.getId().equals(orgao.getId())) {
                            return null;
                        }
                    }
                }
            }

            // Remove espaços extras do nome
            orgao.setNome(orgao.getNome().trim());

            novoOrgao = orgaosRepository.save(orgao);
        } catch (Exception e) {
            e.printStackTrace();
            novoOrgao = null;
        }
        return novoOrgao;
    }


    @Transactional
    public boolean apagarOrgao(Long id) {
        Orgaos orgao = orgaosRepository.findById(id).orElse(null);
        if (orgao != null) {
            if (verificarOrgaoEmUso(id)) {
                return false;
            }

            try {
                orgaosRepository.delete(orgao);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean verificarOrgaoEmUso(Long id) {
        return denunciaRepository.countByOrgaoId(id) > 0;
    }


    public List<Object[]> getEstatisticasUsoOrgaos() {
        return denunciaRepository.countDenunciasByOrgao();
    }


    public long countOrgaos() {
        return orgaosRepository.count();
    }


    public List<Orgaos> getOrgaosNaoUtilizados() {
        return orgaosRepository.findOrgaosNaoUtilizados();
    }


    public List<Orgaos> buscarPorNomeParcial(String termoBusca) {
        return orgaosRepository.findByNomeContainingIgnoreCase(termoBusca);
    }


    public boolean existeOrgaoComNome(String nome) {
        return orgaosRepository.findByNome(nome.trim()) != null;
    }


    public boolean existeOrgaoComId(Long id) {
        return orgaosRepository.existsById(id);
    }
}