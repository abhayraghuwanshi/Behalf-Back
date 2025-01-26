package com.behalf.delta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable().headers().frameOptions().sameOrigin().and().
//        // Disable CSRF for testing purposes. Remove this in production.
//                cors().and() // Enable CORS configuration
//                .authorizeHttpRequests().anyRequest().permitAll(); // Allow all requests
//        return http.build();
//    }
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOriginPattern("*"); // Allow all origins
//        config.addAllowedHeader("*"); // Allow all headers
//        config.addAllowedMethod("*"); // Allow all methods (GET, POST, PUT, DELETE, etc.)
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("public/**").permitAll()  // Public endpoints
                        .anyRequest().authenticated()              // Secure all other endpoints
                ).oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google")); // Custom login page (if required)


        return http.build();
    }



}
