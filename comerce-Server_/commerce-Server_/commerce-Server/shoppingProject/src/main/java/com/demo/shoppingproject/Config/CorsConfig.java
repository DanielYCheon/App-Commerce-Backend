package com.demo.shoppingproject.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* configure the CORS policy for the application,
prevent the browser from blocking requests between different origins 8080 and 3000
 */
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(
                    @NonNull CorsRegistry registry){
                registry.addMapping("/**")
                        //Allowing the request from the frontend port:3000
                        .allowedOrigins("http://localhost:3000")
                        //Response Header with the credentials, cookies,
                        //Example, if .allowCredentials(false), then cookies will not be sent
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }

        };
    }
}
