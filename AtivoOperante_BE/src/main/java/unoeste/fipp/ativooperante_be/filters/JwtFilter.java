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
import org.springframework.stereotype.Component;

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

        String uri = httpRequest.getRequestURI();
        if (uri.contains("/acesso/autenticar") || uri.contains("/acesso/login") || uri.contains("/apis/usuario/cadastro") || uri.matches("/apis/denuncia/\\d+/imagem")){
            chain.doFilter(request, response);
            return;
        }

        String token = httpRequest.getHeader("Authorization");
        if (token != null && JWTTokenProvider.verifyToken(token)) {
            try {
                String email = JWTTokenProvider.getEmailFromToken(token);
                Usuario usuario = usuarioService.getByEmail(email);

                if (usuario != null) {
                    if (verificaNivel(token, request)) {
                        chain.doFilter(request, response);
                        return;
                    }
                    else {
                        ((HttpServletResponse) response).setStatus(500);
                        response.getOutputStream().write("Não autorizado".getBytes());
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro ao verificar usuário do token: " + e.getMessage());
            }
        }

        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("Acesso não autorizado. Token inválido ou ausente.");
    }


    private boolean verificaNivel(String token, ServletRequest request) {
        boolean isAuthorized = true;
        String nivel = JWTTokenProvider.getAllClaimsFromToken(token).get("nivel").toString();
        String rotaDestino = ((HttpServletRequest) request).getRequestURI();

        if (nivel.equals("2") && rotaDestino.contains("adm"))
            isAuthorized = false;

        return isAuthorized;
    }

}