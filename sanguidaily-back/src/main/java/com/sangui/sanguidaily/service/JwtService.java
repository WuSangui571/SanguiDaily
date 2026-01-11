package com.sangui.sanguidaily.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String HMAC_ALG = "HmacSHA256";

    private final ObjectMapper objectMapper;
    private final String secret;
    private final long expireSeconds;

    public JwtService(
        ObjectMapper objectMapper,
        @Value("${app.jwt.secret}") String secret,
        @Value("${app.jwt.expire-days:30}") long expireDays
    ) {
        this.objectMapper = objectMapper;
        this.secret = secret;
        this.expireSeconds = expireDays * 24 * 60 * 60;
    }

    public String createToken(Long userId, String openid, String role) {
        long now = Instant.now().getEpochSecond();
        long exp = now + expireSeconds;
        java.util.Map<String, Object> payload = new java.util.HashMap<>();
        if (userId != null) {
            payload.put("sub", userId);
        }
        if (openid != null) {
            payload.put("openid", openid);
        }
        if (role != null) {
            payload.put("role", role);
        }
        payload.put("iat", now);
        payload.put("exp", exp);
        return sign(payload);
    }

    public Optional<JwtUser> parseToken(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            return Optional.empty();
        }
        String token = authHeader.trim();
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return Optional.empty();
        }
        String header = parts[0];
        String payload = parts[1];
        String signature = parts[2];
        String signingInput = header + "." + payload;
        String expected = hmacSha256(signingInput);
        if (!safeEquals(expected, signature)) {
            return Optional.empty();
        }
        try {
            byte[] payloadBytes = Base64.getUrlDecoder().decode(payload);
            Map<String, Object> payloadMap = objectMapper.readValue(
                payloadBytes,
                new TypeReference<>() {}
            );
            long now = Instant.now().getEpochSecond();
            Object expObj = payloadMap.get("exp");
            long exp = expObj instanceof Number ? ((Number) expObj).longValue() : 0L;
            if (exp <= 0 || exp < now) {
                return Optional.empty();
            }
            Long userId = null;
            Object sub = payloadMap.get("sub");
            if (sub instanceof Number) {
                userId = ((Number) sub).longValue();
            }
            String openid = payloadMap.get("openid") instanceof String ? (String) payloadMap.get("openid") : null;
            return Optional.of(new JwtUser(userId, openid));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    private String sign(Map<String, Object> payload) {
        try {
            String headerJson = objectMapper.writeValueAsString(Map.of("alg", "HS256", "typ", "JWT"));
            String payloadJson = objectMapper.writeValueAsString(payload);
            String header = base64Url(headerJson.getBytes(StandardCharsets.UTF_8));
            String body = base64Url(payloadJson.getBytes(StandardCharsets.UTF_8));
            String signingInput = header + "." + body;
            String signature = hmacSha256(signingInput);
            return signingInput + "." + signature;
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to sign token", ex);
        }
    }

    private String hmacSha256(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALG);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALG));
            byte[] digest = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return base64Url(digest);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to sign token", ex);
        }
    }

    private String base64Url(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    private boolean safeEquals(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        return java.security.MessageDigest.isEqual(
            a.getBytes(StandardCharsets.UTF_8),
            b.getBytes(StandardCharsets.UTF_8)
        );
    }

    public record JwtUser(Long userId, String openid) {}
}
