package com.demo.shoppingproject.Config;


import com.demo.shoppingproject.Security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity  //Annotation for enabling Web Security
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    //Use Bcrypt password encoder from Spring Security to hash the password
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable form login
                .formLogin(AbstractHttpConfigurer::disable)

                //Disable basic http authentication
                .httpBasic(AbstractHttpConfigurer::disable)

                /* Disable CSRF (Cross-site request forgery) ATTACK , because (1) JWT is not using Session; JWT is stateless */
                .csrf(AbstractHttpConfigurer::disable)

                //Configure Authorization rules, HTTP request

                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/","/register").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/image").permitAll()
                        .anyRequest()
                        .authenticated())





                //Disable Session; Session management to STATELESS, because JWT is using stateless status
                .sessionManagement( management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
