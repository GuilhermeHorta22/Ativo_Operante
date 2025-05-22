package ativo_operante.ativooperante_be.util;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        if(token!=null && JWTTokenProvider.verifyToken(token)) {
            String nivel=JWTTokenProvider.getAllClaimsFromToken(token).get("nivel").toString();
            String rotaDestino=(((HttpServletRequest) request).getRequestURI());
            if((rotaDestino.contains("admin") && nivel.equals("1"))|| (rotaDestino.contains("cidadao") && nivel.equals("2")))
                chain.doFilter(request, response);
        }
        else {
            ((HttpServletResponse) response).setStatus(500);
            response.getOutputStream().write("Não autorizado ".getBytes());
        }
    }
    private boolean verificaNivel(String token, ServletRequest request){
        boolean isAuthorized = true;
        String nivel = JWTTokenProvider.getAllClaimsFromToken(token).get("nivel").toString();
        String rotaDestino=(((HttpServletRequest)request).getRequestURI());
        //coloque aqui as excesses para um determinado usuario
        if(nivel.equals("2")&&rotaDestino.contains("admin"))//use match para análises mais detalhadas
            isAuthorized = false; //usuario do nivel 2 nao pode acessar rotas que possuem "adm"
        return isAuthorized;
    }
}
