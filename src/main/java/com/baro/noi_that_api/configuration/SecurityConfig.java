package com.baro.noi_that_api.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // cho phép dùng @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
    // 👉 các API public (không cần login)
    private final String[] PUBLIC_ENDPOINTS = {
            "/auth/**"
    };

    private final CustomJwtDecoder customJwtDecoder;
    private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

//        config.setAllowedOriginPatterns(List.of(
//                "http://localhost:3000",
//                "http://localhost:5173"
//        ));

        config.setAllowedOriginPatterns(List.of("*"));   // Tạm thời cho dev, sau đổi thành localhost cụ thể

        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // ===================== PUBLIC - CUSTOMER AUTH =====================
                        .requestMatchers(HttpMethod.POST,
                                "/api/internal/customers/register",
                                "/api/internal/customers/verify",
                                "/api/internal/customers/google"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/api/internal/customers/email/**"
                        ).permitAll()

                        // ===================== PUBLIC - STAFF AUTH =====================
                        .requestMatchers(HttpMethod.POST,
                                "/api/internal/staff/verify"
                        ).permitAll()

                        // ===================== ADMIN + STAFF =====================
                        .requestMatchers(
                                "/api/internal/orders/**",
                                "/api/internal/order-details/**",
                                "/api/internal/payments/**",
                                "/api/internal/promotions/**",
                                "/api/internal/category-promotions/**"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")

                        .requestMatchers(
                                "/api/internal/customers/**"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")

                        // ===================== ADMIN ONLY =====================
                        .requestMatchers(HttpMethod.POST,
                                "/api/internal/staff"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/internal/staff/*/status"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/internal/staff/**"
                        ).hasAuthority("ROLE_ADMIN")

                        // Staff tự xem/sửa profile của mình
                        .requestMatchers(HttpMethod.GET,
                                "/api/internal/staff/**"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/internal/staff/**"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")

                        .anyRequest().authenticated()
                )

                // ===================== JWT CONFIG =====================
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                );

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String scope = jwt.getClaimAsString("scope");

            if (scope == null || scope.isBlank()) {
                return Collections.emptyList();
            }

            return Arrays.stream(scope.split(" "))
                    .map(role -> role.startsWith("ROLE_")
                            ? new SimpleGrantedAuthority(role)
                            : new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        });

        return converter;
    }
}