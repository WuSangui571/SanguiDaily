package com.sangui.sanguidaily.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sangui.sanguidaily.dto.WechatSessionResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WechatAuthService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String appid;
    private final String secret;

    public WechatAuthService(
        ObjectMapper objectMapper,
        @Value("${wechat.appid}") String appid,
        @Value("${wechat.secret}") String secret
    ) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
        this.appid = appid;
        this.secret = secret;
    }

    public WechatSessionResponse exchangeCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code is required");
        }
        URI uri = UriComponentsBuilder.fromHttpUrl("https://api.weixin.qq.com/sns/jscode2session")
            .queryParam("appid", appid)
            .queryParam("secret", secret)
            .queryParam("js_code", code)
            .queryParam("grant_type", "authorization_code")
            .build(true)
            .encode(StandardCharsets.UTF_8)
            .toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).build();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        String body = Optional.ofNullable(response.getBody()).orElse("");
        try {
            WechatSessionResponse session = objectMapper.readValue(body, WechatSessionResponse.class);
            if (session.errcode() != null && session.errcode() != 0) {
                throw new IllegalStateException("wechat login failed: " + session.errmsg());
            }
            if (session.openid() == null || session.openid().isBlank()) {
                throw new IllegalStateException("wechat login failed: openid missing");
            }
            return session;
        } catch (Exception ex) {
            throw new IllegalStateException("wechat login failed: parse error", ex);
        }
    }
}
