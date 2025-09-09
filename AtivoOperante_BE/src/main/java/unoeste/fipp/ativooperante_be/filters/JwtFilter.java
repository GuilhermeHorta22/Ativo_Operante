package unoeste.fipp.ativooperante_be.filters;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import unoeste.fipp.ativooperante_be.entities.Erro;
import unoeste.fipp.ativooperante_be.entities.Usuario;
import unoeste.fipp.ativooperante_be.services.UsuarioService;
import unoeste.fipp.ativooperante_be.util.JWTTokenProvider;

@Component
public class JwtFilter implements Filter {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Rotas públicas que não precisam de autenticação
        String uri = httpRequest.getRequestURI();
        if (uri.contains("/acesso/autenticar") || uri.contains("/acesso/login") || uri.contains("/apis/usuario/cadastro") || uri.matches("/apis/denuncia/\\d+/imagem")){
            chain.doFilter(request, response);
            return;
        }

        String token = httpRequest.getHeader("Authorization");
        
        // Se o token for nulo, a requisição não está autorizada
        if (token == null || !JWTTokenProvider.verifyToken(token)) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Acesso não autorizado. Token inválido ou ausente.");
            return;
        }

        try {
            String email = JWTTokenProvider.getEmailFromToken(token);
            Usuario usuario = usuarioService.getByEmail(email);

            if (usuario != null) {
                if (verificaNivel(token, request)) {
                    chain.doFilter(request, response);
                    return;
                } else {
                    sendErrorResponse(httpResponse, HttpServletResponse.SC_FORBIDDEN, "Acesso negado. Você não tem permissão para esta ação.");
                    return;
                }
            } else {
                 sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Usuário do token não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar usuário do token: " + e.getMessage());
            sendErrorResponse(httpResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro interno ao processar token.");
        }
    }

    private boolean verificaNivel(String token, ServletRequest request) {
        String nivel = JWTTokenProvider.getNivelFromToken(token);
        String rotaDestino = ((HttpServletRequest) request).getRequestURI();

        // Nível 1 é administrador, Nível 2 é cidadão
        if (nivel.equals("2") && rotaDestino.contains("adm")) {
            return false; // Cidadão não pode acessar rotas de admin
        }
        
        // Se for admin (nível 1), ou se for cidadão acessando rotas que não são de admin, o acesso é autorizado.
        return true;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        // Usar ObjectMapper para serializar o objeto de erro para JSON
        ObjectMapper mapper = new ObjectMapper();
        Erro erro = new Erro(message);
        String jsonError = mapper.writeValueAsString(erro);
        
        response.getWriter().write(jsonError);
    }
}