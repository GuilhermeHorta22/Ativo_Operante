package unoeste.fipp.ativooperante_be.config; // Crie este pacote

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //.allowedOrigins("http://localhost:8080") // Permite apenas seu frontend
                .allowedOrigins("*") // OU use "*" para permitir qualquer origem (menos seguro para produção)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH") // Métodos permitidos
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}