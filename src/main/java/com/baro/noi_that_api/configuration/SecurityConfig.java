package com.baro.noi_that_api.configuration;

import com.baro.noi_that_api.common.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomJwtDecoder customJwtDecoder;
    private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
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

                        // ===================== PUBLIC =====================
                        .requestMatchers(HttpMethod.POST,
                                "/api/internal/auth/login",
                                "/api/internal/auth/forgot-password",
                                "/api/internal/auth/verify-otp",
                                "/api/internal/auth/reset-password",
                                "/api/internal/customers/register",
                                "/api/internal/customers/google",
                                "/api/internal/customers/verify"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/api/internal/customers/email/**",
                                "/api/internal/staff/email/**",
                                "/api/internal/payments/vnpay/callback"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST,
                                "/api/internal/payments/vnpay/callback"
                        ).permitAll()


                        // ===================== CUSTOMER + STAFF + ADMIN =====================
                        .requestMatchers(HttpMethod.GET,
                                "/api/internal/orders/**",
                                "/api/internal/order-details/**",
                                "/api/internal/payments/**",
                                "/api/internal/promotions/**"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF", "ROLE_CUSTOMER")


                        // ⚠️ CATEGORY PROMOTION: CHỈ STAFF + ADMIN (KHÔNG cho CUSTOMER)
                        .requestMatchers(HttpMethod.GET,
                                "/api/internal/category-promotions/**"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")


                        // ===================== CUSTOMER =====================
                        .requestMatchers(HttpMethod.PUT,
                                "/api/internal/customers/**"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER")

                        .requestMatchers(HttpMethod.POST,
                                "/api/internal/orders",
                                "/api/internal/payments/vnpay/create"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_CUSTOMER")


                        // ===================== STAFF & ADMIN =====================
                        .requestMatchers(HttpMethod.GET,
                                "/api/internal/staff/**",
                                "/api/internal/customers"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/internal/orders/**"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_STAFF")


                        // ===================== ADMIN ONLY =====================

                        // Staff management
                        .requestMatchers(HttpMethod.POST,
                                "/api/internal/staff"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/internal/staff/**"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/internal/staff/**"
                        ).hasAuthority("ROLE_ADMIN")


                        // Promotions
                        .requestMatchers(HttpMethod.POST,
                                "/api/internal/promotions"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/internal/promotions/**"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/internal/promotions/**"
                        ).hasAuthority("ROLE_ADMIN")


                        // CATEGORY PROMOTION (mapping internal)
                        .requestMatchers(HttpMethod.POST,
                                "/api/internal/category-promotions"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/internal/category-promotions/**"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/internal/category-promotions/**"
                        ).hasAuthority("ROLE_ADMIN")


                        // Payments admin control
                        .requestMatchers(HttpMethod.PUT,
                                "/api/internal/payments/**"
                        ).hasAuthority("ROLE_ADMIN")


                        // ===================== FALLBACK =====================
                        .anyRequest().authenticated()
                )

                // ===================== JWT RESOURCE SERVER =====================
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)

                        // ==================== 403 HANDLER ====================
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json;charset=UTF-8");

                            ApiResponse<?> apiResponse = ApiResponse.builder()
                                    .code(403)
                                    .message("Bạn không có quyền thực hiện thao tác này.")
                                    .build();

                            try {
                                new ObjectMapper().writeValue(response.getWriter(), apiResponse);
                            } catch (Exception e) {
                                response.getWriter().write("{\"code\":403,\"message\":\"Access Denied\"}");
                            }

                            response.flushBuffer();
                        })
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