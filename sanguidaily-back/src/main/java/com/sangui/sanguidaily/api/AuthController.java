package com.sangui.sanguidaily.api;

import com.sangui.sanguidaily.dto.AuthResponse;
import com.sangui.sanguidaily.dto.ApiError;
import com.sangui.sanguidaily.dto.WechatLoginRequest;
import com.sangui.sanguidaily.dto.WechatSessionResponse;
import com.sangui.sanguidaily.model.User;
import com.sangui.sanguidaily.service.JwtService;
import com.sangui.sanguidaily.service.UserService;
import com.sangui.sanguidaily.service.WechatAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final WechatAuthService wechatAuthService;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(
        WechatAuthService wechatAuthService,
        UserService userService,
        JwtService jwtService
    ) {
        this.wechatAuthService = wechatAuthService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/wechat")
    public ResponseEntity<?> loginByWechat(@RequestBody WechatLoginRequest request) {
        try {
            WechatSessionResponse session = wechatAuthService.exchangeCode(request.code());
            User user = userService.loginOrRegisterByWechat(
                session.openid(),
                request.nickname(),
                request.avatarUrl()
            );
            String token = jwtService.createToken(user.id(), user.openid(), user.role());
            return ResponseEntity.ok(new AuthResponse(token, user));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            String message = ex.getMessage() == null ? "登录失败" : ex.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(message));
        }
    }
}
