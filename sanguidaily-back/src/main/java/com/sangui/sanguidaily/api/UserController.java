package com.sangui.sanguidaily.api;

import com.sangui.sanguidaily.model.User;
import com.sangui.sanguidaily.service.JwtService;
import com.sangui.sanguidaily.service.UserService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/current")
    public ResponseEntity<User> getCurrent(
        @RequestHeader(value = "Authorization", required = false) String authorization,
        @RequestParam(value = "id", required = false) Long id,
        @RequestParam(value = "openid", required = false) String openid
    ) {
        boolean hasAuthHeader = authorization != null && !authorization.isBlank();
        Optional<JwtService.JwtUser> jwtUser = jwtService.parseToken(authorization);
        if (jwtUser.isPresent()) {
            JwtService.JwtUser payload = jwtUser.get();
            Optional<User> user = userService.getCurrentUser(payload.userId(), payload.openid());
            return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
        if (hasAuthHeader) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (id != null || (openid != null && !openid.isBlank())) {
            Optional<User> user = userService.getCurrentUser(id, openid);
            return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.ok(userService.buildGuestUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        Optional<User> user = userService.getCurrentUser(id, null);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
