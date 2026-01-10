package com.sangui.sanguidaily.api;

import com.sangui.sanguidaily.model.User;
import com.sangui.sanguidaily.service.UserService;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    public ResponseEntity<User> getCurrent(
        @RequestParam(value = "id", required = false) Long id,
        @RequestParam(value = "openid", required = false) String openid
    ) {
        Optional<User> user = userService.getCurrentUser(id, openid);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        Optional<User> user = userService.getCurrentUser(id, null);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
