package com.behalf.delta.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Value("setting.frontend-url")
    private String frontendUrl;

    private final CustomOAuth2SuccessHandler successHandler;

    public SecurityConfig(CustomOAuth2SuccessHandler successHandler) {
        this.successHandler = successHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/quests/fetch").permitAll()
                        .requestMatchers("/public/**", "/login/**").permitAll() // Public endpoints
                        .requestMatchers("/api/quests/create").permitAll()
                        .requestMatchers("/api/quests/agreement").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated()              // Secure all other endpoints
                ).oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google")
                        .successHandler(this.successHandler)).csrf(AbstractHttpConfigurer::disable);


        return http.build();
    }

    @Bean
    public CorsFilter corsFilter(){
//        registry.addMapping("/**").allowedOrigins("http://localhost:3000").allowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");  // Allow all origins for local development
        config.addAllowedMethod("GET");  // Allow all HTTP methods (GET, POST, etc.)
        config.addAllowedMethod("POST");  // Allow all HTTP methods (GET, POST, etc.)
        config.addAllowedHeader("*");  // Allow all headers
        config.addAllowedMethod("OPTIONS");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }



}
