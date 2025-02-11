package com.behalf.delta.config;

import jakarta.servlet.http.HttpServletResponse;
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

    private final AppProperties appProperties;


    private final CustomOAuth2SuccessHandler successHandler;

    public SecurityConfig(AppProperties appProperties, CustomOAuth2SuccessHandler successHandler) {
        this.appProperties = appProperties;
        this.successHandler = successHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/quests/fetch").permitAll()
                        .requestMatchers("/api/quests/recommend").permitAll()
                        .requestMatchers("/api/quests/detail").permitAll()
                        .requestMatchers("/api/store/products").permitAll()
                        .requestMatchers("/public/**", "/login/**").permitAll() // Public endpoints
                        .requestMatchers("/api/user/info").permitAll()
                        .requestMatchers("/api/v1/document/*/file/*").permitAll()
                        .requestMatchers("/api/v1/document/*/file").authenticated()
                        .anyRequest().authenticated()              // Secure all other endpoints
                ).oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google")
                        .successHandler(this.successHandler)).csrf(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/api/logout")  // ✅ Ensures backend logout is triggered
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")  // ✅ Clears cookies
                );;


        return http.build();
    }

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        if (appProperties.getFrontendUrl() == null || appProperties.getFrontendUrl().isBlank()) {
            throw new IllegalStateException("Frontend URL cannot be null or empty!");
        }
        config.addAllowedOrigin(appProperties.getFrontendUrl());
        config.addAllowedMethod("GET");  // Allow all HTTP methods (GET, POST, etc.)
        config.addAllowedMethod("POST");  // Allow all HTTP methods (GET, POST, etc.)
        config.addAllowedHeader("*");  // Allow all headers
        config.addAllowedMethod("OPTIONS");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }



}
