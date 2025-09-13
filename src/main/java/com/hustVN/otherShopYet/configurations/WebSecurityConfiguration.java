package com.hustVN.otherShopYet.configurations;

import com.hustVN.otherShopYet.components.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@RequiredArgsConstructor
@EnableWebMvc
@EnableWebSecurity
public class WebSecurityConfiguration {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers(
                                    String.format("%s/users/login", apiPrefix),
                                    String.format("%s/users/register", apiPrefix)
                            ).permitAll()
                            .requestMatchers(GET, String.format("%s/categories", apiPrefix)).permitAll()
                            .requestMatchers(GET, String.format("%s/categories/**", apiPrefix)).permitAll()
                            .requestMatchers(PUT, String.format("%s/categories/**", apiPrefix)).hasAnyRole("ADMIN")
                            .requestMatchers(POST, String.format("%s/categories/**", apiPrefix)).hasAnyRole("ADMIN")
                            .requestMatchers(DELETE, String.format("%s/categories/**", apiPrefix)).hasAnyRole("ADMIN")

                            .requestMatchers(GET, String.format("%s/products", apiPrefix)).permitAll()
                            .requestMatchers(GET, String.format("%s/products/**", apiPrefix)).permitAll()
                            .requestMatchers(PUT, String.format("%s/products/**", apiPrefix)).hasAnyRole("ADMIN")
                            .requestMatchers(POST, String.format("%s/products/**", apiPrefix)).hasAnyRole("ADMIN")
                            .requestMatchers(DELETE, String.format("%s/products/**", apiPrefix)).hasAnyRole("ADMIN")

                            .requestMatchers(GET, String.format("%s/orders?**", apiPrefix)).hasAnyRole("USER","ADMIN")
                            .requestMatchers(PUT, String.format("%s/orders/**", apiPrefix)).hasAnyRole("ADMIN")
                            .requestMatchers(POST, String.format("%s/orders/**", apiPrefix)).hasAnyRole("ADMIN")
                            .requestMatchers(DELETE, String.format("%s/orders/**", apiPrefix)).hasAnyRole("ADMIN")

                            .requestMatchers(GET, String.format("%s/order_details?**", apiPrefix)).hasAnyRole("USER","ADMIN")
                            .requestMatchers(PUT, String.format("%s/order_details/**", apiPrefix)).hasAnyRole("ADMIN")
                            .requestMatchers(POST, String.format("%s/order_details/**", apiPrefix)).hasAnyRole("ADMIN")
                            .requestMatchers(DELETE, String.format("%s/order_details/**", apiPrefix)).hasAnyRole("ADMIN")

                            .requestMatchers(POST, String.format("%s/products/upload/**", apiPrefix)).hasAnyRole("ADMIN")
                            .anyRequest().authenticated();
                });
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(List.of("*"));
                corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                corsConfiguration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "x-auth-token", "Accept", "Access-Control-Request-Headers"));
                corsConfiguration.setExposedHeaders(Arrays.asList("x-auth-token", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", corsConfiguration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return http.build();
    }
}