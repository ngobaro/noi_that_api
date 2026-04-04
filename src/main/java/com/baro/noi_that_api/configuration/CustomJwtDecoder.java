package com.baro.noi_that_api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${spring.jwt.signerKey}")
    private String signerKey;

    private NimbusJwtDecoder nimbusJwtDecoder;

    @Override
    public Jwt decode(String token) throws JwtException {
        System.out.println("CustomJwtDecoder được gọi với token: " + token.substring(0, 20) + "..."); // debug

        if (Objects.isNull(nimbusJwtDecoder)) {
            System.out.println("Tạo NimbusJwtDecoder với key: " + signerKey.substring(0, 10) + "...");
            SecretKeySpec secretKey = new SecretKeySpec(signerKey.getBytes(), "HmacSHA256");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKey)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build();
        }

        try {
            Jwt jwt = nimbusJwtDecoder.decode(token);
            System.out.println("Token valid, subject: " + jwt.getSubject());
            return jwt;
        } catch (Exception e) {
            System.out.println("Decode fail: " + e.getMessage());
            throw e;
        }
    }
}