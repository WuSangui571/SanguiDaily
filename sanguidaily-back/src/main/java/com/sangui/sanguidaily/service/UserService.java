package com.sangui.sanguidaily.service;

import com.sangui.sanguidaily.model.User;
import com.sangui.sanguidaily.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final String ownerOpenid;

    public UserService(
        UserRepository userRepository,
        @Value("${app.owner-openid:}") String ownerOpenid
    ) {
        this.userRepository = userRepository;
        this.ownerOpenid = ownerOpenid == null ? "" : ownerOpenid.trim();
    }

    public Optional<User> getCurrentUser(Long id, String openid) {
        if (id != null) {
            return userRepository.findById(id);
        }
        if (openid != null && !openid.isBlank()) {
            return userRepository.findByOpenid(openid);
        }
        return userRepository.findFirst();
    }

    public User loginOrRegisterByWechat(String openid, String nickname, String avatarUrl) {
        String cleanNickname = normalizeText(nickname);
        String cleanAvatar = normalizeText(avatarUrl);
        if (isPlaceholderNickname(cleanNickname)) {
            cleanNickname = null;
            cleanAvatar = null;
        }
        Optional<User> existing = userRepository.findByOpenid(openid);
        LocalDateTime now = LocalDateTime.now();
        boolean isOwner = !ownerOpenid.isBlank() && ownerOpenid.equals(openid);
        if (existing.isPresent()) {
            User user = existing.get();
            userRepository.updateLoginProfile(user.id(), cleanNickname, cleanAvatar, now);
            if (isOwner && !"OWNER".equals(user.role())) {
                userRepository.updateRole(user.id(), "OWNER");
            }
            return userRepository.findById(user.id()).orElse(user);
        }
        String role = isOwner ? "OWNER" : "VIEWER";
        return userRepository.insert(openid, role, cleanNickname, cleanAvatar, now)
            .orElseThrow(() -> new IllegalStateException("create user failed"));
    }

    public User buildGuestUser() {
        return new User(
            null,
            null,
            "VIEWER",
            null,
            null,
            null,
            null,
            null
        );
    }

    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private boolean isPlaceholderNickname(String nickname) {
        return "微信用户".equals(nickname);
    }
}
