package unoeste.fipp.ativooperante_be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unoeste.fipp.ativooperante_be.entities.Usuario;
import unoeste.fipp.ativooperante_be.repositories.DenunciaRepository;
import unoeste.fipp.ativooperante_be.repositories.UsuarioRepository;
import unoeste.fipp.ativooperante_be.util.JWTTokenProvider;

import java.util.List;
import java.util.regex.Pattern;


@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DenunciaRepository denunciaRepository;


    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }


    public Usuario getId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario getByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }


    public Usuario getByCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf);
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        Usuario novoUsuario;
        try {
            if (!validarCamposObrigatorios(usuario)) {
                return null;
            }

            if (!isValidEmail(usuario.getEmail())) {
                return null;
            }

            if (!isValidCPF(usuario.getCpf())) {
                return null;
            }

            if (usuario.getId() == null || usuario.getId() == 0) {
                if (existeUsuarioComEmail(usuario.getEmail())) {
                    return null;
                }

                if (existeUsuarioComCPF(usuario.getCpf())) {
                    return null;
                }
            } else {
                Usuario usuarioAtual = usuarioRepository.findById(usuario.getId()).orElse(null);
                if (usuarioAtual == null) {
                    return null;
                }

                if (!usuarioAtual.getEmail().equals(usuario.getEmail())) {
                    Usuario outroUsuario = usuarioRepository.findByEmail(usuario.getEmail());
                    if (outroUsuario != null && !outroUsuario.getId().equals(usuario.getId())) {
                        return null;
                    }
                }

                if (!usuarioAtual.getCpf().equals(usuario.getCpf())) {
                    Usuario outroUsuario = usuarioRepository.findByCpf(usuario.getCpf());
                    if (outroUsuario != null && !outroUsuario.getId().equals(usuario.getId())) {
                        return null;
                    }
                }

                if (usuarioAtual.getEmail().equals("admin@pm.br") && usuario.getNivel() != 1) {
                    usuario.setNivel(1);
                }
            }

            usuario.setEmail(usuario.getEmail().trim());
            usuario.setCpf(usuario.getCpf().replaceAll("[^0-9]", ""));

            novoUsuario = usuarioRepository.save(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            novoUsuario = null;
        }
        return novoUsuario;
    }


    @Transactional
    public boolean apagar(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            if (usuario.getEmail().equals("admin@pm.br")) {
                return false;
            }

            if (usuarioPossuiDenuncias(id)) {
                return false;
            }

            try {
                usuarioRepository.delete(usuario);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }


    public Usuario autenticar(String email, String senha) {
        if (email == null || email.trim().isEmpty() ||
                senha == null || senha.trim().isEmpty()) {
            return null;
        }

        Usuario usuario = usuarioRepository.findByEmail(email.trim());
        if (usuario != null && usuario.getSenha().equals(senha)) {
            return usuario;
        }

        return null;
    }

    public boolean usuarioPossuiDenuncias(Long id) {
        return denunciaRepository.countByUsuarioId(id) > 0;
    }


    public boolean existeUsuarioComEmail(String email) {
        return usuarioRepository.findByEmail(email.trim()) != null;
    }


    public boolean existeUsuarioComCPF(String cpf) {
        // Remove caracteres não numéricos
        String cpfNumerico = cpf.replaceAll("[^0-9]", "");
        return usuarioRepository.findByCpf(cpfNumerico) != null;
    }

    public List<Usuario> getUsuariosPorNivel(int nivel) {
        return usuarioRepository.findByNivel(nivel);
    }


    @Transactional
    public boolean atualizarSenha(Long id, String senhaAtual, String novaSenha) {
        if (novaSenha == null || novaSenha.trim().isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return false;
        }

        if (!usuario.getSenha().equals(senhaAtual)) {
            return false;
        }

        try {
            usuario.setSenha(novaSenha);
            usuarioRepository.save(usuario);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Object[]> getEstatisticasUsuariosPorNivel() {
        return usuarioRepository.countUsuariosByNivel();
    }


    public List<Object[]> getEstatisticasDenunciasPorUsuario() {
        return usuarioRepository.countDenunciasByUsuario();
    }

    public long countUsuarios() {
        return usuarioRepository.count();
    }


    private boolean validarCamposObrigatorios(Usuario usuario) {
        return usuario.getCpf() != null && !usuario.getCpf().trim().isEmpty() &&
                usuario.getEmail() != null && !usuario.getEmail().trim().isEmpty() &&
                usuario.getSenha() != null && !usuario.getSenha().trim().isEmpty() &&
                (usuario.getNivel() == 1 || usuario.getNivel() == 2);
    }


    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }


    private boolean isValidCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }

        String cpfNumerico = cpf.replaceAll("[^0-9]", "");

        if (cpfNumerico.length() != 11) {
            return false;
        }

        boolean todosDigitosIguais = true;
        for (int i = 1; i < cpfNumerico.length(); i++) {
            if (cpfNumerico.charAt(i) != cpfNumerico.charAt(0)) {
                todosDigitosIguais = false;
                break;
            }
        }

        if (todosDigitosIguais) {
            return false;
        }


        return true;
    }


    public boolean isAdmin(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        return usuario != null && usuario.getNivel() == 1;
    }


    public List<Usuario> buscarUsuarios(String termo) {
        return usuarioRepository.findByEmailContainingIgnoreCaseOrCpfContaining(termo, termo);
    }

    @Transactional
    public void criarUsuarioAdminPadrao() {
        Usuario adminExistente = usuarioRepository.findByEmail("admin@pm.br");
        if (adminExistente == null) {
            Usuario admin = new Usuario();
            admin.setEmail("admin@pm.br");
            admin.setCpf("12111158963");
            admin.setSenha("123321");
            admin.setNivel(1);

            try {
                usuarioRepository.save(admin);
                System.out.println("Usuário administrador padrão criado com sucesso!");
            } catch (Exception e) {
                System.err.println("Erro ao criar usuário administrador padrão: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}