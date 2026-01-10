package com.sangui.sanguidaily.service;

import com.sangui.sanguidaily.model.User;
import com.sangui.sanguidaily.repository.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
