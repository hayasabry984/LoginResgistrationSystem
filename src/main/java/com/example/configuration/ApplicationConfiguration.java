package com.example.configuration;

import com.example.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//this class configures spring security's authentication mechanism using a database-backed user store for secure user login (UserDetailsService, BCrypt, AuthenticationManager, AuthenticationProvider)
@Configuration
public class ApplicationConfiguration {
    private final UserRepository userRepository;

    public ApplicationConfiguration(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Bean
    UserDetailsService userDetailsService(){ //loads users from the database by email for authentication and throws exception if the user doesn't exist
        return username->userRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() { //returns password encoder that uses BCrypt to hash passwords for protection
        return new BCryptPasswordEncoder();
    }

    //setup authentication process via spring security:
    //handle authentication requests (user credentials validation) and delegates authentication requests to the configured authenticationProvider
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //DaoAuthenticationProvider combine userDetailsService (for fetching users) and passwordEncoder (for password checking) for authentication to verify user credentials
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}