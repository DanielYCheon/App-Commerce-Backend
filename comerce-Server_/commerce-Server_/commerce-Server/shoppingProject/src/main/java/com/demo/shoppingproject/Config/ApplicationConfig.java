package com.demo.shoppingproject.Config;
import com.demo.shoppingproject.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    //Instance for bring User Data from the database
    private final UserRepository userRepository;

    @Bean
    //Retrieve user details from the database, when user attempts to authenticate.
    public UserDetailsService userDetailsService(){
        //using Lamda expression to implement the UserDetailsService interface
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }

        @Bean
        //AuthenticationProvider is access from Database and response user details and encoded password
    public AuthenticationProvider authenticationProvider(){
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            //getting user details from the database
            authenticationProvider.setUserDetailsService(userDetailsService());
            //encoding the password
            authenticationProvider.setPasswordEncoder(passwordEncoder());
            return authenticationProvider;
    }
    @Bean
    //Provide authentication manager
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    //Use Bcrypt password encoder from Spring Security to hash the password
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}


