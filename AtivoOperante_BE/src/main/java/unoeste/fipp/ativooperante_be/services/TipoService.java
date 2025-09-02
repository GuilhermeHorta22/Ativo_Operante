package unoeste.fipp.ativooperante_be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unoeste.fipp.ativooperante_be.entities.Tipo;
import unoeste.fipp.ativooperante_be.repositories.DenunciaRepository;
import unoeste.fipp.ativooperante_be.repositories.TipoRepository;

import java.util.List;
import java.util.Optional;


@Service
public class TipoService {
    @Autowired
    private TipoRepository tipoRepository;

    @Autowired
    private DenunciaRepository denunciaRepository;


    public List<Tipo> getAll() {
        return tipoRepository.findAll();
    }


    public Tipo getTipo(Long id) {
        return tipoRepository.findById(id).orElse(null);
    }


    public Tipo getByNome(String nome) {
        return tipoRepository.findByNome(nome);
    }


    @Transactional
    public Tipo save(Tipo tipo) {
        Tipo novoTipo;
        try {
            // Validação do nome
            if (tipo.getNome() == null || tipo.getNome().trim().isEmpty()) {
                return null; // Nome é obrigatório
            }

            // Verifica se já existe outro tipo com o mesmo nome (apenas para novos registros ou alteração de nome)
            if (tipo.getId() == null || tipo.getId() == 0) {
                Tipo tipoExistente = tipoRepository.findByNome(tipo.getNome().trim());
                if (tipoExistente != null) {
                    return null; // Já existe um tipo com este nome
                }
            } else {
                Optional<Tipo> tipoAtualOpt = tipoRepository.findById(tipo.getId());
                if (tipoAtualOpt.isPresent()) {
                    Tipo tipoAtual = tipoAtualOpt.get();
                    // Se estiver alterando o nome, verifica duplicidade
                    if (!tipoAtual.getNome().equals(tipo.getNome())) {
                        Tipo tipoExistente = tipoRepository.findByNome(tipo.getNome().trim());
                        if (tipoExistente != null && !tipoExistente.getId().equals(tipo.getId())) {
                            return null; // Já existe outro tipo com este nome
                        }
                    }
                }
            }

            // Remove espaços extras do nome
            tipo.setNome(tipo.getNome().trim());

            novoTipo = tipoRepository.save(tipo);
        } catch (Exception e) {
            e.printStackTrace();
            novoTipo = null;
        }
        return novoTipo;
    }


    @Transactional
    public boolean apagarTipo(Long id) {
        Tipo tipo = tipoRepository.findById(id).orElse(null);
        if (tipo != null) {
            if (verificarTipoEmUso(id)) {
                return false;
            }

            try {
                tipoRepository.delete(tipo);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }


    public boolean verificarTipoEmUso(Long id) {
        return denunciaRepository.countByTipoId(id) > 0;
    }


    public List<Object[]> getTiposMaisFrequentes() {
        return denunciaRepository.countDenunciasByTipo();
    }


    public long countTipos() {
        return tipoRepository.count();
    }


    public List<Tipo> getTiposNaoUtilizados() {
        return tipoRepository.findTiposNaoUtilizados();
    }


    public List<Tipo> buscarPorNomeParcial(String termoBusca) {
        return tipoRepository.findByNomeContainingIgnoreCase(termoBusca);
    }

    public boolean existeTipoComNome(String nome) {
        return tipoRepository.findByNome(nome.trim()) != null;
    }


    public boolean existeTipoComId(Long id) {
        return tipoRepository.existsById(id);
    }


    public List<Object[]> getDadosDashboardTipos() {

        return tipoRepository.getDadosDashboardTipos();
    }

    @Transactional
    public boolean atualizarNomeTipo(Long id, String novoNome) {
        if (novoNome == null || novoNome.trim().isEmpty()) {
            return false; // Nome não pode ser vazio
        }

        Tipo tipo = tipoRepository.findById(id).orElse(null);
        if (tipo == null) {
            return false;
        }

        Tipo tipoExistente = tipoRepository.findByNome(novoNome.trim());
        if (tipoExistente != null && !tipoExistente.getId().equals(id)) {
            return false;
        }

        try {
            tipo.setNome(novoNome.trim());
            tipoRepository.save(tipo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}