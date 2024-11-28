package LIVTech.authentication.Authentication_memory_dump.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class CorsConfig implements WebMvcConfigurer {

    @Value("${frontend.url}")
    private String frontendUrl;


    /**
     * Configures CORS settings to allow requests from the frontend.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Apply CORS to all endpoints
                        .allowedOrigins(frontendUrl) // Allow only requests from the frontend URL
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Add supported methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow cookies or authentication tokens
            }
        };
    }
}
